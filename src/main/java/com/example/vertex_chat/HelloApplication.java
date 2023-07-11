package com.example.vertex_chat;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 726, 398);
        stage.setTitle("Vertex");
        stage.setScene(scene);
        //stage.initStyle(StageStyle.UNDECORATED);
        String path = System.getProperty("user.dir") + "\\src\\main\\resources\\com\\example\\vertex_chat\\Icons\\hexBlue.png";
        //stage.getIcons().add(new Image(path));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}