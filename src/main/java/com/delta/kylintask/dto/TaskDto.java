package com.delta.kylintask.dto;

import lombok.Data;

import java.util.Date;

@Data
public class TaskDto {
    //即是groupid
    private String kylinid;
    private String targetData;
    private String cron;
    private boolean isResume;
    private Integer resumeTimes;
    private boolean isLimit;
    private Date startAt;
    private Date endAt;
    private String description;

    //给前端使用
    private Date nextFireTime;
    private String status;
    private String taskName;
    private String groupName;
}


