package com.bjtu.camerapi.controller;

import com.bjtu.camerapi.response.CommonResponse;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class TestController {

    @Autowired
    public TestController() {
    }

    @RequestMapping("/GG")
    public String index() {
        return JSONObject.fromObject(new CommonResponse.NoResponse()).toString();
    }
}
