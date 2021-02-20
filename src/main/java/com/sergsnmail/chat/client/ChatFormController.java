package com.sergsnmail.chat.client;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class ChatFormController implements Initializable {

    @FXML
    private TextArea output;

    @FXML
    private TextField input;

    private Network network;
    private ChatController controller;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        network = Network.getInstance();

        Thread server = new Thread(() -> {
            try {
                network.writeMessage("/get_nickname");
                network.writeMessage("/history");
                while (true) {
                    String message = network.readMessage();
                    if (message.equals("/quit")) {
                        network.close();
                        break;
                    } else if (message.startsWith("/nickname")) {
                        String[] messageArgs = message.split(" ", 2);
                        Platform.runLater(() -> controller.setTitle(messageArgs[1]));
                    } else {
                        Platform.runLater(() -> appendMessage(message));
                    }
                    System.out.println("[DEBUG] Message from server: " + message);
                }
            } catch (IOException ioException) {
                System.err.println("Server was broken");
                Platform.runLater(() -> appendMessage("Server was broken"));
                try {
                    network.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        server.setDaemon(true);
        server.start();
    }

    private void sendMessageToServer(String message) {
        if (!message.equals("")) {
            try {
                network.writeMessage(input.getText());
            } catch (IOException e) {
                appendMessage("Server was broken");
            }
            input.clear();
        }
    }

    public void appendMessage(String message) {
        if (!message.equals("")) {
            output.appendText(message + "\n");
        }
    }

    public void sendMessage(ActionEvent actionEvent) {
        sendMessageToServer(input.getText());
    }

    public void keyListener(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            sendMessageToServer(input.getText());
        }
    }

    public void init(ChatController chatController) {
        this.controller = chatController;
    }
}
