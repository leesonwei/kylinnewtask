package com.delta.kylintask.kylinclient;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.delta.kylintask.commons.ServerResponse;
import com.delta.kylintask.dto.BuildCubeDto;
import com.delta.kylintask.entity.Cube;
import com.delta.kylintask.entity.Kylin;
import com.delta.kylintask.entity.KylinJob;
import com.delta.kylintask.entity.Project;
import com.delta.kylintask.exception.KylinException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.quartz.JobExecutionException;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.lang.reflect.Type;
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
    public String connect() {
        String url = getUrl(auth);
        RequestBody requestBody = getRequestBody(kylin);
        ServerResponse<String> stringServerResponse = doRequestSync(url, requestBody, HttpMethod.POST);
        assert stringServerResponse != null;
        if (!stringServerResponse.isSuccess()) {
            throw new KylinException(String.format("connect to kylin server error. status:{}", stringServerResponse.getStatus()));
        }
        return stringServerResponse.getData();
    }

    @Override
    public List<Project> getProjects() {
        String url = getUrl(projects);
        ServerResponse<String> stringServerResponse = doRequestSync(url, null, HttpMethod.GET);
        assert stringServerResponse != null;
        if (!stringServerResponse.isSuccess()) {
            throw new KylinException(String.format("get Project error. status:{}", stringServerResponse.getStatus()));
        }
        List<Project> projects = JSONObject.parseArray(stringServerResponse.getData(), Project.class);
        return projects;
    }

    @Override
    public List<Cube> getCubes(String projectName)  {
        String url = getUrl(String.format("%s?projectName=%s&offset=%d&limit=%d", cubes,
                projectName, 0, 200));
        ServerResponse<String> stringServerResponse =  doRequestSync(url, null, HttpMethod.GET);
        assert stringServerResponse != null;
        if (!stringServerResponse.isSuccess()) {
            throw new KylinException(String.format("get cubes error. status:{}", stringServerResponse.getStatus()));
        }
        List<Cube> cubes = JSON.parseArray(stringServerResponse.getData(), Cube.class);
        return cubes;
    }

    @Override
    public Cube getCube(Cube cube) {
        String url = getUrl(String.format("%s/%s", cubes,cube.getName()));
        ServerResponse<String> stringServerResponse = doRequestSync(url, null, HttpMethod.GET);
        assert stringServerResponse != null;
        if (!stringServerResponse.isSuccess()) {
            throw new KylinException(String.format("get cube error. status:{}", stringServerResponse.getStatus()));
        }
        Optional<Cube> first;
        try {
            List<Cube> cubes = JSON.parseArray(stringServerResponse.getData(), Cube.class);
            first = cubes.parallelStream().filter(m -> m.getName().equals(cube.getName())).findFirst();
        } catch (Exception e) {
            Cube resultCube = JSON.parseObject(stringServerResponse.getData(), Cube.class);
            first = Optional.of(resultCube);
        }
        return first.get();
    }

    @Override
    public String buildCube(BuildCubeDto buildCubeDto) {
        String url = getUrl(getSubUrl(buildCubeDto).replace("{cube_name}", buildCubeDto.getCubeName()));
        RequestBody requestBody = getRequestBody(buildCubeDto);
        ServerResponse<String> stringServerResponse = doRequestSync(url, requestBody, HttpMethod.PUT);
        return stringServerResponse.getData();
    }

    private String getSubUrl(BuildCubeDto buildCubeDto) {
        String subUrl = rebuild;
        if ("REFRESH".equals(buildCubeDto.getBuildType()) || ("BUILD".equals(buildCubeDto.getBuildType()) && buildCubeDto.isFullBuild())) {
            subUrl = rebuild;
        }
        if ("BUILD".equals(buildCubeDto.getBuildType()) && !buildCubeDto.isFullBuild()) {
            subUrl = build;
        }
        if ("REFRESH".equals(buildCubeDto.getBuildType()) && buildCubeDto.isStreaming()) {
            subUrl = streamingrebuild;
        }
        if ("REFRESH".equals(buildCubeDto.getBuildType()) && !buildCubeDto.isStreaming()) {
            subUrl = streamingbuild;
        }
        return subUrl;
    }

    @Override
    public KylinJob resumeJob(KylinJob job) {
        String url = getUrl(String.format("%s/%s", resume, job.getUuid()));
        ServerResponse<String> stringServerResponse = doRequestSync(url, null, HttpMethod.PUT);
        assert stringServerResponse != null;
        if (!stringServerResponse.isSuccess()) {
            return null;
        }
        return JSON.parseObject(stringServerResponse.getData(), KylinJob.class);
    }


    @Override
    public KylinJob getJob(String jobUuid) {
        String url = getUrl(String.format("%s/%s", jobs, jobUuid));
        ServerResponse<String> stringServerResponse = doRequestSync(url, null, HttpMethod.GET);
        assert stringServerResponse != null;
        if (!stringServerResponse.isSuccess()) {
            return null;
        }
        return JSON.parseObject(stringServerResponse.getData(), KylinJob.class);
    }

    @Override
    public List<KylinJob> getJobs(String cubeName) {
        String getParams = "cubeName={cube_name}&limit=144&offset=0&timeFilter=1".replace("{cube_name}", cubeName);
        String url = getUrl(String.format("%s?%s", jobs, getParams));
        ServerResponse<String> stringServerResponse = doRequestSync(url, null, HttpMethod.GET);
        assert stringServerResponse != null;
        if (!stringServerResponse.isSuccess()) {
            return null;
        }
        return JSON.parseArray(stringServerResponse.getData(), KylinJob.class);
    }

    private String getUrl(String detail){
        String url = String.format("%s://%s:%d/%s/%s", kylin.getProtocol(),
                kylin.getHost(), kylin.getPort(), baseurl, detail);
        return url;
    }

    private void doRequestAync(String url, RequestBody requestBody, HttpMethod method, Callback callback){
        Request request = getRequest(url, requestBody, method);
        okHttpClient.newCall(request).enqueue(callback);
    }

    private Request getRequest(String url, RequestBody requestBody, HttpMethod method){
        Request.Builder builder = new Request.Builder().url(url)
                .headers(initHeader(kylin));
        Request request = null;
        if (method.equals(HttpMethod.PUT)) {
            request = builder.put(requestBody).build();
        } else if (method.equals(HttpMethod.POST)) {
            request = builder.post(requestBody).build();
        } else if (method.equals(HttpMethod.DELETE)) {
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

    private ServerResponse<String> doRequestSync(String url, RequestBody requestBody, HttpMethod method) {
        Request request = getRequest(url, requestBody, method);
        Response response = null;
        String responseString = null;
        try {
            Call call = okHttpClient.newCall(request);
            response = call.execute();
            responseString = response.body().string();
            if (!response.isSuccessful()) {
                log.info("Kylin request failed. status:{}, result:{}", response.code(), responseString);
                return ServerResponse.createByErrorCodeMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        String.format("Kylin request failed. status:{}", response.code()));
            }
            ServerResponse.createBySuccess(responseString);
        } catch (IOException e) {
            log.error(e.getMessage());
        } finally {
            response.body().close();
        }
        return null;
    }

    private List<Object> doRequestSync(String url, RequestBody requestBody, HttpMethod method, Class result){
        Request request = getRequest(url, requestBody, method);
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
