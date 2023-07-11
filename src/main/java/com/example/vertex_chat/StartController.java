package com.example.vertex_chat;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class StartController {
    @FXML
    private TextField ip;
    @FXML
    private TextField port;
    @FXML
    private TextField username;
    @FXML
    private Button submit;
    @FXML
    private Label Warning;
    @FXML
    private Label IP;
    @FXML
    private Label PORT;
    @FXML
    private Label USERNAME;

    @FXML
    private void ChangeColor(KeyEvent key){
        ip.setStyle("-fx-text-inner-color: #fff;-fx-background-color: #586875");
        port.setStyle("-fx-text-inner-color: #fff;-fx-background-color: #586875");
        username.setStyle("-fx-text-inner-color: #fff;-fx-background-color: #586875");
    }

    @FXML
    private void initialize(){
        Platform.runLater(() -> {
            Stage stage = (Stage) submit.getScene().getWindow();
            String path = System.getProperty("user.dir") + "\\src\\main\\resources\\com\\example\\vertex_chat\\Icons\\hexBlue.png";
            stage.getIcons().add(new Image(path));
            stage.widthProperty().addListener((obs, odlWidth, nerWidth) -> {
                Double SpaceBetWeenLabelTextField = ip.getLayoutX() - IP.getLayoutX() - IP.getWidth();
                Double NewX = (stage.getWidth()/2) - (ip.getWidth()/2);
                ip.setMaxWidth(stage.getWidth() * 0.40);
                port.setMaxWidth(stage.getWidth() * 0.40);
                username.setMaxWidth(stage.getWidth() * 0.40);
                username.setLayoutX(NewX);
                port.setLayoutX(NewX);
                ip.setLayoutX(NewX);
                IP.setLayoutX(NewX - SpaceBetWeenLabelTextField - IP.getWidth());
                PORT.setLayoutX(NewX - SpaceBetWeenLabelTextField - PORT.getWidth());
                USERNAME.setLayoutX(NewX - SpaceBetWeenLabelTextField - USERNAME.getWidth());
                submit.setLayoutX((stage.getWidth()/2) - (submit.getWidth()/2));
                Warning.setLayoutX((stage.getWidth()/2) - (Warning.getWidth()/2));
            });
        });
    }
    @FXML
    public void NextPage() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("chat-view.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle("Vertex");
        stage.setScene(new Scene(root, 1035, 613));
        Stage old = (Stage) submit.getScene().getWindow();
        if (!ip.getText().equals("") && !port.getText().equals("") && !username.getText().equals("")){
            try{
                Socket socket = new Socket(ip.getText(), Integer.parseInt(port.getText()));
                old.close();
                stage.show();
                HelloController app = fxmlLoader.getController();
                app.socket = socket;
                ObjectOutputStream UserNameOS = new ObjectOutputStream(socket.getOutputStream());
                UserNameOS.writeObject(username.getText());
                UserNameOS.flush();
                new ReadMSG(socket, app.ChatList, app.image, app.UserConnectNames, app.media);
                app.setIP(ip.getText());
                app.setPort(port.getText());
            } catch (Exception e){
                Warning.setText("Err to conect Server");
            }
        }   else Warning.setText("Invalid");
    }

}