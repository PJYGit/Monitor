package com.bjtu.camerapi.entity;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class User {
    @NotNull(message = "uid can't be null")
    private int uid;

    @NotNull(message = "username can't be null")
    private String username;

    @NotNull(message = "password can't be null")
    private String password;

    @NotNull(message = "salt can't be null")
    private String salt;

    @NotNull(message = "group can't be null")
    private int group;

    @NotNull(message = "regtime can't be null")
    private long regtime;
}
