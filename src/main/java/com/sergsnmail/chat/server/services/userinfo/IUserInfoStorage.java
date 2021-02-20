package com.sergsnmail.chat.server.services.userinfo;

import com.sergsnmail.chat.server.UserInfo;

public interface IUserInfoStorage {
    UserInfo getUserInfo(String userName);
    boolean updateUserInfo(UserInfo userInfo);
    boolean createUserInfo(UserInfo userInfo, String hashedPassword);
    boolean deleteUserInfo(UserInfo userInfo);
}
