package com.bjtu.camerapi.entity;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class Address {
    @NotNull(message = "uid can't be null")
    private int uid;

    @NotNull(message = "addr can't be null")
    private String addr;
}
