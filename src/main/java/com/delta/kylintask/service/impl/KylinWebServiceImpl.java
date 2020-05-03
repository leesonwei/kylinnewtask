package com.delta.kylintask.service.impl;

import com.delta.kylintask.commons.ServerResponse;
import com.delta.kylintask.entity.Cube;
import com.delta.kylintask.entity.Kylin;
import com.delta.kylintask.entity.KylinJob;
import com.delta.kylintask.entity.Project;
import com.delta.kylintask.kylinclient.HttpClientFactory;
import com.delta.kylintask.kylinclient.KylinClient;
import com.delta.kylintask.service.KylinService;
import com.delta.kylintask.service.KylinWebService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class KylinWebServiceImpl implements KylinWebService {
    @Autowired
    private HttpClientFactory httpClientFactory;
    @Autowired
    private KylinService kylinService;

    @Override
    public ServerResponse<List<Project>> getProjects(String clientKey) {
        KylinClient client = httpClientFactory.getKylinClient(clientKey);
        try {
            List<Project> projects = client.getProjects();
            return ServerResponse.createBySuccess(projects);
        } catch (Exception e) {
            log.error(e.getMessage() + ", {}", e.getCause());
            return ServerResponse.createByErrorMessage(e.getMessage());
        }
    }

    @Override
    public ServerResponse<String> connect(Kylin kylin) {
        KylinClient client = httpClientFactory.getKylinClient(kylin);
        try {
            String key = client.connect();
            if (null == kylinService.selectById(key)) {
                kylinService.insert(kylin);
            }
            return ServerResponse.createBySuccess(key);
        } catch (Exception e) {
            log.error(e.getMessage() + ", {}", e.getCause());
            return ServerResponse.createByErrorMessage(e.getMessage());
        }
    }

    @Override
    public ServerResponse<List<Cube>> getCubes(String clientKey, String projectName) {
        KylinClient client = httpClientFactory.getKylinClient(clientKey);
        try {
            return ServerResponse.createBySuccess(client.getCubes(projectName));
        } catch (Exception e) {
            return ServerResponse.createByErrorMessage(e.getMessage());
        }
    }

    @Override
    public ServerResponse<Cube> getCube(String clientKey, Cube cube) {
        KylinClient client = httpClientFactory.getKylinClient(clientKey);
        try {
            return ServerResponse.createBySuccess(client.getCube(cube));
        } catch (Exception e) {
            return ServerResponse.createByErrorMessage(e.getMessage());
        }
    }

    @Override
    public ServerResponse<KylinJob> getJob(String clientKey, KylinJob kylinJob) {
        KylinClient client = httpClientFactory.getKylinClient(clientKey);
        try {
            return ServerResponse.createBySuccess(client.getJob(kylinJob.getUuid()));
        } catch (Exception e) {
            return ServerResponse.createByErrorMessage(e.getMessage());
        }
    }

    @Override
    public ServerResponse<List<KylinJob>> getJobs(String clientKey, List<String> jobids) {
        List<KylinJob> kylinJobs = new ArrayList<>();
        KylinJob jobDto = new KylinJob();
        for (String jobid : jobids) {
            jobDto.setUuid(jobid);
            ServerResponse<KylinJob> result= getJob(clientKey, jobDto);
            kylinJobs.add(result.getData());
        }
        return ServerResponse.createBySuccess(kylinJobs);
    }
}
