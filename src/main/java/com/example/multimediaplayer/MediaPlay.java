package com.example.multimediaplayer;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;

public class MediaPlay implements Initializable {

    @FXML
    private ImageView imgNext;

    @FXML
    private ImageView imgPause;

    @FXML
    private ImageView imgPlay;

    @FXML
    private ImageView imgPrevious;

    @FXML
    private ImageView imgStop;

    @FXML
    private MediaView mediaView;

    @FXML
    private Slider progressBar;

    @FXML
    private Slider volumeSlider;

    @FXML
    private Label labelCurrentTime;

    private MediaPlayer mediaPlayer;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        String video = Objects.requireNonNull(getClass().getResource("arsenal.mp4")).toExternalForm();
        Media media = new Media(video);
        mediaPlayer = new MediaPlayer(media);
        mediaView.setMediaPlayer(mediaPlayer);

        imgStop.setCursor(Cursor.HAND);
        imgPlay.setCursor(Cursor.HAND);
        imgPause.setCursor(Cursor.HAND);
        imgNext.setCursor(Cursor.HAND);
        imgPrevious.setCursor(Cursor.HAND);
        volumeSlider.setCursor(Cursor.HAND);
        progressBar.setCursor(Cursor.HAND);

        DoubleProperty widthProp = mediaView.fitWidthProperty();
        DoubleProperty heightProp = mediaView.fitHeightProperty();
        widthProp.bind(Bindings.selectDouble(mediaView.sceneProperty(), "width"));
        heightProp.bind(Bindings.selectDouble(mediaView.sceneProperty(), "height"));

        volumeSlider.setValue(mediaPlayer.getVolume()*100);
        volumeSlider.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                mediaPlayer.setVolume(volumeSlider.getValue()/100);

            }
        });

        mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
            @Override
            public void changed(ObservableValue<? extends Duration> observableValue, Duration duration, Duration t1) {
            }
        });

        mediaPlayer.currentTimeProperty().addListener(new ChangeListener<javafx.util.Duration>() {
            @Override
            public void changed(ObservableValue<? extends javafx.util.Duration> observable, javafx.util.Duration oldValue, javafx.util.Duration newValue) {
                progressBar.setValue(newValue.toSeconds());
                }
            }
        );

        progressBar.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mediaPlayer.seek(javafx.util.Duration.seconds(progressBar.getValue()));
            }
        });

        progressBar.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mediaPlayer.seek(javafx.util.Duration.seconds(progressBar.getValue()));
            }
        });

        mediaPlayer.setOnReady(new Runnable() {
            @Override
            public void run() {
                javafx.util.Duration total = media.getDuration();
                progressBar.setMax(total.toSeconds());
                mediaPlayer.stop();
            }
        });

        bindCurrentTimeLabel();

    }

    public void bindCurrentTimeLabel(){
        labelCurrentTime.textProperty().bind(Bindings.createStringBinding(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return getTime(mediaPlayer.getCurrentTime()) + " / ";
            }
        },mediaPlayer.currentTimeProperty()));
    }

    public String getTime(Duration time){
        int hours = (int) time.toHours();
        int minutes = (int) time.toMinutes();
        int seconds = (int) time.toSeconds();

        if (seconds > 59) seconds = seconds % 60;
        if (minutes > 59) minutes = minutes % 60;
        if (hours > 59) hours = hours % 60;

        if(hours > 0 ) return String.format("%d:%02d%02d",
                hours, minutes, seconds);

        else return String.format("%02d:%02d",
                minutes,seconds);
    }

    @FXML
    void pauseVideo(MouseEvent event) {
        mediaPlayer.pause();

    }

    @FXML
    void playNext(MouseEvent event) {
        mediaPlayer.seek(mediaPlayer.getCurrentTime().add(javafx.util.Duration.seconds(-27)));

    }

    @FXML
    void playPrevious(MouseEvent event) {
        mediaPlayer.seek(mediaPlayer.getCurrentTime().add(javafx.util.Duration.seconds(-27)));

    }

    @FXML
    void playVideo(MouseEvent event) {
        mediaPlayer.play();
        mediaPlayer.setRate(1);

    }

    @FXML
    void stopVideo(MouseEvent event) {
        mediaPlayer.stop();

    }


}