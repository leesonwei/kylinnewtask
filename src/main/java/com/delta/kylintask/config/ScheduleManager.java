package com.delta.kylintask.config;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.delta.kylintask.commons.Constants;
import com.delta.kylintask.commons.ServerResponse;
import com.delta.kylintask.dto.TargetDataDto;
import com.delta.kylintask.dto.TaskDto;
import com.delta.kylintask.entity.Cube;
import com.delta.kylintask.entity.Kylin;
import com.delta.kylintask.entity.Segment;
import com.delta.kylintask.mapper.KylinMapper;
import com.delta.kylintask.quartz.*;
import com.delta.kylintask.service.KylinService;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.AfterMapping;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.impl.matchers.KeyMatcher;
import org.quartz.listeners.JobListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.*;

@Component
@Slf4j
public class ScheduleManager {
    @Autowired
    private Scheduler scheduler;
    @Autowired
    private KylinMapper kylinMapper;

    @PostConstruct
    public void addListener() throws SchedulerException {
        //todo KylinService获取全部
        List<Kylin> kylinList = kylinMapper.selectList(new EntityWrapper<Kylin>().isNotNull("id"));
        for (Kylin kylin : kylinList) {
            scheduler.getListenerManager().addJobListener(new BuildJobListener(),
                    GroupMatcher.jobGroupEquals(kylin.getId() + Constants.BUILD));
            scheduler.getListenerManager().addJobListener(new BuildJobListener(),
                    GroupMatcher.jobGroupEquals(kylin.getId() + Constants.REFRESH));
            scheduler.getListenerManager().addJobListener(new MonitorJobListener(),
                    GroupMatcher.jobGroupEquals(kylin.getId() + Constants.MONITOR));
            scheduler.getListenerManager().addJobListener(new ResumeJobListener(),
                    GroupMatcher.jobGroupEquals(kylin.getId() + Constants.RESUME));
        }

    }

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
                        taskDto.setGroupName(jobKey.getGroup());
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

    private String getCubeJobName(TargetDataDto targetDataDto){
        Cube cube = JSON.parseObject(targetDataDto.getCube(), Cube.class);
        Segment segment = JSON.parseObject(targetDataDto.getSegment(), Segment.class);
        return String.format("%s_%s_%s", targetDataDto.getAction(), cube.getName(), segment.getName());
    }

    private String getMonitorJobName(TargetDataDto targetDataDto){
        return String.format("%s_%s", targetDataDto.getAction(), targetDataDto.getJobUuid());
    }

    private Map<String, Object> getJobInfo(String buildType){
        Map<String, Object> jobInfo =new HashMap<>();
        switch (buildType) {
            case Constants.REFRESH :
            case Constants.BUILD : {
                jobInfo.put("listener", new BuildJobListener());
                jobInfo.put("clazz", BuildJob.class);
                jobInfo.put("jobName", "getCubeJobName");
                return jobInfo;
            }
            case Constants.MONITOR : {
                jobInfo.put("listener", new MonitorJobListener());
                jobInfo.put("clazz", MonitorJob.class);
                jobInfo.put("jobName", "getMonitorJobName");
                return jobInfo;
            }
            case Constants.RESUME : {
                jobInfo.put("listener", new ResumeJobListener());
                jobInfo.put("clazz", ResumeJob.class);
                jobInfo.put("jobName", "getMonitorJobName");
                return jobInfo;
            }
            default : {
                throw new RuntimeException("built type do not match.");
            }
        }
    }

    public TaskDto addJob(TaskDto taskDto) {
        try {
            TargetDataDto targetDataDto = JSON.parseObject(taskDto.getTargetData(), TargetDataDto.class);
            String group = String.format("%s_%s", taskDto.getKylinid(), targetDataDto.getAction());
            Map<String, Object> jobInfo = getJobInfo(targetDataDto.getAction());
            Method method = this.getClass().getDeclaredMethod((String) jobInfo.get("jobName"), TargetDataDto.class);
            method.setAccessible(true);
            String jobName = (String) method.invoke(this, targetDataDto);
            JobDetail jobDetail = JobBuilder.newJob((Class<? extends Job>) jobInfo.get("clazz"))
                    .usingJobData(new JobDataMap(BeanMap.create(taskDto)))
                    .withDescription(taskDto.getDescription())
                    .withIdentity(jobName, group).build();
            TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
            triggerBuilder.withIdentity(jobName, group);
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
            taskDto.setTaskName(jobName);
            taskDto.setNextFireTime(new Timestamp(trigger.getNextFireTime().getTime()));
            Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
            taskDto.setStatus(triggerState.name());
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

    public TaskDto pauseJob(TaskDto taskDto){
        try {
            JobKey jobKey = JobKey.jobKey(taskDto.getTaskName(), taskDto.getGroupName());
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            if (jobDetail == null) {
                return null;
            }
            scheduler.pauseJob(jobKey);
            Trigger trigger = scheduler.getTriggersOfJob(jobKey).get(0);
            taskDto.setStatus(scheduler.getTriggerState(trigger.getKey()).name());
            return taskDto;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public TaskDto resumeJob(TaskDto taskDto){
        try {
            JobKey jobKey = JobKey.jobKey(taskDto.getTaskName(), taskDto.getGroupName());
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            if (jobDetail == null) {
                return null;
            }
            scheduler.resumeJob(jobKey);
            Trigger trigger = scheduler.getTriggersOfJob(jobKey).get(0);
            taskDto.setStatus(scheduler.getTriggerState(trigger.getKey()).name());
            return taskDto;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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

    public TaskDto removeJob(TaskDto taskDto) {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(taskDto.getTaskName(), taskDto.getGroupName());
            scheduler.pauseTrigger(triggerKey);
            scheduler.unscheduleJob(triggerKey);
            scheduler.deleteJob(JobKey.jobKey(taskDto.getTaskName(), taskDto.getKylinid()));
            return taskDto;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean removeJob(JobDetail jobDetail) {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(jobDetail.getKey().getName(), jobDetail.getKey().getGroup());
            scheduler.pauseTrigger(triggerKey);
            scheduler.unscheduleJob(triggerKey);
            scheduler.deleteJob(jobDetail.getKey());
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
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

