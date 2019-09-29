package com.bjtu.camerapi.controller;


import com.bjtu.camerapi.bo.PiBO;
import com.bjtu.camerapi.service.PiService;
import com.common.spring.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/pi")
public class PiController {
    private final PiService piService;

    @Autowired
    public PiController(PiService piService) {
        this.piService = piService;
    }

    @RequestMapping("/hi")
    public Response hi(
            @RequestParam(value = "mac", defaultValue = "") String mac,
            HttpServletRequest request) {
        return PiBO.deviceLogin(mac, request.getRemoteAddr());
    }

    @RequestMapping("/beat")
    public Response beat(
            @RequestParam(value = "id", defaultValue = "") int id,
            @RequestParam(value = "time", defaultValue = "") long time) {
        log.info("pibeat with " + id + " " + time);
        return PiBO.deviceHeartbeat(id, time);
    }


    @PostMapping(value = "/upload")
    public Response upload(
            @RequestParam(value = "id", defaultValue = "") int id,
            @RequestParam(value = "type", defaultValue = "") int type,
            @RequestParam(value = "data") MultipartFile[] file) {
        return PiBO.uploadPicture(id, type, file);
    }

}
