package org.laeq.video;

import griffon.core.artifact.GriffonView;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;
import org.laeq.VifecoView;
import org.laeq.icon.VideoPointService;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;


@ArtifactProviderFor(GriffonView.class)
public class PlayerView extends AbstractJavaFXGriffonView {
    private final String[] icons = new String[]{
            "icons/truck-mvt-blk.png",
            "icons/truck-mvt-red.png",
            "icons/icon-bicycle-mvt-64.png",
            "icons/icon-car-co2-black-64.png",
            "icons/icon-constr-black-64.png",
            "icons/iconmonstr-car-23-64.png",
            "icons/icon-car-elec-black-64.png",
    };

    private static int REWIND_VALUE = 10;

    @MVCMember @Nonnull
    private PlayerController controller;

    @MVCMember @Nonnull
    private PlayerModel model;

    @MVCMember @Nonnull
    private VifecoView parentView;

    @FXML
    private MediaView mediaView;

    @FXML
    private Pane iconPane;

    @FXML
    private Button playActionTarget;

    @FXML private Button rewindActionTarget;
    @FXML private Button forwardActionTarget;
    @FXML private Button backVideoActionTarget;

    @FXML private Slider videoTimeSlider;

    @FXML private Label durationLabel;

    private Media media;
    private MediaPlayer mediaPlayer;
    private Duration duration;

    @Inject private VideoService videoService;
    @Inject private VideoPointService videoPointService;

    @MVCMember
    public void setController(@Nonnull PlayerController controller) {
        this.controller = controller;
    }

    @MVCMember
    public void setModel(@Nonnull PlayerModel model) {
        this.model = model;
    }

    private ControlsModel controlsModel;

    @Override
    public void initUI() {
       Node node = loadFromFXML();
       VideoListView test = (VideoListView) getApplication().getMvcGroupManager().findGroup("videoList").getView();

       connectActions(node, controller);

       Tab tab = new Tab();
       tab.textProperty().bind(model.videoPathProperty());
       tab.setContent(node);

       test.getTabPane().getTabs().add(tab);

       subInit();

//       setMedia("C:\\Users\\David\\Desktop\\inrs-videa\\ID2_MG_2018-06-19_TRAJET13.mp4");
       setMedia("/home/david/Downloads/small.wav");

    }

    private void subInit(){
        rewindActionTarget.setText("");
        forwardActionTarget.setText("");
        backVideoActionTarget.setText("");
        videoService.setUp(iconPane);

        mediaView.boundsInLocalProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println(newValue);
            iconPane.setPrefWidth(newValue.getWidth());
            iconPane.setPrefHeight(newValue.getHeight());
        });
    }

    private void initPlayer(){
        videoTimeSlider.valueProperty().addListener(observable -> {
            if(videoTimeSlider.isValueChanging()){
                if(mediaPlayer != null){
                    mediaPlayer.seek(duration.multiply(videoTimeSlider.getValue() / 100.0));
                }
            }
        });
    }

    public void debug(){
        for(int i = 0; i < 2000; i ++){
            double x = rand(100, 800);
            double y = rand(100, 600);
            Point2D point = new Point2D(x / iconPane.getBoundsInLocal().getWidth(), y / iconPane.getBoundsInLocal().getHeight());
           try {
               videoService.addVideoIcon(point, Duration.seconds(rand(20, 1000)));
           } catch (FileNotFoundException e) {
               e.printStackTrace();
           }
        }
    }

    public void setMedia(String filePath) {
        playActionTarget.setDisable(true);

        File file = new File(filePath);

        if(file.exists()){
            try {
                videoService.init();

                media = new Media(file.getCanonicalFile().toURI().toString());
                mediaPlayer = new MediaPlayer(media);
                mediaPlayer.setOnReady(() -> {duration = mediaPlayer.getMedia().getDuration();});
                mediaView.setMediaPlayer(mediaPlayer);
                playActionTarget.setDisable(false);

                mediaPlayer.currentTimeProperty().addListener(observable -> {
                    updateValues();
                });

                videoTimeSlider.setOnMouseClicked(event -> {
                    videoTimeSlider.setValueChanging(true);
                    double value = (event.getX()/videoTimeSlider.getWidth()) * videoTimeSlider.getMax();
                    videoTimeSlider.setValue(value);
                    videoTimeSlider.setValueChanging(false);
                });

            } catch (IOException | MediaException e) {
                getLog().error(String.format("MediaException: %s\n", e.toString()));
            } catch (Exception e){
                getLog().error(e.getMessage());
            }

        } else {
            getLog().error("File: %s does not exists\n", filePath);
        }
    }

    public void play() {
        debug();
        if(model.isIsPlaying()){
            mediaPlayer.pause();
        } else{
            mediaPlayer.play();
        }

        model.setIsPlaying(! model.isIsPlaying());
    }

    @FXML
    public void playerPaneScroll(ScrollEvent event){
        if(controlsModel == null){
            controlsModel = (ControlsModel) getApplication().getMvcGroupManager().getAt("controls").getModel();
        }

        if(event.getDeltaY() > 0){
            controlsModel.increaseRate();
            mediaPlayer.setRate(controlsModel.getRate());
        } else if (event.getDeltaY() < 0){
            controlsModel.decreateRate();
            mediaPlayer.setRate(controlsModel.getRate());
        }
    }

    @FXML
    public void playerPaneMouseClicked(MouseEvent mouseEvent) {
        try {
            int rand = (int)(Math.random() * 10) % icons.length;
            Point2D point = new Point2D(mouseEvent.getX() / iconPane.getBoundsInLocal().getWidth(), mouseEvent.getY() / iconPane.getBoundsInLocal().getHeight());

            videoService.addVideoIcon(point, mediaPlayer.getCurrentTime());



        } catch (FileNotFoundException e) {
//            getLog().error(String.format("Icon file not found: %s"));
        }
    }

    public void setVolume() {
        getLog().info("set volume");
    }

    private void updateValues() {
        Platform.runLater(() -> {
            Duration currentTime = mediaPlayer.getCurrentTime();
            videoService.update(currentTime);

            videoTimeSlider.setDisable(duration.isUnknown());

            if (!videoTimeSlider.isDisabled() && duration.greaterThan(Duration.ZERO) && !videoTimeSlider.isValueChanging()) {
                videoTimeSlider.setValue(currentTime.divide(duration).toMillis() * 100.0);
            }

            durationLabel.setText(
                String.format("%s / %s",
                    videoService.formatDuration(mediaPlayer.getCurrentTime()),
                    videoService.formatDuration(mediaPlayer.getTotalDuration())
                )
            );
        });
    }

    private double rand(double min, double max){
        return min + Math.random() * (max - min);
    }

}
