package org.laeq.statistic;

import griffon.core.artifact.GriffonView;
import griffon.core.mvc.MVCGroup;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;
import org.laeq.model.Video;
import org.laeq.model.icon.IconPoint;
import org.laeq.model.icon.IconPointColorized;
import org.laeq.model.statistic.MatchedPoint;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        stage.getIcons().add( getImage("favicon-32x32.png"));
        stage.setScene(init());
        stage.sizeToScene();
        stage.setAlwaysOnTop(true);

        getApplication().getWindowManager().attach("statistic_display", stage);
        getApplication().getWindowManager().show("statistic_display");
        initPlayer();

        stage.setOnCloseRequest(event -> {
            getApplication().getEventRouter().publishEvent("mvc.clean", Arrays.asList("statistic_display"));
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
            getApplication().getWindowManager().detach(name);
        }catch (Exception e){

        }
    }

    private void initPlayer(){
        Video video = matchedPoint.getVideo();

        try {
            File file = new File(video.getPath());
            Media media = new Media(file.getCanonicalFile().toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            mediaView.setMediaPlayer(mediaPlayer);

            mediaView.boundsInLocalProperty().addListener((observable, oldValue, newValue) -> {
                iconPane.setPrefWidth(newValue.getWidth());
                iconPane.setPrefHeight(newValue.getHeight());
                width.set(newValue.getWidth());
                height.set(newValue.getHeight());
                displayPoints();
            });

            mediaPlayer.setOnReady(() -> {
                model.mp = matchedPoint;
                displayPoints();
                totalDuration.setText(formatDuration(video.getDuration()));
            });

            mediaPlayer.setOnError(() -> {
                getApplication().getEventRouter().publishEvent("mvc.clean", Arrays.asList("statistic_display"));
            });

        } catch (Exception e) {
            getApplication().getEventRouter().publishEvent("status.error", Arrays.asList("video.play.error", e.getMessage()));
        }
    }

    public void displayPoints() {
        iconPane.getChildren().clear();
        if(model.mp == null){
            return;
        }

        Duration start = model.mp.getStarts().get(0);
        mediaPlayer.seek(start);
        currentDuration.setText(formatDuration(start));

        model.mp.getPoints().forEach(p -> {
            IconPointColorized icon = p.getIconPoint();
            icon.setScaleX(.4);
            icon.setScaleY(.4);
            icon.setOpacity(.7);
            icon.setLayoutX(p.getX() * width.get());
            icon.setLayoutY(p.getY() * height.get());
            iconPane.getChildren().add(icon);
        });
    }

    public String formatDuration(Duration duration){
        return DurationFormatUtils.formatDuration((long)duration.toMillis(), "H:m:s");
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


    private Image getImage(String path) {
        return new Image(getClass().getClassLoader().getResourceAsStream(path));
    }
}
