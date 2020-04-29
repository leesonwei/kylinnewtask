package com.delta.kylintask.dto;

import com.delta.kylintask.entity.Cube;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class TaskDto {
    private String cubeName;
    private String segment;
    private String buildType;
    private String cron;
    private boolean isResume;
    private Integer resumeTimes;
    private boolean isLimit;
    private Timestamp startAt;
    private Timestamp endAt;
    private String status;
    private String description;
}
