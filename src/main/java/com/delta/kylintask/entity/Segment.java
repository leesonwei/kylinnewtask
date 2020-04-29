package com.delta.kylintask.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Segment {
    private String uuid;
    private String name;
    private String status;
    private Timestamp dateRangeStart;
    private Timestamp dateRangeEnd;
    private Integer sourceOffsetStart;
    private Integer sourceOffsetEnd;
}
