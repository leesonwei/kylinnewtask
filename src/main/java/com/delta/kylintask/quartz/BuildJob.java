package com.delta.kylintask.quartz;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.delta.kylintask.dto.TaskDto;
import com.delta.kylintask.entity.KylinJob;
import com.delta.kylintask.kylinclient.HttpClientFactory;
import com.delta.kylintask.kylinclient.KylinClient;
import com.delta.kylintask.service.KylinJobService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanMap;

@Slf4j
public class BuildJob implements Job {
    @Autowired
    private HttpClientFactory httpClientFactory;

    @SneakyThrows
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        BeanMap beanMap = BeanMap.create(new TaskDto());
        beanMap.putAll(jobDataMap);
        TaskDto taskDto = (TaskDto) beanMap.getBean();
        //KylinClient kylinClient = httpClientFactory.getKylinClient(taskDto.getKylinid());
        //todo 给build传参数
        //String result = kylinClient.buildCube();
        //context.setResult(result);
        context.setResult("build cube 111");
        log.info("excuted job {}.", JSON.toJSONString(taskDto));
    }
}
