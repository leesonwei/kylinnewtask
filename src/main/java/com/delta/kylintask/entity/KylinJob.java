package com.delta.kylintask.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

@Data
@TableName("tb_kylin_job")
public class KylinJob {
    @TableId
    private String uuid;
    private Integer kylinId;
    private boolean isResume;
    private Integer resumeTimes;
    private String status;
    private String taskName;
}
