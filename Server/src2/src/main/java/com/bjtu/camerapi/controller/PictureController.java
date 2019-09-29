package com.bjtu.camerapi.controller;


import com.bjtu.camerapi.bo.PictureBO;
import com.bjtu.camerapi.service.PicturesService;
import com.bjtu.camerapi.service.UserService;
import com.common.spring.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/picmessage")
public class PictureController {

    private final PicturesService pictureService;
    private final UserService userService;

    @Autowired
    public PictureController(PicturesService pictureService, UserService userService) {
        this.pictureService = pictureService;
        this.userService = userService;
    }

    @RequestMapping("/query")
    public Response query(
            @RequestParam(value = "user", defaultValue = "") String user,
            @RequestParam(value = "token", defaultValue = "") String token,
            @RequestParam(value = "starttime", defaultValue = "") long starttime,
            @RequestParam(value = "endtime", defaultValue = "") long endtime,
            @RequestParam(value = "devid", defaultValue = "") int devid,
            @RequestParam(value = "type", defaultValue = "") int type
    ) {
        return PictureBO.queryPictures(user, token, starttime, endtime, devid, type);
    }


}
