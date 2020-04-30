package com.delta.kylintask.quartz;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.delta.kylintask.commons.KylinJobConstants;
import com.delta.kylintask.dto.BuildCubeDto;
import com.delta.kylintask.dto.TargetDataDto;
import com.delta.kylintask.dto.TaskDto;
import com.delta.kylintask.entity.Cube;
import com.delta.kylintask.entity.KylinJob;
import com.delta.kylintask.entity.Segment;
import com.delta.kylintask.kylinclient.HttpClientFactory;
import com.delta.kylintask.kylinclient.KylinClient;
import com.delta.kylintask.service.KylinJobService;
import javafx.concurrent.Task;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import springfox.documentation.spring.web.json.Json;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Slf4j
public class BuildJob implements Job {
    @Autowired
    private HttpClientFactory httpClientFactory;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        TaskDto taskDto = getTaskDto(context);
        KylinClient kylinClient = httpClientFactory.getKylinClient(taskDto.getKylinid());
        BuildCubeDto buildCubeDto = getBuildCubeDto(context);
        List<KylinJob> jobs = kylinClient.getJobs(buildCubeDto.getCubeName());
        if (checkJobExsist(jobs, buildCubeDto)) {
            log.info("cube {} segment {} job is working. wait...", buildCubeDto.getCubeName(), buildCubeDto.getSegment());
            return;
        }
        String result = kylinClient.buildCube(buildCubeDto);
        KylinJob kylinJob = JSON.parseObject(result, KylinJob.class);
        kylinJob.setResume(taskDto.isResume());
        kylinJob.setResumeTimes(taskDto.getResumeTimes());
        context.setResult(kylinJob);
        log.info("excuted job id {}. detail: {}", kylinJob.getUuid(), JSON.toJSONString(taskDto));
    }

    private boolean checkJobExsist(List<KylinJob> jobs, BuildCubeDto buildCubeDto){
        for (KylinJob job : jobs) {
            String segmentName = job.getName().split("-")[2].trim();
            if (segmentName.equals(buildCubeDto.getSegment()) && !job.getJobStatus().equals(KylinJobConstants.FINISHED)) {
                return true;
            }
        }
        return false;
    }

    private TaskDto getTaskDto(JobExecutionContext context) {
        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        BeanMap beanMap = BeanMap.create(new TaskDto());
        beanMap.putAll(jobDataMap);
        TaskDto taskDto = (TaskDto) beanMap.getBean();
        return taskDto;
    }

    private BuildCubeDto getBuildCubeDto(JobExecutionContext context){
        TaskDto taskDto = getTaskDto(context);
        TargetDataDto targetDataDto = JSON.parseObject(taskDto.getTargetData(), TargetDataDto.class);
        BuildCubeDto buildCubeDto = new BuildCubeDto();
        if ("REFRESH".equals(targetDataDto.getAction())) {
            Cube cube = JSON.parseObject(targetDataDto.getCube(), Cube.class);
            Segment segment = JSON.parseObject(targetDataDto.getSegment(), Segment.class);
            buildCubeDto.setCubeName(cube.getName());
            buildCubeDto.setSegment(segment.getName());
            buildCubeDto.setStartTime(segment.getDateRangeStart());
            buildCubeDto.setFullBuild(cube.isFullBuild());
            if (cube.isFullBuild()) {
                buildCubeDto.setBuildType("BUILD");
                buildCubeDto.setEndTime(new Date(System.currentTimeMillis()));
            } else {
                buildCubeDto.setBuildType("REFRESH");
                buildCubeDto.setEndTime(segment.getDateRangeEnd());
            }
            buildCubeDto.setStreaming(cube.isStreaming());
            if (cube.isStreaming()) {
                buildCubeDto.setSourceOffsetStart(segment.getSourceOffsetStart());
                buildCubeDto.setSourceOffsetEnd(segment.getSourceOffsetEnd());
            }
        } else if ("BUILD".equals(targetDataDto.getAction())) {

        }
        return buildCubeDto;
    }
}
