package com.bjtu.camerapi.service;


import com.bjtu.camerapi.entity.Device;
import com.bjtu.camerapi.entity.PictureLine;
import com.bjtu.camerapi.mapper.PiMapper;
import com.bjtu.camerapi.mapper.PicturesMapper;
import com.common.util.NumberUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PiService {

    private final PiMapper piDao;
    private final PicturesMapper picDao;

    @Autowired
    public PiService(PiMapper piDao, PicturesMapper picDao) {
        this.picDao = picDao;
        this.piDao = piDao;
    }

    public Device selectDeviceByID(int deviceID) {
        List<Device> listDevice = piDao.selectDeviceByID(deviceID);
        return (listDevice.size() == 0 ? null : listDevice.get(0));
    }

    public Device selectDeviceByMAC(String mac) {
        List<Device> listDevice = piDao.selectDeviceByMAC(mac);
        return (listDevice.size() == 0 ? null : listDevice.get(0));
    }

    public Device insertDeviceByMAC(String mac) {
        piDao.insertDevice(mac);
        return selectDeviceByMAC(mac);
    }

    public void updateDeviceIP(int devid, String ip) {
        piDao.updateDeviceIP(devid, ip);
    }

    public void updateDeviceState(int devid, int state) {
        piDao.updateDeviceState(devid, state);
    }

    public void updateDeviceLastLogin(int devid, long time) {
        piDao.updateDeviceLastLogin(devid, time);
    }


    public void insertPicture(int id, int type, String s) {
        picDao.insertPicture(s, type, id, NumberUtil.getTimeStrap());
    }

    public void updateDeviceHeartbeat(int devid, long timeStrap) {
        piDao.updateDeviceHeartbeat(devid, timeStrap);
    }

    public PictureLine selectPictureByUrl(String url) {
        return picDao.selectPictureByUrl(url);
    }
}
