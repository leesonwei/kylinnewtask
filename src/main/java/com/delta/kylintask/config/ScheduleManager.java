package com.delta.kylintask.config;

import com.alibaba.fastjson.JSON;
import com.delta.kylintask.commons.ServerResponse;
import com.delta.kylintask.dto.TargetDataDto;
import com.delta.kylintask.dto.TaskDto;
import com.delta.kylintask.quartz.BuildJob;
import com.delta.kylintask.quartz.BuildJobListener;
import com.delta.kylintask.quartz.MonitorJob;
import com.delta.kylintask.quartz.MonitorJobListener;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.listeners.JobListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class ScheduleManager {
    @Autowired
    private Scheduler scheduler;

    public List<TaskDto> getJobs(String kylinId) {
        List<TaskDto> taskDtos = new ArrayList<>();
        TaskDto taskDto = new TaskDto();
        try {
            for (String groupName : scheduler.getJobGroupNames()) {
                if (kylinId.equals(groupName)) {
                    for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
                        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
                        CronTrigger trigger = (CronTrigger) scheduler.getTriggersOfJob(jobKey).get(0);
                        Date nextFireTime = trigger.getNextFireTime();
                        taskDto.setTaskName(jobDetail.getKey().getName());
                        taskDto.setKylinid(kylinId);
                        taskDto.setStartAt(trigger.getStartTime());
                        taskDto.setEndAt(trigger.getEndTime());
                        taskDto.setNextFireTime(nextFireTime);
                        taskDto.setStatus(scheduler.getTriggerState(trigger.getKey()).name());
                        taskDto.setCron(trigger.getCronExpression());
                        taskDto.setDescription(jobDetail.getDescription());
                        taskDtos.add(taskDto);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return taskDtos;
    }
    private Class getClass(String buildType){
        switch (buildType) {
            case "BUILD" :
            case "REFRESH" : {
                return BuildJob.class;
            }
            case "MONITOR" : {
                return MonitorJob.class;
            }
            default : {
                throw new RuntimeException("built type so not match.");
            }
        }
    }
    private JobListenerSupport getListener(String buildType) {
        switch (buildType) {
            case "BUILD" :
            case "REFRESH" : {
                return new BuildJobListener();
            }
            case "MONITOR" : {
                return new MonitorJobListener();
            }
            default : {
                throw new RuntimeException("built type so not match.");
            }
        }
    }

    public TaskDto addJob(TaskDto taskDto) {
        try {
            TargetDataDto targetDataDto = taskDto.getTargetDataDto();
            JobDetail jobDetail = JobBuilder.newJob(getClass(targetDataDto.getBuildType()))
                    .usingJobData(new JobDataMap(BeanMap.create(taskDto)))
                    .usingJobData(new JobDataMap(BeanMap.create(targetDataDto)))
                    .withDescription(taskDto.getDescription())
                    .withIdentity(targetDataDto.getCubeJobName(), taskDto.getKylinid()).build();
            TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
            triggerBuilder.withIdentity(targetDataDto.getCubeJobName(), taskDto.getKylinid());
            if (taskDto.isLimit()) {
                triggerBuilder.startAt(taskDto.getStartAt()).endAt(taskDto.getEndAt());
            } else {
                triggerBuilder.startNow();
            }
            triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(taskDto.getCron()));

            Trigger trigger = triggerBuilder.build();
            scheduler.scheduleJob(jobDetail, trigger);
            if (!scheduler.isShutdown()) {
                scheduler.start();
            }
            taskDto.setTaskName(targetDataDto.getCubeJobName());
            taskDto.setNextFireTime(new Timestamp(trigger.getNextFireTime().getTime()));
            Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
            taskDto.setStatus(triggerState.name());
            scheduler.getListenerManager().addJobListener(
                    getListener(targetDataDto.getBuildType()), GroupMatcher.jobGroupEquals(taskDto.getKylinid()));
            log.info("add job {}.", JSON.toJSONString(taskDto));
            return taskDto;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param triggerName      触发器名
     * @param triggerGroupName 触发器组名
     * @param cron             时间设置，参考quartz说明文档
     * @Description: 修改一个任务的触发时间
     */
    public void modifyJobTime(String triggerName, String triggerGroupName, String cron) {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            if (trigger == null) {
                return;
            }

            String oldTime = trigger.getCronExpression();
            if (!oldTime.equalsIgnoreCase(cron)) {
                TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
                triggerBuilder.withIdentity(triggerName, triggerGroupName);
                triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(cron));
                trigger = (CronTrigger) triggerBuilder.build();
                scheduler.rescheduleJob(triggerKey, trigger);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void pauseJob(){

    }

    public void resumeJob(){

    }

    public void modifyJobDetail(String jobName,String jobGroupName, JobDataMap jobDataMap,
                                       String triggerName, String triggerGroupName, String cron) {
        try {
            JobKey jobKey = JobKey.jobKey(jobName, jobGroupName);
            TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            if (jobDetail == null) {
                return;
            }
            jobDetail = jobDetail.getJobBuilder().setJobData(jobDataMap)
                    .withIdentity(jobName, jobGroupName)
                    .build();
            scheduler.scheduleJob(jobDetail,trigger);
            if (null != cron) {
                modifyJobTime(triggerName,triggerGroupName,cron);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void removeJob(TaskDto taskDto) {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(taskDto.getTaskName(), taskDto.getKylinid());
            scheduler.pauseTrigger(triggerKey);
            scheduler.unscheduleJob(triggerKey);
            scheduler.deleteJob(JobKey.jobKey(taskDto.getTaskName(), taskDto.getKylinid()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public void startJobs() {
        try {
            scheduler.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public void shutdownJobs() {
        try {
            if (!scheduler.isShutdown()) {
                scheduler.shutdown();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void clear(String kylinId) {
        try {
            scheduler.clear();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}

