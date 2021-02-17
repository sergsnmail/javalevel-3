package com.sergsnmail.chat.server;
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
    private boolean running;

    public String getNickName() {
        return nickName;
    }

    private String nickName;
    private static int cnt = 0;

    public ClientHandler(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
        running = true;
        cnt++;
        nickName = "user" + cnt;
    }

    @Override
    public void run() {
        try {
            out = new DataOutputStream(socket.getOutputStream());
            in = new DataInputStream(socket.getInputStream());
            System.out.println("[DEBUG] client start processing");
            sendMessage("Your nikname: " + nickName);
            while (running) {
                String msg = in.readUTF();
                if (msg.equals("/quit")) {
                    out.writeUTF(msg);
                } else if (msg.startsWith("/w")) {
                    String[] msgArgs = parsePrivate(msg);
                    try {
                        server.sendMessageTo(msgArgs[1], nickName +": (private) " + msgArgs[2]);
                    } catch (NoSuchElementException e) {
                        sendMessage(String.format("User with nickname \'%s\' is not connected",msgArgs[1]));
                    }
                } else{
                    server.broadCastMessage(nickName + ": " + msg);
                }
                System.out.println("[DEBUG] message from client: " + msg);
            }
        } catch (Exception e) {
            System.err.println("Handled connection was broken");
            server.removeClient(this);
        }
    }

    /**
     * Parse private message string.
     * @param message Input message string in format "/w [nick_name] [message]".
     * @return  String[0] = /w
     *          String[1] = nick_name
     *          String[2] = message text
     */
    private String[] parsePrivate(String message){
        String[] msgArgs = message.split(" ", 3);
        return msgArgs;
    }

    public void sendMessage(String message) throws IOException {
        out.writeUTF(message);
        out.flush();
    }
}
