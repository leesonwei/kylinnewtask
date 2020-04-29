package com.delta.kylintask.service;

import com.delta.kylintask.commons.ServerResponse;
import com.delta.kylintask.entity.Cube;
import com.delta.kylintask.entity.Kylin;
import com.delta.kylintask.entity.KylinJob;
import com.delta.kylintask.entity.Project;

import java.util.List;

public interface KylinWebService {
    ServerResponse<List<Project>> getProjects(String kylinid);
    ServerResponse<String> connect(Kylin kylin);
    ServerResponse<List<Cube>> getCubes(String kylinid, String projectName);
    ServerResponse<Cube> getCube(String kylinid, Cube cube);
    ServerResponse<KylinJob> getJob(String kylinid, KylinJob kylinJob);
    ServerResponse<List<KylinJob>> getJobs(String kylinid, List<String> jobids);
}
