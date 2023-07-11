package com.example.vertex_chat;

import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.scene.image.ImageView;
import javafx.scene.media.MediaView;

import javafx.stage.Stage;
import javax.swing.*;
import java.io.*;

import java.net.Socket;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;


public class HelloController {
    @FXML
    private AnchorPane Window;
    @FXML
    private Button SendMSGBTN;
    @FXML
    private ScrollPane ScrollBarUsers;
    @FXML
    private TextField MessageArea;
    @FXML
    public ScrollPane Chat;
    @FXML
    private Label ip;
    @FXML
    private Label port;
    public Socket socket;
    public ArrayList<ImageView> image = new ArrayList<>();
    public ArrayList<MediaView> media = new ArrayList<>();

    public ArrayList<String> ChatList = new ArrayList<>(); //List with all mensager history
    public ArrayList<String> UserConnectNames = new ArrayList<>();

    public void setIP(String ip){this.ip.setText(ip);}
    public void setPort(String port){this.port.setText(port);}

    //Put all Chat Content in VBOX:
    private VBox ChatContent(){
        VBox box = new VBox();
        box.setPadding(new Insets(0, 0, 10, 0));
        if(Chat.getContent() != null)   box.getChildren().add(Chat.getContent());
        return box;
    }

    private static void Teste(Stage s){
        System.out.println("Hello");
    }

    @FXML
    private void initialize() {
        BackgroundService service = new BackgroundService();
        service.start();

        Platform.runLater(() -> {
            Stage stage = (Stage) Window.getScene().getWindow();
            MessageArea.setPrefWidth(Window.getWidth()*0.45);
            stage.widthProperty().addListener((obs, oldWidth, newWidth) -> {
                MessageArea.setPrefWidth(Window.getWidth()*0.45);
            });
        });
    }

    @FXML
    private void WriteMSG(KeyEvent key) throws IOException {
        if (key.getCode().getCode() == 10){
            VBox box = ChatContent();
            Label label = new Label(MessageArea.getText());
            label.setId("MY-Text");
            label.setFont(new Font(18));
            box.getChildren().add(label);
            Chat.setPannable(true);
            Chat.setContent(box);
            DataOutputStream os = new DataOutputStream(socket.getOutputStream());
            os.writeInt(MessageArea.getText().getBytes(StandardCharsets.UTF_8).length);
            os.write(MessageArea.getText().getBytes(StandardCharsets.UTF_8));
            os.flush();
            MessageArea.setText("");
        }
    }

    @FXML
    public void AddChat(){
        if (ChatList.size() != 0){
            VBox box = ChatContent();
            String UserName = ChatList.get(ChatList.size() - 1).split(":")[0];
            String Mensager = ChatList.get(ChatList.size() - 1).split(":")[1];
            Label label = new Label(Mensager);
            label.setFont(new Font(18));
            label.setId("MSG");
            Label UserNameLaber = new Label(UserName);
            UserNameLaber.setId("UserName");
            box.getChildren().add(UserNameLaber);
            if (!label.getText().equals(" ") && !label.getText().equals(" $file$"))    box.getChildren().add(label);
            if(image.size() != 0){
                box.getChildren().add(image.get(0));
                image.remove(0);
            }
            if(media.size() != 0){
                box.getChildren().add(media.get(0));
                media.remove(0);
            }
            Chat.setPannable(true);
            Chat.setContent(box);
            ChatList.remove(0);
        }
        AddUsers();
    }

    public void AddUsers(){
        if(UserConnectNames.size() != 0){
            VBox box = new VBox();
            int i = 0;
            for (String User : UserConnectNames){
                i++;
                HBox hbox = new HBox();
                Image icon = new Image(System.getProperty("user.dir") + "\\UserIcons\\" + i + ".png");
                ImageView imageView = new ImageView();
                imageView.setImage(icon);
                imageView.setFitWidth(45);
                imageView.setFitHeight(45);
                hbox.getChildren().add(imageView);
                hbox.getChildren().add(new Label(User));
                box.getChildren().add(hbox);
                if (i == 7) i = 0;
            }
            ScrollBarUsers.setContent(box);
            ScrollBarUsers.setPannable(true);
        }
    }

    @FXML
    public void SendFiles() throws IOException {
        JFileChooser fileChooser = new JFileChooser();
        int window = fileChooser.showOpenDialog(null);

        if(window == JFileChooser.APPROVE_OPTION){
            VBox box = ChatContent();
            String path = fileChooser.getSelectedFile().getPath();
            InputStream File1 = new BufferedInputStream(new FileInputStream(path));
            byte[] buff = new byte[1024];
            File1.read(buff);
            if ((new FileType().getType(buff)).equals("mp4")){
                System.out.println("MP4");
                MediaPlayer player = new MediaPlayer(new Media((new File(path)).toURI().toURL().toString()));
                player.setAutoPlay(true);
                MediaView view = new MediaView(player);
                view.setFitWidth(300);
                box.getChildren().add(view);
                Chat.setPannable(true);
                Chat.setContent(box);
            }
            else if (!(new FileType().getType(buff)).equals("")){
                Image FileImage = new Image(path);
                ImageView ImageView = new ImageView();
                ImageView.setImage(FileImage);
                ImageView.setFitWidth(300);
                ImageView.setFitHeight((FileImage.getHeight() * 300)/FileImage.getWidth());

                box.getChildren().add(ImageView);
                Chat.setPannable(true);
                Chat.setContent(box);
            }
            DataOutputStream os = new DataOutputStream(socket.getOutputStream());
            InputStream File = new BufferedInputStream(new FileInputStream(path));
            byte[] filebytes;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int byteread;
            while ((byteread = File.read(buffer)) != -1){
                baos.write(buffer, 0, byteread);
            }
            filebytes = baos.toByteArray();
            os.writeInt(filebytes.length);
            os.write(filebytes);
            os.flush();
        }
    }

    //Implementação do Serviço responsável por escrever no ScrollPanel as mensagens dos outros utilizadores.
    private class BackgroundService extends Service<Void> {
        @Override
        protected Task<Void> createTask() {
            return new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    while (true){
                        Platform.runLater(() -> AddChat());
                        Thread.sleep(200);
                    }
                }
            };
        }
    }

    @FXML
    private void CleanChat(){
        ChatList.clear();
        VBox Clean = new VBox();
        Chat.setContent(Clean);
        Chat.setPannable(true);
    }

}