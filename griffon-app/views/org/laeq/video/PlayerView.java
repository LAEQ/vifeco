package org.laeq.video;

import griffon.core.artifact.GriffonView;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.input.MouseButton;
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
import org.laeq.icon.IconService;
import org.laeq.model.PointIcon;
import org.laeq.model.VideoPointList;

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

    @FXML private Slider videoTimeSlider;

    @FXML private Label durationLabel;

    @Inject private VideoPointList videoPointList;

    private Media media;
    private MediaPlayer mediaPlayer;
    private Duration duration;

    @Inject private VideoService videoService;
    @Inject private IconService iconService;

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
       model.videoPathProperty().bindBidirectional(tab.textProperty());
       tab.setContent(node);

       test.getTabPane().getTabs().add(tab);

       videoTimeSlider.valueProperty().addListener(observable -> {
           if(videoTimeSlider.isValueChanging()){
               if(mediaPlayer != null){
                   mediaPlayer.seek(duration.multiply(videoTimeSlider.getValue() / 100.0));
               }
           }
       });

    }

    public void setMedia(String filePath) {
        playActionTarget.setDisable(true);

        File file = new File(filePath);

        if(file.exists()){
            try {
                media = new Media(file.getCanonicalFile().toURI().toString());
                mediaPlayer = new MediaPlayer(media);
                mediaPlayer.setOnReady(() -> {duration = mediaPlayer.getMedia().getDuration();});
                mediaView.setMediaPlayer(mediaPlayer);
                playActionTarget.setDisable(false);

                mediaPlayer.currentTimeProperty().addListener(observable -> {
                    updateValues();
                });

                if(controlsModel == null){
                    controlsModel = (ControlsModel) getApplication().getMvcGroupManager().getAt("controls").getModel();
                }

                controlsModel.volumeProperty().bindBidirectional(mediaPlayer.volumeProperty());

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
//        Double seconds = duration.toSeconds();
//        mediaPlayer.getCurrentTime();

        try {
            int rand = (int)(Math.random() * 10) % icons.length;

            System.out.println(rand);

            PointIcon pointIcon = new PointIcon(100, 100,icons[rand]);

            iconService.generateIcon(pointIcon);

            pointIcon.setLayoutX(mouseEvent.getX() - pointIcon.getWidth() / 2);
            pointIcon.setLayoutY(mouseEvent.getY() - pointIcon.getHeight() / 2);
            pointIcon.setOpacity(0.65);
//
            iconPane.getChildren().add(pointIcon);

        } catch (FileNotFoundException e) {
//            getLog().error(String.format("Icon file not found: %s"));
        }

//        if(mouseEvent.getButton() == MouseButton.SECONDARY){
//            Double newPosition = videoService.getPositionSecondsBefore(media.getDuration(), mediaPlayer.getCurrentTime(), REWIND_VALUE);
//            videoTimeSlider.setValue(newPosition);
//        }
    }

    public void setVolume() {
        if(controlsModel == null){
            controlsModel = (ControlsModel) getApplication().getMvcGroupManager().getAt("controls").getModel();
        }

//        Integer volume = controlsModel.getVolume();
//        getLog().info(String.format("Set volume: %d", volume));

        if(mediaPlayer != null){
//            getLog().info(String.format("Set volume: %f", (volume / 10.0)));
//            getLog().info(String.format("Set volume: %d", controlsModel.getVolume()));
//            System.out.println(controlsModel.getVolume());
//            mediaPlayer.setVolume(controlsModel.getVolume() / 10);
        }
    }

    private void updateValues() {
        Platform.runLater(() -> {
            Duration currentTime = mediaPlayer.getCurrentTime();
            durationLabel.setText(
                String.format("%s / %s",
                    videoService.formatDuration(mediaPlayer.getCurrentTime()),
                    videoService.formatDuration(mediaPlayer.getTotalDuration())
                )
            );
        });
    }
}
