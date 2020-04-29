package com.delta.kylintask.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Optional;

@Data
@EqualsAndHashCode(of = "uuid")
public class Cube {
    private String uuid;
    private String version;
    private String name;
    private String status;
    private List<Segment> segments;
    private String project;
    private boolean isStreaming;
    private String model;
    public boolean isFullBuild(){
        Optional<Segment> first = this.getSegments().parallelStream().findFirst();
        return first.get().getName().contains("FULL_BUILD");
    }
}
