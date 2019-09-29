package com.bjtu.camerapi.bo;

import com.bjtu.camerapi.entity.UserLine;
import com.bjtu.camerapi.response.CommonResponse;
import com.bjtu.camerapi.response.UserResponse;
import com.bjtu.camerapi.service.ServiceGetter;
import com.bjtu.camerapi.service.UserService;
import com.common.spring.Response;
import com.bjtu.camerapi.entity.User;
import com.common.util.StringUtil;

import java.util.List;

public class UserBO {

    private static UserService userService;

    static {
        userService = ServiceGetter.getBean(UserService.class);
    }

    public static Response login(String username, String passwordpsmd5) {
        User user = userService.getUserByUsername(username);
        if (user == null) {
            return new UserResponse.login(-1, 0, "");
        }
        String passwordFinal = StringUtil.MD5(passwordpsmd5 + user.getSalt());

        if (passwordFinal.compareTo(user.getPassword()) == 0) {
            userService.login(user.getUid());
            userService.refreshToken(user.getUid());
            return new UserResponse.login(0, user.getGroup(), userService.generateNewToken(user.getUid()));
        } else {
            return new UserResponse.login(-1, 0, "");
        }

    }

    public static Response logout(String username, String token) {
        User user = userService.getUserByUsername(username);
        if (user == null) {
            return new CommonResponse.StateResponse(-1);
        }

        boolean login = userService.checkLogin(user, token);
        if (!login) {
            return new CommonResponse.StateResponse(-1);
        }

        userService.killToken(user.getUid());

        return new CommonResponse.StateResponse(0);
    }

    public static Response pwchange(String username, String token, String newPasswordSaltMD5) {
        User user = userService.getUserByUsername(username);
        if (user == null) {
            return new CommonResponse.StateResponse(-1);
        }

        boolean login = userService.checkLogin(user, token);
        if (!login) {
            return new CommonResponse.StateResponse(-1);
        }

        userService.refreshToken(user.getUid());
        userService.changePassword(user.getUid(), newPasswordSaltMD5);

        return new CommonResponse.StateResponse(0);
    }

    public static Response pschange(String username, String token, String newPushServer) {
        User user = userService.getUserByUsername(username);
        if (user == null || user.getGroup() != 1) {
            return new CommonResponse.StateResponse(-1);
        }

        boolean login = userService.checkLogin(user, token);
        if (!login) {
            return new CommonResponse.StateResponse(-1);
        }

        userService.refreshToken(user.getUid());
        userService.changePushServer(user.getUid(), newPushServer);

        return new CommonResponse.StateResponse(0);
    }

    public static Response newmember(String username, String token, String newUsername, String newPasswordSaltMD5) {
        User user = userService.getUserByUsername(username);
        if (user == null || user.getGroup() != 1) {
            return new CommonResponse.StateResponse(-1);
        }

        boolean login = userService.checkLogin(user, token);
        if (!login) {
            return new CommonResponse.StateResponse(-1);
        }

        User toAddMember = userService.getUserByUsername(newUsername);
        if (toAddMember != null) {
            return new CommonResponse.StateResponse(1);
        }

        userService.refreshToken(user.getUid());
        userService.addMember(newUsername, newPasswordSaltMD5);

        return new CommonResponse.StateResponse(0);
    }

    public static Response delmember(String username, String token, int delUid) {
        User user = userService.getUserByUsername(username);
        if (user == null || user.getGroup() != 1) {
            return new CommonResponse.StateResponse(-1);
        }

        boolean login = userService.checkLogin(user, token);
        if (!login) {
            return new CommonResponse.StateResponse(-1);
        }

        User toDelMember = userService.getUserByUid(delUid);
        if (toDelMember == null || toDelMember.getGroup() == 1) {
            return new CommonResponse.StateResponse(1);
        }

        userService.refreshToken(user.getUid());
        userService.delMember(delUid);

        return new CommonResponse.StateResponse(0);
    }

    public static Response memberlist(String username, String token) {
        User user = userService.getUserByUsername(username);
        if (user == null || user.getGroup() != 1) {
            return new CommonResponse.StateResponse(-1);
        }

        boolean login = userService.checkLogin(user, token);
        if (!login) {
            return new CommonResponse.StateResponse(-1);
        }

        userService.refreshToken(user.getUid());
        List<UserLine> list = userService.getAllUserLine();

        return new UserResponse.memberlist(0, list);
    }

    public static Response info(String username, String token) {
        User user = userService.getUserByUsername(username);
        if (user == null) {
            return new CommonResponse.StateResponse(-1);
        }

        boolean login = userService.checkLogin(user, token);
        if (!login) {
            return new CommonResponse.StateResponse(-1);
        }

        userService.refreshToken(user.getUid());

        String pushServer = "";
        if (user.getGroup() == 1) {
            pushServer = userService.getAddress(user.getUid()).getAddr();
        }

        return new UserResponse.info(0, user.getUid(), pushServer, userService.totalPic());
    }
}
