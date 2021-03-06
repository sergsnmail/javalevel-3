package com.sergsnmail.chat.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentLinkedDeque;

public class Server {

    private static final Logger LOGGER = LogManager.getLogger(Server.class);
    private static final int DEFAULT_PORT = 8189;

    private ConcurrentLinkedDeque<ClientHandler> clients;

    public Server(int port) {
        clients = new ConcurrentLinkedDeque<>();
        try (ServerSocket server = new ServerSocket(port)) {
            LOGGER.info("Server started on port: " + port);
            while (true) {
                Socket socket = server.accept(); // get connection
                LOGGER.info("Client accepted");
                ClientHandler handler = new ClientHandler(socket, this);
                new Thread(handler).start();
            }
        } catch (Exception e) {
            LOGGER.fatal("Server was broken");
        }
    }

    public void addClient(ClientHandler clientHandler) {
        clients.add(clientHandler);
        LOGGER.info(String.format("Client \'%s\' added to broadcast queue",clientHandler.getSessionId()));
    }

    public void removeClient(ClientHandler clientHandler) {
        clients.remove(clientHandler);
        LOGGER.info(String.format("Client \'%s\' removed from broadcast queue",clientHandler.getSessionId()));
    }

    public void broadCastMessage(String msg) throws IOException {
        for (ClientHandler client : clients) {
            client.sendMessage(msg);
        }
    }

    public void sendMessageTo(String nickName, String message) throws IOException, NoSuchElementException {
        if (!nickName.isEmpty() && !message.isEmpty()) {
            ClientHandler client = getClientHandler(nickName);
            client.sendMessage(message);
        }
    }

    private ClientHandler getClientHandler(String nickName) throws NoSuchElementException {
        return clients.stream().filter(x -> x.getNickName().equals(nickName)).findFirst().get();
    }

    public static void main(String[] args) {
        int port = -1;
        if (args !=  null && args.length == 1) {
            port = Integer.parseInt(args[0]);
        }
        if (port == -1) {
            port = DEFAULT_PORT;
        }
        new Server(port);
    }
}
