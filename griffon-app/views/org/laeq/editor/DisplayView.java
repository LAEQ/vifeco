package org.laeq.editor;

import griffon.core.artifact.GriffonView;
import griffon.core.mvc.MVCGroup;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;
import org.laeq.model.Icon;
import org.laeq.model.icon.IconSVG;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.Collections;

@ArtifactProviderFor(GriffonView.class)
public class DisplayView extends AbstractJavaFXGriffonView {
    @MVCMember @Nonnull private DisplayController controller;
    @MVCMember @Nonnull private DisplayModel model;
    @MVCMember @Nonnull File file;
    @MVCMember @Nonnull Duration currentTime;
    @MVCMember @Nonnull Controls controls;

    private MediaPlayer mediaPlayer;
    @FXML private Pane playerPane;
    @FXML private MediaView mediaView;
    @FXML private Button volumeActionTarget;

    public Stage stage;

    private Icon volumeOn = new Icon(IconSVG.volumeOn, org.laeq.model.icon.Color.white);
    private Icon volumeOff = new Icon(IconSVG.volumeOff, org.laeq.model.icon.Color.white);

    @Override
    public void initUI() {
        stage = (Stage) getApplication().createApplicationContainer(Collections.<String,Object>emptyMap());
        stage.setTitle(getApplication().getConfiguration().getAsString("application.title"));
        stage.setScene(init());
        stage.sizeToScene();
        stage.setAlwaysOnTop(true);
        initPlayer();

        getApplication().getWindowManager().attach("display", stage);
        getApplication().getWindowManager().show("display");

        stage.setOnCloseRequest(event -> {
            try {
                mediaPlayer.stop();
                closeAndDestroy("display");
            }catch (Exception e){

            }
        });
    }


    private void closeAndDestroy(String name){
        destroy(name);
        closeScene(name);
        System.out.println("Display view editor closing");
        System.out.printf("Windows: %s\n", getApplication().getWindowManager().getWindowNames());
        System.out.printf("MVC : %s\n", getApplication().getMvcGroupManager().getGroups());
    }

    private void destroy(String name) {
        try{
            MVCGroup group = getApplication().getMvcGroupManager().findGroup(name);
            if(group != null){
                group.destroy();
            }
        }catch (Exception e){

        }
    }
    private void closeScene(String name){
        try{
            Stage window = (Stage) getApplication().getWindowManager().findWindow(name);
            window.close();
        }catch (Exception e){

        }
    }

    private void initPlayer(){
        try {
            Media media = new Media(file.getCanonicalFile().toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            mediaView.setMediaPlayer(mediaPlayer);
            volumeActionTarget.setText("");

            mediaPlayer.setOnReady(() -> {
                runInsideUISync(() -> {
                    mediaPlayer.play();
                    mediaPlayer.seek(currentTime);
                    mediaPlayer.pause();
                    controller.isReady();
                    mediaPlayer.rateProperty().bindBidirectional(controls.speed);
                });
            });

            mediaPlayer.setOnError(() -> {
                System.out.println("on error");
            });

            this.volumeOff();
        } catch (Exception e) {

        }
    }

    public void volumeOff(){
        volumeActionTarget.getStyleClass().clear();
        volumeActionTarget.getStyleClass().addAll("btn", "btn-danger");
        volumeActionTarget.setGraphic(volumeOff);
        mediaPlayer.setVolume(0);
    }

    public void volumeOn(){
        volumeActionTarget.getStyleClass().clear();
        volumeActionTarget.getStyleClass().addAll("btn", "btn-success");
        volumeActionTarget.setGraphic(volumeOn);
        mediaPlayer.setVolume(1);
    }

    // build the UI
    private Scene init() {
        Scene scene = new Scene(new Group());
        scene.setFill(Color.WHITE);
        scene.getStylesheets().add("org/kordamp/bootstrapfx/bootstrapfx.css");

        Node node = loadFromFXML();

        if (node instanceof Parent) {
            scene.setRoot((Parent) node);
        } else {
            ((Group) scene.getRoot()).getChildren().addAll(node);
        }
        connectActions(node, controller);
        connectMessageSource(node);

        return scene;
    }

    public void pause() {
        runInsideUISync(() -> {
            mediaPlayer.pause();
        });

    }

    public void play() {
        runInsideUISync(() -> {
            mediaPlayer.play();
        });
    }

    public void seek(Duration currentTime) {
        runInsideUISync(() -> {
            Duration buffer = mediaPlayer.getBufferProgressTime();
            System.out.println("buffer 2: " + DurationFormatUtils.formatDuration((long) buffer.toMillis(),"HH:mm:ss"));
            mediaPlayer.seek(currentTime);
        });
    }
}
