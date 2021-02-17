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
    //private Network network;

    public ChatController(Stage primaryStage) {
        pStage = primaryStage;
        pScene = new Scene(new StackPane());
        //network = Network.getInstance();
        //pStage.setScene(pScene);
    }

    public void Login() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("loginForm.fxml"));
        //pScene.setRoot((Parent) loader.load());
        pStage.setScene(new Scene((Parent) loader.load()));
        //scene.setRoot((Parent) loader.load());
        LoginFormController controller = loader.<LoginFormController>getController();
        controller.init(this);

        //primaryStage.initStyle(StageStyle.UNDECORATED);
        //Parent root = FXMLLoader.load(getClass().getResource("chatForm.fxml"));
        //Parent root = FXMLLoader.load(getClass().getResource("loginForm.fxml"));
        //primaryStage.setScene(new Scene((Parent) loader.load()));

        //        primaryStage.setTitle("Чат");
        pStage.setResizable(false);
    }

    public void authorisation(String username) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("chatForm.fxml"));
        //pScene.setRoot((Parent) loader.load());
        pStage.setScene(new Scene((Parent) loader.load()));
        //scene.setRoot((Parent) loader.load());
        ChatFormController controller = loader.<ChatFormController>getController();
        controller.init(this);

        //primaryStage.initStyle(StageStyle.TRANSPARENT);
        //Parent root = FXMLLoader.load(getClass().getResource("chatForm.fxml"));
        //Parent root = FXMLLoader.load(getClass().getResource("loginForm.fxml"));
        //primaryStage.setScene(new Scene((Parent) loader.load()));

        //        primaryStage.setTitle("Чат");
        //primaryStage.initStyle(StageStyle.DECORATED);
        //primaryStage.setResizable(false);
    }

    public void close() {
        pStage.close();
    }

    public void setTitle(String title){
        pStage.setTitle(title);
    }
}
