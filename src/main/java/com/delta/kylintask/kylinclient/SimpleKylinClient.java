package com.delta.kylintask.kylinclient;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.delta.kylintask.commons.Http;
import com.delta.kylintask.entity.Cube;
import com.delta.kylintask.entity.Kylin;
import com.delta.kylintask.entity.KylinJob;
import com.delta.kylintask.entity.Project;
import com.delta.kylintask.exception.KylinException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Data
@Slf4j
public class SimpleKylinClient implements KylinClient {
    private final static String baseurl="kylin/api";
    private final static String auth="user/authentication";
    private final static String cubes="cubes";
    private final static String rebuild="cubes/{cube_name}/rebuild";
    private final static String build="cubes/{cube_name}/build";
    private final static String streamingbuild="cubes/{cube_name}/build2";
    private final static String streamingrebuild="cubes/{cube_name}/rebuild2";
    private final static String jobs="jobs";
    private final static String projects="projects";
    private final static String resume="jobs/{job_uuid}/resume";

    private Kylin kylin;
    private Headers headers;
    private OkHttpClient okHttpClient;

    @Override
    public String connect() throws Exception {
        String url = getUrl(auth);
        RequestBody requestBody = getRequestBody(kylin);
        String requestSync = doRequestSync(url, requestBody, Http.POST);
        if (requestSync.contains("userDetails")) {
            return String.valueOf(kylin.hashCode());
        } else {
            throw new KylinException("can't connect to kylin server");
        }
    }

    @Override
    public List<Project> getProjects() {
        String url = getUrl(projects);
        String project = doRequestSync(url, null, Http.GET);
        List<Project> projects = JSONObject.parseArray(project, Project.class);
        return projects;
    }

    @Override
    public List<Cube> getCubes(String projectName) {
        String url = getUrl(String.format("%s?projectName=%s&offset=%d&limit=%d", cubes,
                projectName, 0, 200));
        String requestSync =  doRequestSync(url, null, Http.GET);
        List<Cube> cubes = JSON.parseArray(requestSync, Cube.class);
        return cubes;
    }

    @Override
    public Cube getCube(Cube cube) {
        String url = getUrl(String.format("%s/%s", cubes,cube.getName()));
        String requestSync =  doRequestSync(url, null, Http.GET);
        Optional<Cube> first;
        try {
            List<Cube> cubes = JSON.parseArray(requestSync, Cube.class);
            first = cubes.parallelStream().filter(m -> m.getName().equals(cube.getName())).findFirst();
        } catch (Exception e) {
            Cube resultCube = JSON.parseObject(requestSync, Cube.class);
            first = Optional.of(resultCube);
        }
        return first.get();
    }

    @Override
    public String buildCube() throws Exception {
        return null;
    }

    @Override
    public void resumeJob(KylinJob job, Callback callback) throws Exception {

    }


    @Override
    public KylinJob getJob(KylinJob job, Callback callback) {
        String url = getUrl(String.format("%s/%s", jobs, job.getUuid()));
        String requestSync =  doRequestSync(url, null, Http.GET);
        return JSON.parseObject(requestSync, KylinJob.class);
    }

    private String getUrl(String detail){
        String url = String.format("%s://%s:%d/%s/%s",kylin.getProtocol(),
                kylin.getHost(), kylin.getPort(), baseurl, detail);
        return url;
    }

    private void doRequestAync(String url, RequestBody requestBody, Http restType, Callback callback){
        Request request = getRequest(url, requestBody, restType);
        okHttpClient.newCall(request).enqueue(callback);
    }

    private Request getRequest(String url, RequestBody requestBody, Http restType){
        Request.Builder builder = new Request.Builder().url(url)
                .headers(initHeader(kylin));
        Request request = null;
        if (restType.equals(Http.PUT)) {
            request = builder.put(requestBody).build();
        } else if (restType.equals(Http.POST)) {
            request = builder.post(requestBody).build();
        } else if (restType.equals(Http.DELETE)) {
            request = builder.delete(requestBody).build();
        } else {
            request = builder.build();
        }
        return request;
    }

    private RequestBody getRequestBody(Object obj){
        RequestBody requestBody = FormBody.create(JSON.toJSONString(obj), MediaType.parse("application/json; charset=utf-8"));
        return requestBody;
    }

    private String doRequestSync(String url, RequestBody requestBody, Http restType){
        Request request = getRequest(url, requestBody, restType);
        Response response = null;
        String responseString = null;
        try {
            Call call = okHttpClient.newCall(request);
            response = call.execute();
            responseString = response.body().string();
            log.debug(responseString);
            return responseString;
        } catch (IOException e) {
            log.error(e.getMessage());
        } finally {
            response.body().close();
        }
        return null;
    }

    private List<Object> doRequestSync(String url, RequestBody requestBody, Http restType, Class result){
        Request request = getRequest(url, requestBody, restType);
        Response response = null;
        String responsBody = null;
        try {
            Call call = okHttpClient.newCall(request);
            response = call.execute();
            responsBody = response.body().string();
            List<Object> object= JSONObject.parseObject(responsBody, (Type) result);
            return object;
        } catch (IOException e) {
            log.error(e.getMessage());
        } finally {
            response.body().close();
        }
        return null;
    }

    private Headers initHeader(Kylin kylin){
        String authBase64 = new String(Base64.getEncoder().encode(String.format("%s:%s", kylin.getUsername(), kylin.getPassword()).getBytes()));
        return new Headers.Builder()
                .add(String.format("Authorization: Basic %s", authBase64))
                .add("Content-Type","application/json;charset=UTF-8")
                .build();
    }
}
