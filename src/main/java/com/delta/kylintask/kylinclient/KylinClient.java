package com.delta.kylintask.kylinclient;

import com.delta.kylintask.dto.BuildCubeDto;
import com.delta.kylintask.entity.Cube;
import com.delta.kylintask.entity.KylinJob;
import com.delta.kylintask.entity.Project;
import okhttp3.Callback;
import org.quartz.JobExecutionException;

import java.util.List;

public interface KylinClient {
    String connect();
    List<Project> getProjects();
    List<Cube> getCubes(String projectName);
    Cube getCube(Cube cube);
    String buildCube(BuildCubeDto buildCubeDto);
    KylinJob resumeJob(KylinJob job);
    KylinJob getJob(String jobUuid);
    List<KylinJob> getJobs(String cubeName);
}
