package com.delta.kylintask.quartz;

import com.alibaba.fastjson.JSON;
import com.delta.kylintask.commons.Constants;
import com.delta.kylintask.config.KylinProperties;
import com.delta.kylintask.config.ScheduleManager;
import com.delta.kylintask.dto.TargetDataDto;
import com.delta.kylintask.dto.TaskDto;
import com.delta.kylintask.entity.KylinJob;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.listeners.JobListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
public class BuildJobListener extends JobListenerSupport {

    @Autowired
    private ScheduleManager scheduleManager;
    @Autowired
    private KylinProperties kylinProperties;

    @Override
    public String getName() {
        return "BuildJobListener";
    }

    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
        if (null == context.getResult()) {
            return;
        }
        synchronized (this) {
            KylinJob result = (KylinJob) context.getResult();
            TaskDto taskDto = getTaskDto(result);
            scheduleManager.addJob(taskDto);
            log.info("Monitor job was create. {}", taskDto);
        }
    }

    private TaskDto getTaskDto(KylinJob result) {
        TaskDto taskDto = new TaskDto();
        TargetDataDto targetDataDto = new TargetDataDto();
        targetDataDto.setAction(Constants.MONITOR);
        targetDataDto.setJobUuid(result.getUuid());
        taskDto.setLimit(false);
        taskDto.setCron(kylinProperties.getMonitor().getCron());
        taskDto.setResume(result.isResume());
        taskDto.setResumeTimes(result.getResumeTimes());
        taskDto.setKylinid(result.getKylinId());
        taskDto.setTargetData(JSON.toJSONString(targetDataDto));
        return taskDto;
    }
}
