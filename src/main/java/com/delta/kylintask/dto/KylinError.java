package com.delta.kylintask.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(exclude = "stacktrace")
public class KylinError {
    private Integer code;
    private Object data;
    private String msg;
    private String stacktrace;
    private String exception;
    private String url;
}
