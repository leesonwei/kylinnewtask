package com.delta.kylintask.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(exclude = {"id", "password", "protocol"})
@TableName("tb_kylin")
public class Kylin {
    @TableId
    private Integer id;
    private String protocol;
    private String host;
    private Integer port;
    private String username;
    @JsonIgnore
    private String password;
}
