package com.sergsnmail.chat.server;

import com.sergsnmail.chat.server.services.userinfo.UserInfoDBStorage;
import com.sergsnmail.chat.server.services.userinfo.UserInfoService;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.NoSuchElementException;

public class ClientHandler implements Runnable {

    private Socket socket;
    private Server server;
    private DataInputStream in;
    private DataOutputStream out;
    private UserInfo userInfo;

    /**
     * Сервисный класс для работы с UserInfo
     */
    private UserInfoService userInfoService;

    public String getNickName() {
        return userInfo.getNickname();
    }

    private String nickName;
    private static int cnt = 0;

    public ClientHandler(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
        cnt++;
        nickName = "user" + cnt;
    }

    @Override
    public void run() {
        try {
            System.out.println("[DEBUG] client handler start processing");

            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());

            /**
             * Создаем экземпляр UserInfoService и передаем ему хранилище, работающее с БД
             */
            userInfoService = UserInfoService.getInstance(new UserInfoDBStorage());

            /*
             * Authenticate
             */
            System.out.println(String.format("[DEBUG] client %s waiting authentication", nickName));
            String msg;
            boolean isAuthenticated = false;
            while (!isAuthenticated) {
                msg = in.readUTF();
                System.out.println("[DEBUG] Authentication message from client: " + msg);
                if (msg.equals("/quit")) {
                    out.writeUTF(msg);
                } else if (msg.startsWith("/auth")) { // Аутентификация пользователя
                    isAuthenticated = authenticateCommand(msg);
                } else if (msg.startsWith("/reg")) { // Регистрация нового пользователя
                    registrationCommand(msg);
                } else {
                    System.err.println(String.format("[DEBUG] client %s was sent wrong authentication message", userInfo.getNickname()));
                }
            }

            /*
             * General work
             */
            while (isAuthenticated) {
                msg = in.readUTF();
                System.out.println("[DEBUG] message from client: " + msg);
                if (msg.equals("/quit")) {
                    out.writeUTF(msg);
                } else if (msg.startsWith("/w")) { // send private message
                    String[] msgArgs = parsePrivate(msg);
                    try {
                        server.sendMessageTo(msgArgs[1], userInfo.getNickname() + ": (private) " + msgArgs[2]);
                    } catch (NoSuchElementException e) {
                        sendMessage(String.format("User with nickname \'%s\' is not connected", msgArgs[1]));
                    }
                } else if (msg.startsWith("/new_nickname")) {
                    changeNicknameCommand(msg);
                } else if (msg.startsWith("/get_nickname")) {
                    getNicknameCommand();
                } else { // broadcast message
                    server.broadCastMessage(userInfo.getNickname() + ": " + msg);
                    System.out.println("[DEBUG] broadcasting message from client: " + msg);
                }
            }
        } catch (Exception e) {
            System.err.println("Handled connection was broken");
            server.removeClient(this);
        }
    }

    /**
     * Parse private message string.
     *
     * @param message Input message string in format "/w [nick_name] [message]".
     * @return String[0] = /w
     * String[1] = nick_name
     * String[2] = message text
     */
    private String[] parsePrivate(String message) {
        String[] msgArgs = message.split(" ", 3);
        return msgArgs;
    }

    private boolean authenticateCommand(String message) throws IOException {
        String[] msgArgs = message.split(" ", 3); // msgArgs[1] - login, msgArgs[2] - plain password
        boolean authResult = false;
        if (userInfoService.hasUserInfo(msgArgs[1], msgArgs[2])) {
            userInfo = userInfoService.getUserInfo(msgArgs[1]);
            server.addClient(this);
            out.writeUTF("/auth ok");
            System.out.println(String.format("[DEBUG] client %s authentication successful", userInfo.getNickname()));
            authResult = true;
        } else {
            out.writeUTF("/auth failed");
            System.out.println(String.format("[DEBUG] client %s authentication failed", nickName));
        }
        return authResult;
    }

    private void registrationCommand(String message) throws IOException {
        String[] msgArgs = message.split(" ", 3); // msgArgs[1] - username, msgArgs[2] - plain password
        userInfo = new UserInfo();
        userInfo.setUsername(msgArgs[1]);
        userInfo.setNickname(nickName);
        if (userInfoService.addUserInfo(userInfo, msgArgs[2])) {
            out.writeUTF("/reg ok");
            System.out.println(String.format("[DEBUG] client %s registered successful", userInfo.getNickname()));
        }
    }

    private void changeNicknameCommand(String message) throws IOException {
        String[] msgArgs = message.split(" ", 2); // msgArgs[1] contains new nickname
        userInfo.setNickname(msgArgs[1]);
        if (userInfoService.updateUserInfo(userInfo)) {
            getNicknameCommand();
            System.out.println(String.format("[DEBUG] client %s changed nickname", userInfo.getNickname()));
        }
    }

    private void getNicknameCommand() throws IOException {
        out.writeUTF("/nickname" + " " + userInfo.getNickname());
    }

    public void sendMessage(String message) throws IOException {
        out.writeUTF(message);
        out.flush();
    }
}
