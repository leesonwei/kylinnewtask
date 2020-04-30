package com.delta.kylintask.entity;

import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

@Data
public class Segment {
    private String uuid;
    private String name;
    private String status;
    private Date dateRangeStart;
    private Date dateRangeEnd;
    private Integer sourceOffsetStart;
    private Integer sourceOffsetEnd;

    public void setDateRangeEnd(Date dateRangeEnd){
        if (dateRangeEnd.getTime() > 253381081048000L) {
            this.dateRangeEnd = new Date(253381081048000L);
        }
    }
}
