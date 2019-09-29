package com.bjtu.camerapi.entity;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class Device {
    @NotNull(message = "mac can't be null")
    private String mac;
    @NotNull(message = "devid can't be null")
    private int devid;
    @NotNull(message = "state can't be null")
    private int state;
    @NotNull(message = "ip can't be null")
    private String ip;
    @NotNull(message = "time can't be null")
    private long time;
    @NotNull(message = "lastbeat can't be null")
    private long lastbeat;
}
