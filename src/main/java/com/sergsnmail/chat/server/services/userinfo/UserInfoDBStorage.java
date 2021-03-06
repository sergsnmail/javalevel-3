package com.sergsnmail.chat.server.services.userinfo;

import com.sergsnmail.chat.server.UserInfo;
import com.sergsnmail.chat.server.services.DbStorage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserInfoDBStorage extends DbStorage implements IUserInfoStorage {

    private final String CREATE_USER_TABLE_SQL = "CREATE TABLE IF NOT EXISTS 'users' ('id' INTEGER PRIMARY KEY AUTOINCREMENT," +
            " 'username' TEXT," +
            " 'password' TEXT," +
            " 'nickname' TEXT," +
            " UNIQUE(username));";
    private final String SELECT_USER_SQL = "SELECT * FROM users WHERE username = ?";
    private final String UPDATE_USER_SQL = "UPDATE users SET nickname = ? WHERE username = ?";
    private final String INSERT_USER_SQL = "INSERT INTO users (username, password, nickname) VALUES (?, ?, ?)";
    private final String DELETE_USER_SQL = "DELETE FROM users WHERE username = ?";

    @Override
    public UserInfo getUserInfo(String userName) {
        if (userName == null || userName.equals("")) {
            throw new IllegalArgumentException();
        }
        UserInfo userInfo = null;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_SQL)) {
            preparedStatement.setString(1, userName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                userInfo = new UserInfo();
                userInfo.setUsername(resultSet.getString("username"));
                userInfo.setNickname(resultSet.getString("nickname"));
                userInfo.setPassword(resultSet.getString("password"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return userInfo;
    }

    @Override
    public boolean updateUserInfo(UserInfo userInfo) {
        if (userInfo == null) {
            throw new IllegalArgumentException();
        }
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_USER_SQL)) {
            preparedStatement.setString(1, userInfo.getNickname());
            preparedStatement.setString(2, userInfo.getUsername());
            preparedStatement.executeUpdate();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean createUserInfo(UserInfo userInfo, String hashedPassword) {
        if (userInfo == null) {
            throw new IllegalArgumentException();
        }
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER_SQL)) {
            preparedStatement.setString(1, userInfo.getUsername());
            preparedStatement.setString(2, hashedPassword);
            preparedStatement.setString(3, userInfo.getNickname());
            preparedStatement.executeUpdate();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteUserInfo(UserInfo userInfo) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_USER_SQL)) {
            preparedStatement.setString(1, userInfo.getUsername());
            preparedStatement.executeUpdate();
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    @Override
    protected void initStorage() {
        try (Connection connection = getConnection()) {
            connection.createStatement().execute(CREATE_USER_TABLE_SQL);
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }
}
