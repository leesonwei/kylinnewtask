package com.delta.kylintask.entity;

import lombok.Data;

import java.util.List;

@Data
public class Project {
    private String uuid;
    private String version;
    private String name;
    private String status;
    private List<String> tables;
    private List<Realization> realizations;
    private List<String> models;
    private String description;
}
