package com.delta.kylintask.quartz;

import com.alibaba.fastjson.JSON;
import com.delta.kylintask.commons.Constants;
import com.delta.kylintask.commons.KylinJobConstants;
import com.delta.kylintask.config.KylinProperties;
import com.delta.kylintask.config.ScheduleManager;
import com.delta.kylintask.dto.TargetDataDto;
import com.delta.kylintask.dto.TaskDto;
import com.delta.kylintask.entity.KylinJob;
import com.delta.kylintask.kylinclient.HttpClientFactory;
import com.delta.kylintask.kylinclient.KylinClient;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.listeners.JobListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
public class MonitorJobListener extends JobListenerSupport {
    @Autowired
    private ScheduleManager scheduleManager;
    @Autowired
    private HttpClientFactory httpClientFactory;
    @Autowired
    private KylinProperties kylinProperties;

    @Override
    public String getName() {
        return "MonitorJobListener";
    }

    @SneakyThrows
    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
        if (jobException != null) {
            return;
        }
        synchronized (this) {
            KylinJob result = (KylinJob) context.getResult();
            KylinClient kylinClient = httpClientFactory.getKylinClient(result.getKylinId());

            if (result.getJobStatus().equals(KylinJobConstants.FINISHED)) {
                //todo 更新數據庫的值
                scheduleManager.removeJob(context.getJobDetail());
            }
            if (result.getJobStatus().equals(KylinJobConstants.ERROR)) {
                //todo 獲取數據resumeTimes比較
                kylinClient.resumeJob(result);
                //todo 更新數據庫的值resumetimes+1
                log.info("Resume job excuted. {}", result);
            }
        }
    }

}
