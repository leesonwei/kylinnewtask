package com.delta.kylintask.quartz;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.listeners.JobListenerSupport;

@Slf4j
public class MonitorJobListener extends JobListenerSupport {
    @Override
    public String getName() {
        return "MonitorJobListener";
    }

    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        log.info("MonitorJobListener job listener {}", context.getResult());
    }
}
