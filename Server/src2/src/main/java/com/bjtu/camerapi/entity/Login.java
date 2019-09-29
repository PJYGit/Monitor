package com.bjtu.camerapi.entity;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class Login {

    @NotNull(message = "uid can't be null")
    private int uid;

    private String token;

    private long deadtime;

    private long lastlogin;
}
