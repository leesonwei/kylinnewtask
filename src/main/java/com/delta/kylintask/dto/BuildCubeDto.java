package com.delta.kylintask.dto;

import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

@Data
public class BuildCubeDto {
    private String cubeName;
    private String segment;
    private Date startTime;
    private Date endTime;
    private String buildType;
    private boolean isStreaming;
    private boolean isFullBuild;
    private Integer sourceOffsetStart;
    private Integer sourceOffsetEnd;
}
