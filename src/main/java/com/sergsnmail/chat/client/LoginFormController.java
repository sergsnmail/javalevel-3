package com.sergsnmail.chat.client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginFormController implements Initializable {

    public TextField username;
    public PasswordField password;
    public Label errorMessage;
    private ChatController controller;
    private Network network;

    public void init(ChatController controller) {
        this.controller = controller;
    }

    public void onCloseButton(ActionEvent actionEvent) {
        controller.close();
    }

    public void keyListener(KeyEvent keyEvent) throws IOException {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            network.writeMessage("/auth " + username.getText() + " " + password.getText());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        network = Network.getInstance();
        Thread server = new Thread(() -> {
            try {
                while (true) {
                    String message = network.readMessage();
                    if (message.equals("/quit")) {
                        network.close();
                        close();
                        break;
                    }
                    if (message.equals("/auth ok")) {
                        Platform.runLater(() -> {
                            try {
                                controller.authorisation("Sergey");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                        break;
                    }
                    if (message.equals("/auth failed")){
                        Platform.runLater(() -> clearInput());
                    }
                    if (message.equals("/reg ok")){
                        network.writeMessage("/auth " + username.getText() + " " + password.getText());
                    }
                }
            } catch (IOException ioException) {
                System.err.println("Server was broken");
                try {
                    network.close();
                    close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        server.setDaemon(true);
        server.start();
    }

    private void close() {
        Platform.runLater(() -> controller.close());
    }

    public void onLoginButton(ActionEvent actionEvent) throws IOException {
        network.writeMessage("/auth " + username.getText() + " " + password.getText());
    }

    private void clearInput(){
        username.clear();
        password.clear();
        errorMessage.setVisible(true);
    }

    public void onRegisterButton(ActionEvent actionEvent) throws IOException {
        network.writeMessage("/reg " + username.getText() + " " + password.getText());
    }

    public void onForgotButton(ActionEvent actionEvent) {
    }
}
