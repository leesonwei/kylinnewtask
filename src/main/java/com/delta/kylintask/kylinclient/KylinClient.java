package com.delta.kylintask.kylinclient;

import com.delta.kylintask.dto.BuildCubeDto;
import com.delta.kylintask.entity.Cube;
import com.delta.kylintask.entity.KylinJob;
import com.delta.kylintask.entity.Project;
import okhttp3.Callback;
import org.quartz.JobExecutionException;

import java.util.List;

public interface KylinClient {
    String connect() throws Exception;
    List<Project> getProjects() throws Exception;
    List<Cube> getCubes(String projectName) throws Exception;
    Cube getCube(Cube cube) throws Exception;
    String buildCube(BuildCubeDto buildCubeDto) throws JobExecutionException;
    KylinJob resumeJob(KylinJob job) throws JobExecutionException;
    KylinJob getJob(String jobUuid) throws JobExecutionException;
    List<KylinJob> getJobs(String cubeName) throws JobExecutionException;
}
