module com.example.vertex_chat {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.media;


    opens com.example.vertex_chat to javafx.fxml;
    exports com.example.vertex_chat;
}