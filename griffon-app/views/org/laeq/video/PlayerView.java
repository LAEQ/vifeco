package org.laeq.video;

import griffon.core.artifact.GriffonView;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;
import org.laeq.VifecoView;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;


@ArtifactProviderFor(GriffonView.class)
public class PlayerView extends AbstractJavaFXGriffonView {
    @MVCMember @Nonnull
    private PlayerController controller;

    @MVCMember @Nonnull
    private PlayerModel model;

    @MVCMember @Nonnull
    private VifecoView parentView;

    @FXML
    private MediaView mediaView;

    @FXML
    private Button playActionTarget;

    private Media media;
    private MediaPlayer mediaPlayer;
    private Duration duration;

    @MVCMember
    public void setController(@Nonnull PlayerController controller) {
        this.controller = controller;
    }

    @MVCMember
    public void setModel(@Nonnull PlayerModel model) {
        this.model = model;
    }

    @Override
    public void initUI() {
       Node node = loadFromFXML();
       VideoListView test = (VideoListView) getApplication().getMvcGroupManager().findGroup("videoList").getView();

       connectActions(node, controller);

       Tab tab = new Tab();
       model.videoPathProperty().bindBidirectional(tab.textProperty());
       tab.setContent(node);

       test.getTabPane().getTabs().add(tab);
    }

    public void setMedia(String filePath) {
        playActionTarget.setDisable(true);

        File file = new File(filePath);

        if(file.exists()){
            try {
                media = new Media(file.getCanonicalFile().toURI().toString());
                mediaPlayer = new MediaPlayer(media);
                mediaView.setMediaPlayer(mediaPlayer);
                playActionTarget.setDisable(false);
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

        model.setIsPlaying(model.isIsPlaying());
    }
}
