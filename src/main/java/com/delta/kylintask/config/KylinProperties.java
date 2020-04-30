package com.delta.kylintask.config;

import lombok.Data;
import org.quartz.Job;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@EnableConfigurationProperties
@ConfigurationProperties("spring.kylin")
@PropertySource("classpath:application.properties")
@Data
public class KylinProperties {
    private Job job;
    private Monitor monitor;

    @Data
    public static class Job {
        private Integer duration;
    }
    @Data
    public static class Monitor {
        private String cron;
    }
}
