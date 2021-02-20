package com.sergsnmail.chat.server.services;

import java.sql.*;

public class DbStorage {

    private final String DEFAULT_DATABASE = "server.db";

    public DbStorage() {
        initStorage();
    }

    protected Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        return DriverManager.getConnection("jdbc:sqlite:" + DEFAULT_DATABASE);
    }

    protected void initStorage() {
    }
}
