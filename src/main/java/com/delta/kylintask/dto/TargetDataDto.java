package com.delta.kylintask.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class TargetDataDto implements Serializable {
    private String cubeName;
    private String segment;
    private String buildType;
    private String jobUuid;

    public String getCubeJobName(){
        return String.format("%s_%s_%s", buildType, cubeName, segment);
    }

    public String getMonitorJobName(){
        return String.format("%s_%s", buildType, jobUuid);
    }
}
