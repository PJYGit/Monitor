package com.bjtu.camerapi.controller;

import com.bjtu.camerapi.bo.UserBO;

import com.bjtu.camerapi.service.UserService;
import com.common.spring.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/login")
    public Response login(
            @RequestParam(value = "user", defaultValue = "") String username,
            @RequestParam(value = "pw", defaultValue = "") String passwordSaltMD5
    ) {
        return UserBO.login(username, passwordSaltMD5);
    }

    @RequestMapping("/logout")
    public Response logout(
            @RequestParam(value = "user", defaultValue = "") String username,
            @RequestParam(value = "token", defaultValue = "") String token
    ) {
        return UserBO.logout(username, token);
    }

    @RequestMapping("/pwchange")
    public Response changePassword(
            @RequestParam(value = "user", defaultValue = "") String username,
            @RequestParam(value = "token", defaultValue = "") String token,
            @RequestParam(value = "newpw", defaultValue = "") String newPasswordSaltMD5
    ) {
        return UserBO.pwchange(username, token, newPasswordSaltMD5);
    }

    @RequestMapping("/pschange")
    public Response changePushServer(
            @RequestParam(value = "user", defaultValue = "") String username,
            @RequestParam(value = "token", defaultValue = "") String token,
            @RequestParam(value = "newps", defaultValue = "") String newPushServer
    ) {
        return UserBO.pschange(username, token, newPushServer);
    }

    @RequestMapping("/newmember")
    public Response addMember(
            @RequestParam(value = "user", defaultValue = "") String username,
            @RequestParam(value = "token", defaultValue = "") String token,
            @RequestParam(value = "newuser", defaultValue = "") String newUsername,
            @RequestParam(value = "newpw", defaultValue = "") String newPasswordSaltMD5
    ) {
        return UserBO.newmember(username, token, newUsername, newPasswordSaltMD5);
    }

    @RequestMapping("/delmember")
    public Response delMember(
            @RequestParam(value = "user", defaultValue = "") String username,
            @RequestParam(value = "token", defaultValue = "") String token,
            @RequestParam(value = "duid", defaultValue = "") int delUid
    ) {
        return UserBO.delmember(username, token, delUid);
    }

    @RequestMapping("/memberlist")
    public Response memberList(
            @RequestParam(value = "user", defaultValue = "") String username,
            @RequestParam(value = "token", defaultValue = "") String token
    ) {
        return UserBO.memberlist(username, token);
    }

    @RequestMapping("/info")
    public Response info(
            @RequestParam(value = "user", defaultValue = "") String username,
            @RequestParam(value = "token", defaultValue = "") String token
    ) {
        return UserBO.info(username, token);
    }

}
