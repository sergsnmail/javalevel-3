package com.sergsnmail.chat.server;

import com.sergsnmail.chat.server.services.history.FileHistory;
import com.sergsnmail.chat.server.services.history.HistoryService;
import com.sergsnmail.chat.server.services.userinfo.UserInfoDBStorage;
import com.sergsnmail.chat.server.services.userinfo.UserInfoService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.NoSuchElementException;

public class ClientHandler implements Runnable {

    private static final Logger LOGGER = LogManager.getLogger("ClientLogger");
    private final String ANONYMOUS_USER = "Anonymous";
    private final String SESSION_NAME = "session";

    private Socket socket;
    private Server server;
    private DataInputStream in;
    private DataOutputStream out;
    private UserInfo userInfo;

    private UserInfoService userInfoService;
    private HistoryService historyService;

    private String sessionId;
    private static int cnt = 0;

    public ClientHandler(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
        cnt++;
        sessionId = SESSION_NAME + "-" + cnt;
        userInfo = new UserInfo();
        userInfo.setUsername(ANONYMOUS_USER);
    }

    @Override
    public void run() {
        try {
            ThreadContext.put("USERNAME", sessionId);
            LOGGER.info("Client handler start processing");

            out = new DataOutputStream(socket.getOutputStream()); // к клиенту
            in = new DataInputStream(socket.getInputStream()); // от клиента

            /**
             * Создаем экземпляр UserInfoService и передаем ему хранилище, работающее с БД
             */
            userInfoService = UserInfoService.getInstance(new UserInfoDBStorage());

            /*
             * Authenticate
             */
            LOGGER.info(String.format("Client \'%s\' waiting authentication", userInfo.getUsername()));
            String msg;
            boolean isAuthenticated = false;
            while (!isAuthenticated) {
                msg = in.readUTF();
                if (msg.equals("/quit")) {
                    out.writeUTF(msg);
                } else if (msg.startsWith("/auth")) { // Аутентификация пользователя
                    isAuthenticated = authenticateCommand(msg);
                } else if (msg.startsWith("/reg")) { // Регистрация нового пользователя
                    registrationCommand(msg);
                } else {
                    LOGGER.info(String.format("Client \'%s\' was sent wrong authentication message", userInfo.getNickname()));
                }
            }

            /*
             * General work
             */
            while (isAuthenticated) {
                msg = in.readUTF();
                LOGGER.info("Message from client: " + msg);
                if (msg.equals("/quit")) {
                    out.writeUTF(msg);
                } else if (msg.startsWith("/w")) { // send private message
                    String[] msgArgs = parsePrivateMessageString(msg);
                    try {
                        String message = userInfo.getNickname() + ": (private) " + msgArgs[2];
                        server.sendMessageTo(msgArgs[1], message);
                        sendMessage(message);
                    } catch (NoSuchElementException e) {
                        sendMessage(String.format("Server message: User with nickname \'%s\' is not connected", msgArgs[1]));
                    }
                } else if (msg.startsWith("/new_nickname")) {
                    changeNicknameCommand(msg);
                } else if (msg.startsWith("/get_nickname")) {
                    getNicknameCommand();
                } else if (msg.startsWith("/history")) {
                    sendHistory();
                } else { // broadcast message
                    server.broadCastMessage(userInfo.getNickname() + ": " + msg);
                    LOGGER.info("Broadcasting message from client: " + msg);
                }
            }
        } catch (Exception e) {
            LOGGER.fatal("Handled connection was broken");
            server.removeClient(this);
        }
        ThreadContext.clearAll();
    }

    /**
     * Parse private message string.
     *
     * @param message Input message string in format "/w [nick_name] [message]".
     * @return String[0] = /w
     * String[1] = nick_name
     * String[2] = message text
     */
    private String[] parsePrivateMessageString(String message) {
        String[] msgArgs = message.split(" ", 3);
        return msgArgs;
    }

    private void sendHistory() throws IOException {
        List<String> history = historyService.get(100);
        if (history != null) {
            for (String historyMsg : history) {
                sendHistory(historyMsg);
            }
        }
    }

    private boolean authenticateCommand(String message) throws IOException {
        LOGGER.info(String.format("Authentication message from client: %s", message));
        String[] msgArgs = message.split(" ", 3); // msgArgs[1] - login, msgArgs[2] - plain password
        boolean authResult = false;
        if (userInfoService.hasUserInfo(msgArgs[1], msgArgs[2])) {
            LOGGER.info(String.format("Client \'%s\' authentication successful", msgArgs[1]));

            userInfo = userInfoService.getUserInfo(msgArgs[1]);
            LOGGER.info("Load client info successful");

            historyService = new HistoryService(new FileHistory(userInfo.getUsername()));
            LOGGER.info("Load client history successful");

            server.addClient(this);
            out.writeUTF("/auth ok");
            authResult = true;
        } else {
            out.writeUTF("/auth failed");
            LOGGER.info(String.format("Client \'%s\' authentication failed", msgArgs[1]));
        }
        return authResult;
    }

    private void registrationCommand(String message) throws IOException {
        LOGGER.info(String.format("Registration message from client: %s", message));
        String[] msgArgs = message.split(" ", 3); // msgArgs[1] - username, msgArgs[2] - plain password
        userInfo.setUsername(msgArgs[1]);
        userInfo.setNickname(msgArgs[1]);
        if (userInfoService.addUserInfo(userInfo, msgArgs[2])) {
            out.writeUTF("/reg ok");
            LOGGER.info(String.format("Client \'%s\' with nickname \'%s\' registered successful", msgArgs[1], msgArgs[1]));
        }
    }

    private void changeNicknameCommand(String message) throws IOException {
        String[] msgArgs = message.split(" ", 2); // msgArgs[1] contains new nickname
        userInfo.setNickname(msgArgs[1]);
        if (userInfoService.updateUserInfo(userInfo)) {
            getNicknameCommand();
            LOGGER.info(String.format("Client \'%s\' changed nickname to \'%s\'", userInfo.getUsername(), userInfo.getNickname()));
        }
    }

    private void getNicknameCommand() throws IOException {
        out.writeUTF("/nickname" + " " + userInfo.getNickname());
    }

    public void sendHistory(String message) throws IOException {
        out.writeUTF(message);
        out.flush();
    }

    public void sendMessage(String message) throws IOException {
        out.writeUTF(message);
        out.flush();
        historyService.add(message);
    }

    public String getNickName() {
        return userInfo.getNickname();
    }

    public String getSessionId() {
        return sessionId;
    }
}
