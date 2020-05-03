package com.delta.kylintask.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class UserInfo {
    private String username;
    private String token;
    private String displayName;
    private Timestamp tokenExpired;
}
