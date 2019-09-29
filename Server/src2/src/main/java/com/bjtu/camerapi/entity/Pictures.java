package com.bjtu.camerapi.entity;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class Pictures {
    @NotNull(message = "pid can't be null")
    private int pid;
    private String pname;
    private int type;
    private int devid;
    private long time;
}
