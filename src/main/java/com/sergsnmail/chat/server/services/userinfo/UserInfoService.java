package com.sergsnmail.chat.server.services.userinfo;

import com.sergsnmail.chat.server.UserInfo;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UserInfoService {

    private IUserInfoStorage storage;
    private static final String SUPER_SECRET_SALT = "SUPER_SECRET_SALT";
    private static UserInfoService instance;

    public static UserInfoService getInstance(IUserInfoStorage storage) {
        if (instance == null) {
            try {
                instance = new UserInfoService(storage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    public UserInfoService(IUserInfoStorage storage) {
        this.storage = storage;
    }

    public UserInfo getUserInfo(String userName) {
        if (userName == null || userName.equals("")) {
            throw new IllegalArgumentException();
        }
        return storage.getUserInfo(userName);
    }

    public boolean hasUserInfo(String userName, String password) {
        if (userName == null || userName.equals("")) {
            throw new IllegalArgumentException();
        }
        UserInfo userInfo = storage.getUserInfo(userName);
        if (userInfo != null) {
            return storage.getUserInfo(userName).getPassword().equals(getPassword(password));
        }
        return false;
    }

    public boolean updateUserInfo(UserInfo userInfo) {
        if (userInfo == null) {
            throw new IllegalArgumentException();
        }
        return storage.updateUserInfo(userInfo);
    }

    public boolean addUserInfo(UserInfo userInfo, String plainPassword) {
        if (userInfo == null) {
            throw new IllegalArgumentException();
        }
        if (getUserInfo(userInfo.getUsername()) == null) {
            String hashedPassword = getPassword(plainPassword);
            return storage.createUserInfo(userInfo, hashedPassword);
        }
        return false;
    }

    public boolean deleteUserInfo(UserInfo userInfo){
        if (userInfo == null) {
            throw new IllegalArgumentException();
        }
        return storage.deleteUserInfo(userInfo);
    }

    private String getPassword(String plainPassword) {
        String hashedPassword = null;
        String passwordWithSalt = plainPassword + SUPER_SECRET_SALT;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            hashedPassword = bytesToHex(md.digest(passwordWithSalt.getBytes()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hashedPassword;
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
