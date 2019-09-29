package com.bjtu.camerapi.service;

import com.bjtu.camerapi.entity.Device;
import com.bjtu.camerapi.mapper.DeviceMapper;
import com.common.util.NumberUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviceService {

    private final DeviceMapper deviceDao;

    @Autowired
    public DeviceService(DeviceMapper deviceDao) {
        this.deviceDao = deviceDao;
    }

    public List<Device> getAllDevice() {
        return deviceDao.selectAll();
    }

    public Device getDeviceByDevid(int devid) {
        List<Device> listDevice = deviceDao.selectByDevid(devid);
        return (listDevice.size() == 0 ? null : listDevice.get(0));
    }

    public void changeDeviceState(int deviceId, int operation) {
        deviceDao.updateDeviceState(deviceId, operation);
    }

    public void refreshDeviceState() {
        List<Device> listDevice = deviceDao.selectAll();
        long nowTime = NumberUtil.getTimeStrap();
        for (Device device : listDevice) {
            if (device.getLastbeat() + 60 < nowTime) {
                deviceDao.updateDeviceState(device.getDevid(), -1);
            }
        }
    }
}
