package com.sergsnmail.chat.client;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class ChatController {

    private Stage pStage;
    private Scene pScene;

    public ChatController(Stage primaryStage) {
        pStage = primaryStage;
        pScene = new Scene(new StackPane());
    }

    public void Login() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("loginForm.fxml"));
        pStage.setScene(new Scene((Parent) loader.load()));
        LoginFormController controller = loader.<LoginFormController>getController();
        controller.init(this);
        pStage.setResizable(false);
    }

    public void authorisation(String username) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("chatForm.fxml"));
        pStage.setScene(new Scene((Parent) loader.load()));
        ChatFormController controller = loader.<ChatFormController>getController();
        controller.init(this);
    }

    public void close() {
        pStage.close();
    }

    public void setTitle(String title){
        pStage.setTitle("Your nickname: " + title);
    }
}
