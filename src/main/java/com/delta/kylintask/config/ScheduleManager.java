package com.delta.kylintask.config;

import com.delta.kylintask.dto.TaskDto;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ScheduleManager {
    @Autowired
    private Scheduler scheduler;

    public void addJob(String jobName, String jobGroupName, JobDataMap jobDataMap,
                              String triggerName, String triggerGroupName, Class jobClass, String cron) {
        try {
            JobDetail jobDetail = JobBuilder.newJob(jobClass)
                    .usingJobData(jobDataMap)
                    .withIdentity(jobName, jobGroupName).build();

            TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
            triggerBuilder.withIdentity(triggerName, triggerGroupName);
            triggerBuilder.startNow();
            triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(cron));
            CronTrigger trigger = (CronTrigger) triggerBuilder.build();
            scheduler.scheduleJob(jobDetail, trigger);
            if (!scheduler.isShutdown()) {
                scheduler.start();
            }
            log.info("start job {}, trigger: {}, class: {}, cron: {}.", jobName, triggerName, jobClass.getName(), cron);
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

    public void removeJob(String jobName, String jobGroupName,
                                 String triggerName, String triggerGroupName) {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);
            scheduler.pauseTrigger(triggerKey);
            scheduler.unscheduleJob(triggerKey);
            scheduler.deleteJob(JobKey.jobKey(jobName, jobGroupName));
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
}
