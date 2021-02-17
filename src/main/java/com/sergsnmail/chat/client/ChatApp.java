package com.sergsnmail.chat.client;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ChatApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Network network = Network.getInstance();
        new ChatController(primaryStage).Login();
        primaryStage.show();
        primaryStage.setOnCloseRequest(request -> {
            try {
                network.writeMessage("/quit");
            } catch (IOException ioException) {
                System.err.println("Server was broken");
            }
        });
    }
}
