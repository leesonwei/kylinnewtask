package com.delta.kylintask.entity;

import lombok.Data;

import java.util.Date;


@Data
public class Step {
    private String id;
    private String name;
    private Date execStartTime;
    private Date execEndTime;
    private String stepStatus;
}
