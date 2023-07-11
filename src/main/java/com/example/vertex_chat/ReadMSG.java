package com.example.vertex_chat;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;


import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;

public class ReadMSG extends Thread{
    private Socket socket;
    private ArrayList<String> ChatList;
    private ArrayList<ImageView> Imagem;
    private ArrayList<MediaView> Media;
    private ArrayList<String> UserConnectName;

    public ReadMSG(Socket socket, ArrayList<String> ChatList, ArrayList<ImageView> Image, ArrayList<String> UserConnectName, ArrayList<MediaView> Media){
        this.socket = socket;
        this.ChatList = ChatList;
        this.Imagem = Image;
        this.UserConnectName = UserConnectName;
        this.Media = Media;
        this.setDaemon(true);
        start();
    }

    //Create a file, with input bytes array and return de file path:
    private String CreateFile(byte[] input, String FileName) throws IOException {
        OutputStream os = new BufferedOutputStream(new FileOutputStream(FileName));
        os.write(input, 0, input.length);
        os.flush();
        return System.getProperty("user.dir") + "\\" + FileName;
    }

    @FXML
    synchronized public void run(){
        try{
            while (true){
                DataInputStream is = new DataInputStream(socket.getInputStream());
                byte[] input = new byte[is.readInt()];
                is.readFully(input);
                String FileType = new FileType().getType(input);
                if (!FileType.equals("") && !FileType.equals("mp4")){
                    String FileName = System.currentTimeMillis() + "." + (new FileType().getType(input));
                    String path = CreateFile(input, FileName);
                    Image image = new Image(path);
                    ImageView view = new ImageView();
                    view.setImage(image);
                    view.setFitWidth(300);
                    view.setFitHeight((image.getHeight() * 300)/image.getWidth());
                    Imagem.add(view);
                }   else if (FileType.equals("mp4")){
                    String FileName = System.currentTimeMillis() + ".mp4";
                    String path = CreateFile(input, FileName);
                    File file = new File(path);
                    MediaPlayer player = new MediaPlayer(new Media(file.toURI().toURL().toString()));
                    player.setAutoPlay(true);
                    MediaView view = new MediaView(player);
                    view.setFitWidth(450);
                    Media.add(view);
                }   else{
                    String[] msg = new String(input, StandardCharsets.UTF_8).split(",");
                    String author = msg[msg.length - 1].replace("]", "").substring(1);
                    ChatList.add(author + ":" + msg[msg.length - 2]);
                    UserConnectName.clear();
                    for (int i = 0; i < msg.length - 2; i++)    UserConnectName.add(msg[i].replace("[", ""));
                }
            }
        }   catch (IOException e){
            System.out.println(e.getMessage());
            System.out.println("Parrou o ReaDMSG");
        }
    }
}
