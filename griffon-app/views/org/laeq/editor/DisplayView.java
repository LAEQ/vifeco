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
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;
import org.laeq.model.Icon;
import org.laeq.model.icon.IconSVG;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.Arrays;
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
        stage.setTitle(getApplication().getMessageSource().getMessage("editor.window.title"));
        stage.getIcons().add( getImage("favicon-32x32.png"));
        stage.setScene(init());
        stage.sizeToScene();
        stage.setAlwaysOnTop(true);
        initPlayer();

        getApplication().getWindowManager().attach("display", stage);
        getApplication().getWindowManager().show("display");

        stage.setOnCloseRequest(event -> {
            mediaPlayer.stop();
            getApplication().getEventRouter().publishEventAsync("mvc.clean", Arrays.asList("display"));
        });
    }


    private void closeAndDestroy(String name){
        destroy(name);
        closeScene(name);
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
                mediaPlayer.rateProperty().bindBidirectional(controls.speed);
                controller.isReady();
                mediaPlayer.play();
                mediaPlayer.pause();
                runOutsideUIAsync(() -> mediaPlayer.seek(currentTime));
            });

            mediaPlayer.setOnError(() -> {
                getApplication().getEventRouter().publishEventOutsideUI("status.error", Arrays.asList("video.metadata.error"));
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
        mediaPlayer.pause();
    }

    public void play() {
        mediaPlayer.play();
    }

    public void seek(Duration currentTime) {
        runOutsideUIAsync(() -> {
            mediaPlayer.seek(currentTime);
        });
    }

    private Image getImage(String path) {
        return new Image(getClass().getClassLoader().getResourceAsStream(path));
    }
}
