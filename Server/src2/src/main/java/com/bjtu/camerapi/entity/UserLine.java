package com.bjtu.camerapi.entity;

import lombok.Data;

@Data
public class UserLine {
    private int uid;
    private String username;
    private long regtime;
    private long lastlogin;
}
