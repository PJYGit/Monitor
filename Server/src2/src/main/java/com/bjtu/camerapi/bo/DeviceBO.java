package com.bjtu.camerapi.bo;

import com.bjtu.camerapi.entity.Device;
import com.bjtu.camerapi.entity.User;
import com.bjtu.camerapi.response.CommonResponse;
import com.bjtu.camerapi.response.DeviceResponse;
import com.bjtu.camerapi.service.DeviceService;
import com.bjtu.camerapi.service.ServiceGetter;
import com.bjtu.camerapi.service.UserService;
import com.common.spring.Response;

import java.util.List;

public class DeviceBO {

    private static UserService userService;
    private static DeviceService deviceService;

    static {
        userService = ServiceGetter.getBean(UserService.class);
        deviceService = ServiceGetter.getBean(DeviceService.class);
    }

    public static Response statechange(String username, String token, int deviceId, int operation) {
        User user = userService.getUserByUsername(username);
        if (user == null || user.getGroup() != 1) {
            return new CommonResponse.StateResponse(-1);
        }

        boolean login = userService.checkLogin(user, token);
        if (!login) {
            return new CommonResponse.StateResponse(-1);
        }

        userService.refreshToken(user.getUid());
        deviceService.refreshDeviceState();

        Device device = deviceService.getDeviceByDevid(deviceId);
        if (device == null) {
            return new CommonResponse.StateResponse(1);
        }

        if ((device.getState() == 2 && operation == 0) ||
                (device.getState() == 2 && operation == 1) ||
                (device.getState() == 1 && operation == 0) ||
                (device.getState() == 1 && operation == 2)) {
            deviceService.changeDeviceState(deviceId, operation);
            return new CommonResponse.StateResponse(0);
        } else {
            return new CommonResponse.StateResponse(1);
        }

    }

    public static Response devicelist(String username, String token) {
        User user = userService.getUserByUsername(username);
        if (user == null) {
            return new CommonResponse.StateResponse(-1);
        }

        boolean login = userService.checkLogin(user, token);
        if (!login) {
            return new CommonResponse.StateResponse(-1);
        }

        userService.refreshToken(user.getUid());
        deviceService.refreshDeviceState();

        List<Device> list = deviceService.getAllDevice();
        for (Device device : list) {
            if (device.getState() == -1) {
                device.setState(0);
            }
        }

        return new DeviceResponse.devicelist(0, list);
    }
}
