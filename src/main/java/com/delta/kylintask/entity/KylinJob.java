package com.delta.kylintask.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

@Data
@TableName("tb_kylin_job")
public class KylinJob {
    @TableId
    private String uuid;
    private String kylinId;
    private String name;
    private boolean isResume;
    private Integer resumeTimes;
    private String jobStatus;
    private String taskName;
}
