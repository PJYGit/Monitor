package com.bjtu.camerapi.mapper;

import com.bjtu.camerapi.entity.Device;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface PiMapper {

    @Select("select * from device where mac=#{mac}")
    List<Device> selectDeviceByMAC(String mac);

    @Select("select * from device where devid=#{devid}")
    List<Device> selectDeviceByID(int devid);

    @Insert("insert into device(mac,state,ip,time,lastbeat) values (#{mac},0,'',0,0)")
    void insertDevice(String mac);

    @Update("update device set ip = #{ip} where devid = #{devid}")
    void updateDeviceIP(int devid, String ip);

    @Update("update device set time=#{time} where devid = #{devid}")
    void updateDeviceLastLogin(int devid, long time);

    @Update("update device set state = #{state} where devid = #{devid}")
    void updateDeviceState(int devid, int state);

    @Update("update device set lastbeat = #{timeStrap} where devid = #{devid}")
    void updateDeviceHeartbeat(int devid, long timeStrap);

}
