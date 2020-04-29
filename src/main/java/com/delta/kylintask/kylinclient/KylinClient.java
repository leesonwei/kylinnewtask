package com.delta.kylintask.kylinclient;

import com.delta.kylintask.entity.Cube;
import com.delta.kylintask.entity.KylinJob;
import com.delta.kylintask.entity.Project;
import okhttp3.Callback;

import java.util.List;

public interface KylinClient {
    String connect() throws Exception;
    List<Project> getProjects() throws Exception;
    List<Cube> getCubes(String projectName);
    Cube getCube(Cube cube);
    String buildCube() throws Exception;
    void resumeJob(KylinJob job, Callback callback) throws Exception;
    KylinJob getJob(KylinJob job, Callback callback);
}
