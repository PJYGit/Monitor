package com.bjtu.camerapi.controller;

import com.bjtu.camerapi.bo.DeviceBO;
import com.bjtu.camerapi.service.DeviceService;
import com.bjtu.camerapi.service.UserService;
import com.common.spring.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/device")
public class DeviceController {

    private final DeviceService deviceService;
    private final UserService userService;

    @Autowired
    public DeviceController(DeviceService deviceService, UserService userService) {
        this.deviceService = deviceService;
        this.userService = userService;
    }

    @RequestMapping("/statechange")
    public Response changeState(
            @RequestParam(value = "user", defaultValue = "") String username,
            @RequestParam(value = "token", defaultValue = "") String token,
            @RequestParam(value = "devid", defaultValue = "") int deviceId,
            @RequestParam(value = "op", defaultValue = "") int operation
    ) {
        return DeviceBO.statechange(username, token, deviceId, operation);
    }

    @RequestMapping("/devicelist")
    public Response getDeviceList(
            @RequestParam(value = "user", defaultValue = "") String username,
            @RequestParam(value = "token", defaultValue = "") String token
    ) {
        return DeviceBO.devicelist(username, token);
    }
}
