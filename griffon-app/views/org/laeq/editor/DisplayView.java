package org.laeq.editor;

import griffon.core.artifact.GriffonView;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;
import org.laeq.model.Icon;
import org.laeq.model.icon.IconSVG;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import javax.annotation.Nonnull;

@ArtifactProviderFor(GriffonView.class)
public class DisplayView extends AbstractJavaFXGriffonView {
    @MVCMember @Nonnull private DisplayController controller;
    @MVCMember @Nonnull private DisplayModel model;
    @MVCMember @Nonnull File file;

    private MediaPlayer mediaPlayer;
    @FXML private Pane playerPane;
    @FXML private MediaView mediaView;
    @FXML private Button volumeActionTarget;

    private Icon volumeOn = new Icon(IconSVG.volumeOn, org.laeq.model.icon.Color.white);
    private Icon volumeOff = new Icon(IconSVG.volumeOff, org.laeq.model.icon.Color.white);

    @Override
    public void initUI() {
        Stage stage = (Stage) getApplication()
            .createApplicationContainer(Collections.<String,Object>emptyMap());
        stage.setTitle(getApplication().getConfiguration().getAsString("application.title"));
        stage.setScene(init());
        stage.sizeToScene();
        stage.setAlwaysOnTop(true);
        stage.setOnCloseRequest(event -> mediaPlayer.stop());

        getApplication().getWindowManager().attach("display", stage);
        getApplication().getWindowManager().show("display");
        initPlayer();
    }

    private void initPlayer(){
        try {
            Media media = new Media(file.getCanonicalFile().toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            mediaView.setMediaPlayer(mediaPlayer);
            volumeActionTarget.setText("");

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
        mediaPlayer.seek(currentTime);
    }
}
