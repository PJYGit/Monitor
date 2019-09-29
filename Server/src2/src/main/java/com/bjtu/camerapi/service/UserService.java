package com.bjtu.camerapi.service;

import com.bjtu.camerapi.entity.Address;
import com.bjtu.camerapi.entity.Login;
import com.bjtu.camerapi.entity.UserLine;
import com.bjtu.camerapi.mapper.LoginMapper;
import com.bjtu.camerapi.mapper.PicturesMapper;
import com.bjtu.camerapi.mapper.UserMapper;
import com.bjtu.camerapi.entity.User;
import com.common.util.NumberUtil;
import com.common.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserMapper userDao;
    private final LoginMapper loginDao;
    private final PicturesMapper picturesDao;


    @Autowired
    public UserService(UserMapper userDao, LoginMapper loginDao, PicturesMapper picturesDao) {
        this.userDao = userDao;
        this.loginDao = loginDao;
        this.picturesDao = picturesDao;
    }

    public List<User> getAllUser() {
        return userDao.selectAll();
    }

    public User getUserByUsername(String username) {
        List<User> listUser = userDao.selectByUsername(username);
        return (listUser.size() == 0 ? null : listUser.get(0));
    }

    public User getUserByUid(int uid) {
        List<User> listUser = userDao.selectByUid(uid);
        return (listUser.size() == 0 ? null : listUser.get(0));
    }

    public String generateNewToken(int uid) {
        String token = StringUtil.getRandString();
        loginDao.updateLogin(uid, token, NumberUtil.getTimeStrap() + 3600, NumberUtil.getTimeStrap());
        return token;
    }

    public Login getLoginInfo(int uid, String token) {
        List<Login> list = loginDao.getLoginInfoByUidAndToken(uid, token);
        return (list.size() == 0 ? null : list.get(0));
    }

    public boolean checkLogin(User user, String token) {
        Login login = getLoginInfo(user.getUid(), token);
        return login != null && login.getDeadtime() >= NumberUtil.getTimeStrap();
    }

    public void login(int uid) {
        loginDao.updateLogintime(uid, NumberUtil.getTimeStrap());
    }

    public void refreshToken(int uid) {
        loginDao.updateDeadtime(uid, NumberUtil.getTimeStrap() + 3600);
    }

    public void killToken(int uid) {
        loginDao.updateDeadtime(uid, NumberUtil.getTimeStrap() - 3600);
    }

    public void changePassword(int uid, String newPasswordSaltMD5) {
        String newSalt = StringUtil.getRandString();
        String newFinalPassword = StringUtil.MD5(newPasswordSaltMD5 + newSalt);

        userDao.changePasswordByUid(uid, newFinalPassword, newSalt);
    }

    public void changePushServer(int uid, String newPushServer) {
        userDao.updatePushServer(uid, newPushServer);
    }

    public void addMember(String newUsername, String newPasswordSaltMD5) {
        String newSalt = StringUtil.getRandString();
        String newFinalPassword = StringUtil.MD5(newPasswordSaltMD5 + newSalt);
        long regtime = NumberUtil.getTimeStrap();
        userDao.addMember(newUsername, newFinalPassword, newSalt, regtime);
        User addedUser = getUserByUsername(newUsername);
        loginDao.updateLogin(addedUser.getUid(), "", 0, 0);
    }

    public void delMember(int uid) {
        killToken(uid);
        userDao.delMember(uid);
    }

    public List<UserLine> getAllUserLine() {
        return userDao.selectAllUserLine();
    }

    public int totalPic() {
        return picturesDao.getPictureCounts();
    }

    public Address getAddress(int uid) {
        List<Address> listAddress = userDao.selectAddress(uid);
        return (listAddress.size() == 0 ? null : listAddress.get(0));
    }
}
