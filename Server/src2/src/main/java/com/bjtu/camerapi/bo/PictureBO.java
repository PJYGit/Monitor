package com.bjtu.camerapi.bo;

import com.bjtu.camerapi.entity.Login;
import com.bjtu.camerapi.entity.PictureLine;
import com.bjtu.camerapi.entity.User;
import com.bjtu.camerapi.response.CommonResponse;
import com.bjtu.camerapi.response.PictureResponse;
import com.bjtu.camerapi.service.PicturesService;
import com.bjtu.camerapi.service.ServiceGetter;
import com.bjtu.camerapi.service.UserService;
import com.common.spring.Response;

import java.util.List;

public class PictureBO {

    private static UserService userService;
    private static PicturesService picturesService;

    static {
        userService = ServiceGetter.getBean(UserService.class);
        picturesService = ServiceGetter.getBean(PicturesService.class);
    }

    public static Response queryPictures(String username, String token, long starttime, long endtime, int devid, int type) {
        User user = userService.getUserByUsername(username);
        if (user == null) {
            return new CommonResponse.StateResponse(-1);
        }
        Login login = userService.getLoginInfo(user.getUid(), token);
        if (login == null) {
            return new CommonResponse.StateResponse(-1);
        }

        if (starttime == -1) starttime = -1;
        if (endtime == -1) endtime = 1234567891234567891L;

        int startDeviceID = devid;
        int endDeviceID = devid;
        int startTypeID = type;
        int endTypeID = type;

        if (devid == -1) {
            startDeviceID = 0;
            endDeviceID = 1234567891;
        }

        if (type == -1) {
            startTypeID = 0;
            endTypeID = 1234567891;
        }

        List<PictureLine> rtn = picturesService.query(starttime, endtime, startDeviceID, endDeviceID, startTypeID, endTypeID);
        return new PictureResponse.query(0, rtn);
    }
}
