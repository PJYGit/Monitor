package com.bjtu.camerapi.mapper;

import com.bjtu.camerapi.entity.Address;
import com.bjtu.camerapi.entity.User;
import com.bjtu.camerapi.entity.UserLine;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface UserMapper {
    @Select("select * from user")
    List<User> selectAll();

    @Select("select * from user where username=#{username}")
    List<User> selectByUsername(String username);

    @Select("select * from user where uid=#{uid}")
    List<User> selectByUid(int uid);

    @Update("update user set password=#{finalPassword}, salt=#{salt} where uid=#{uid}")
    void changePasswordByUid(int uid, String finalPassword, String salt);

    @Select("select * from address where uid=#{uid}")
    List<Address> selectAddress(int uid);

    @Update("replace into address(uid, addr) values(#{uid},#{newPushServer})")
    void updatePushServer(int uid, String newPushServer);

    @Insert("insert into user (username,`password`,salt,`group`,regtime) values(#{newUsername},#{newFinalPassword},#{newSalt},0,#{regTime})")
    void addMember(String newUsername, String newFinalPassword, String newSalt, long regTime);

    @Delete("delete from user where uid=#{uid}")
    void delMember(int uid);

    @Select("select `user`.uid,`user`.username,`user`.regtime,login.logintime as lastlogin from `user` inner join login on `user`.uid = login.uid")
    List<UserLine> selectAllUserLine();

}
