package com.delta.kylintask.quartz;

import com.delta.kylintask.kylinclient.HttpClientFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

public class ResumeJob implements Job {
    @Autowired
    private HttpClientFactory httpClientFactory;
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

    }
}
