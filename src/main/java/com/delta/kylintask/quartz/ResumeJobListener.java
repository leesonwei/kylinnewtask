package com.delta.kylintask.quartz;

import com.delta.kylintask.commons.KylinJobConstants;
import com.delta.kylintask.entity.KylinJob;
import com.delta.kylintask.kylinclient.HttpClientFactory;
import com.delta.kylintask.kylinclient.KylinClient;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.listeners.JobListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
public class ResumeJobListener extends JobListenerSupport {
    @Autowired
    private HttpClientFactory httpClientFactory;
    @Override
    public String getName() {
        return "ResumeJobListener";
    }

    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
        synchronized (this) {
            KylinJob result = (KylinJob) context.getResult();

            //todo 更新數據庫的值
            log.info("Resume job excuted. {}", result);
        }
    }
}
