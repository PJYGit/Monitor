package com.bjtu.camerapi.mapper;


import com.bjtu.camerapi.entity.Device;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface DeviceMapper {

    @Select("select * from device")
    List<Device> selectAll();

    @Select("select * from device where devid=#{devid}")
    List<Device> selectByDevid(int devid);

    @Update("update device set state = #{state} where devid = #{devid}")
    void updateDeviceState(int devid, int state);
}
