package org.laeq.statistic;

import griffon.core.artifact.GriffonView;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.transformation.FilteredList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;
import org.laeq.model.Icon;
import org.laeq.model.Point;
import org.laeq.model.Video;
import org.laeq.model.icon.IconPointColorized;
import org.laeq.model.icon.IconSVG;
import org.laeq.model.icon.IconSize;
import org.laeq.model.statistic.MatchedPoint;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;

@ArtifactProviderFor(GriffonView.class)
public class DisplayView extends AbstractJavaFXGriffonView {
    @MVCMember @Nonnull private DisplayModel model;
    @MVCMember @Nonnull private DisplayController controller;
    @MVCMember @Nonnull private MatchedPoint matchedPoint;

    private MediaPlayer mediaPlayer;
    @FXML private Pane playerPane;
    @FXML private MediaView mediaView;
    @FXML private Pane iconPane;
    @FXML private Label totalDuration;
    @FXML private TextField currentDuration;

    public SimpleDoubleProperty width = new SimpleDoubleProperty(1);
    public SimpleDoubleProperty height = new SimpleDoubleProperty(1);

    @Override
    public void initUI() {
        Stage stage = (Stage) getApplication()
            .createApplicationContainer(Collections.<String,Object>emptyMap());
        stage.setTitle(getApplication().getConfiguration().getAsString("application.title"));
        stage.setScene(init());
        stage.sizeToScene();
        stage.setAlwaysOnTop(true);

        getApplication().getWindowManager().attach("statistic_display", stage);
        getApplication().getWindowManager().show("statistic_display");
        initPlayer();

        stage.setOnCloseRequest(event -> {
            getApplication().getWindowManager().detach("statistic_display");
            getApplication().getMvcGroupManager().getGroups().get("statistic_display").destroy();
        });
    }


    private void initPlayer(){
        Video video = matchedPoint.getVideo();

        try {
            File file = new File(video.getPath());
            Media media = new Media(file.getCanonicalFile().toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            mediaView.setMediaPlayer(mediaPlayer);

            mediaView.boundsInLocalProperty().addListener((observable, oldValue, newValue) -> {
                width.set(newValue.getWidth());
                height.set(newValue.getHeight());
                iconPane.setPrefWidth(width.doubleValue());
                iconPane.setPrefHeight(height.doubleValue());
            });

            mediaPlayer.setOnReady(() -> {
                displayPoints(matchedPoint);
                totalDuration.setText(formatDuration(video.getDuration()));
            });

        } catch (Exception e) {
            getApplication().getEventRouter().publishEvent("status.error", Arrays.asList("video.play.error", e.getMessage()));
        }
    }

    public void displayPoints(MatchedPoint mp) {
        iconPane.getChildren().clear();

        Duration start = mp.getStarts().get(0);
        mediaPlayer.seek(start);
        currentDuration.setText(formatDuration(start));

        mp.getPoints().forEach(p -> {
            iconPane.getChildren().add(getIconPoint(p));
        });
    }

    public String formatDuration(Duration duration){
        return DurationFormatUtils.formatDuration((long)duration.toMillis(), "H:m:s");
    }

    private IconPointColorized getIconPoint(Point point){
        IconPointColorized icon = new IconPointColorized(new IconSize(point.getCategory(), 40));
        icon.decorate();
        icon.setLayoutX(point.getX() * width.doubleValue());
        icon.setLayoutY(point.getY() * height.doubleValue());

        return icon;
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

    public void seek(Duration currentTime) {
        mediaPlayer.seek(currentTime);
    }
}
