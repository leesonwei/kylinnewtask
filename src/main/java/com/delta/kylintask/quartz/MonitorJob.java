package com.delta.kylintask.quartz;

import com.alibaba.fastjson.JSON;
import com.delta.kylintask.dto.TargetDataDto;
import com.delta.kylintask.dto.TaskDto;
import com.delta.kylintask.entity.KylinJob;
import com.delta.kylintask.kylinclient.HttpClientFactory;
import com.delta.kylintask.kylinclient.KylinClient;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanMap;

@Slf4j
public class MonitorJob implements Job {
    @Autowired
    private HttpClientFactory httpClientFactory;
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        TaskDto taskDto = getTaskDto(context);
        TargetDataDto targetDataDto = JSON.parseObject(taskDto.getTargetData(), TargetDataDto.class);
        KylinClient kylinClient = httpClientFactory.getKylinClient(taskDto.getKylinid());
        KylinJob job = null;
        try {
            job = kylinClient.getJob(targetDataDto.getJobUuid());
        } catch (Exception e) {
            throw new JobExecutionException(e);
        }
        job.setResume(taskDto.isResume());
        job.setResumeTimes(taskDto.getResumeTimes());
        context.setResult(job);
        log.info("monitor job excute, {}", taskDto);
    }

    private TaskDto getTaskDto(JobExecutionContext context) {
        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        BeanMap beanMap = BeanMap.create(new TaskDto());
        beanMap.putAll(jobDataMap);
        TaskDto taskDto = (TaskDto) beanMap.getBean();
        return taskDto;
    }
}
