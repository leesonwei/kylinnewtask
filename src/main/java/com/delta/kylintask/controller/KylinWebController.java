package com.delta.kylintask.controller;

import com.delta.kylintask.commons.ServerResponse;
import com.delta.kylintask.entity.Cube;
import com.delta.kylintask.entity.Kylin;
import com.delta.kylintask.entity.KylinJob;
import com.delta.kylintask.entity.Project;
import com.delta.kylintask.service.KylinWebService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/kylinweb")
@Api(tags = "Kylin 服務信息相关接口", description = "提供Kylin 服務信息相关的 Rest API")
public class KylinWebController {
    @Autowired
    private KylinWebService kylinWebService;

    @PostMapping("/connect")
    public ServerResponse<String> connect(Kylin kylin){
        kylin.setId(kylin.hashCode());
        return kylinWebService.connect(kylin);
    }

    @GetMapping("/projects")
    public ServerResponse<List<Project>> projects(String clientKey){
        return kylinWebService.getProjects(clientKey);
    }

    @GetMapping("/cubes")
    public ServerResponse<List<Cube>> cubes(String clientKey, String projectName){
        return kylinWebService.getCubes(clientKey, projectName);
    }

    @GetMapping("/cubes/{cube_name}")
    public ServerResponse<Cube> cube(String clientKey, @PathVariable("cube_name") String cubeName){
        Cube cube = new Cube();
        cube.setName(cubeName);
        return kylinWebService.getCube(clientKey, cube);
    }

    @GetMapping("/jobs/{job_uuid}")
    public ServerResponse<KylinJob> job(String clientKey, @PathVariable("job_uuid") String jobUuid){
        KylinJob kylinJob = new KylinJob();
        kylinJob.setUuid(jobUuid);
        return kylinWebService.getJob(clientKey, kylinJob);
    }

    @GetMapping("/jobs")
    public ServerResponse<List<KylinJob>> jobs(String clientKey, List<String> jobids){
        return kylinWebService.getJobs(clientKey, jobids);
    }
}
