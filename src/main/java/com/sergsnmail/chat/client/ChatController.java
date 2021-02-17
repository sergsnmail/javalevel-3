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

public class ChatController implements Initializable {

    @FXML
    private TextArea output;

    @FXML
    private TextField input;

    private Network network;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        network = Network.getInstance();

        new Thread(() -> {
            try {
                while (true) {
                    String message = network.readMessage();
                    if (message.equals("/quit")) {
                        network.close();
                        break;
                    }
                    Platform.runLater(() -> appendMessage(message));
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
        }).start();

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
}
