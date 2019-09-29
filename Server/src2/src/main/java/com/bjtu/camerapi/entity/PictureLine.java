package com.bjtu.camerapi.entity;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class PictureLine {
    @NotNull(message = "id can't be null")
    private int id;

    private int type;
    private String url;
    private String devid;
    private long time;
}
