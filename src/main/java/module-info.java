module com.example.multimediaplayer {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;


    opens com.example.multimediaplayer to javafx.fxml;
    exports com.example.multimediaplayer;
}