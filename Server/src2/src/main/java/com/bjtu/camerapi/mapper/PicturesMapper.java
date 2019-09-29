package com.bjtu.camerapi.mapper;


import com.bjtu.camerapi.entity.PictureLine;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface PicturesMapper {

    @Select("select count(*) from pictures")
    int getPictureCounts();

    @Insert("insert into pictures(pname,type,devid,time) values (#{pname},#{type},#{devid},#{time})")
    void insertPicture(String pname, int type, int devid, long time);

    @Select("select pid as id ,pname as url,type, devid,time  from pictures where " +
            "time >= #{starttime} and " +
            "time <= #{endtime} and " +
            "devid >= #{startDeviceID} and " +
            "devid <= #{endDeviceID} and " +
            "type >= #{startTypeID} and " +
            "type <= #{endTypeID}")
    List<PictureLine> query(long starttime, long endtime, int startDeviceID, int endDeviceID, int startTypeID, int endTypeID);

    @Select("select pid as id ,pname as url,type, devid,time  from pictures where pname = #{url}")
    PictureLine selectPictureByUrl(String url);
}
