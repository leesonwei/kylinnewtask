package com.delta.kylintask.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class TargetDataDto implements Serializable {
    private String cube;
    private String segment;
    private String action;
    private String jobUuid;
}
