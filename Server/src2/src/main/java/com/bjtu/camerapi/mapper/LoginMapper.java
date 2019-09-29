package com.bjtu.camerapi.mapper;

import com.bjtu.camerapi.entity.Login;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface LoginMapper {
    @Select("select * from login where uid=#{uid} and token=#{token}")
    List<Login> getLoginInfoByUidAndToken(int uid, String token);

    @Update("replace into login(uid, token, deadtime, logintime) values(#{uid},#{token},#{deadtime},#{logintime})")
    void updateLogin(int uid, String token, long deadtime, long logintime);

    @Update("update login set deadtime=#{deadtime} where uid=#{uid}")
    void updateDeadtime(int uid, long deadtime);

    @Update("update login set logintime=#{logintime} where uid=#{uid}")
    void updateLogintime(int uid, long logintime);
}
