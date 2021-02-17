package com.sergsnmail.chat.server;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentLinkedDeque;

public class Server {

    private static final int DEFAULT_PORT = 8189;

    private ConcurrentLinkedDeque<ClientHandler> clients;

    public Server(int port) {
        clients = new ConcurrentLinkedDeque<>();
        try (ServerSocket server = new ServerSocket(port)) {
            System.out.println("[DEBUG] server started on port: " + port);
            while (true) {
                Socket socket = server.accept(); // get connection
                System.out.println("[DEBUG] client accepted");
                ClientHandler handler = new ClientHandler(socket, this);
                addClient(handler);
                new Thread(handler).start();
            }
        } catch (Exception e) {
            System.err.println("Server was broken");
        }
    }

    public void addClient(ClientHandler clientHandler) {
        clients.add(clientHandler);
        System.out.println("[DEBUG] client added to broadcast queue");
    }

    public void removeClient(ClientHandler clientHandler) {
        clients.remove(clientHandler);
        System.out.println("[DEBUG] client removed from broadcast queue");
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
