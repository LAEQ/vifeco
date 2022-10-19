package org.laeq.editor;

import griffon.core.artifact.GriffonView;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
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
import org.laeq.model.Point;
import org.laeq.model.Video;
import org.laeq.model.icon.IconPointColorized;
import org.laeq.model.icon.IconSVG;
import org.reactfx.Subscription;
import javax.annotation.Nonnull;
import java.io.File;
import java.util.*;

@ArtifactProviderFor(GriffonView.class)
public class PlayerView extends AbstractJavaFXGriffonView {
    @MVCMember @Nonnull private PlayerController controller;
    @MVCMember @Nonnull private PlayerModel model;
    @MVCMember @Nonnull private Video video;

    public Scene scene;

    @FXML public Label title;
    @FXML public AnchorPane timeline;
    @FXML public AnchorPane summary;

    //Video player
    private MediaPlayer mediaPlayer;
    @FXML public Pane playerPane;

    @FXML public AnchorPane playerControls;
    @FXML private MediaView mediaView;
    @FXML public Pane iconPane;
    @FXML private VideoSlider slider;
    @FXML private ElapsedText elapsed;
    @FXML private Label duration;

    @FXML private Button addActionTarget;
    @FXML private Button controlsActionTarget;
    @FXML private Button imageControlsActionTarget;

    @FXML private Button drawActionTarget;

    private ColorAdjust colorAdjust;

    private Boolean wasPlaying = false;
    private Double videoDuration;

    public Subscription currentTimeSubscription;

    private ChangeListener<? super Duration> currentTimeListener = currentTimeListener();
    private Duration display;

    @Override
    public void mvcGroupInit(@Nonnull Map<String, Object> args){
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("video", args.get("video"));

        createMVCGroup("timeline", arguments);
        createMVCGroup("category_sum", arguments);
        createMVCGroup("icon_pane", arguments);
        createMVCGroup("player_controls", arguments);
    }

    @Override
    public void mvcGroupDestroy(){
        mediaPlayer.currentTimeProperty().removeListener(currentTimeListener);
    }

    @Override
    public void initUI() {
        Stage stage = (Stage) getApplication().createApplicationContainer(Collections.<String,Object>emptyMap());
        stage.setTitle(getApplication().getMessageSource().getMessage("editor.window.title"));
        stage.getIcons().add( getImage("favicon-32x32.png"));
        scene = init();
        stage.setScene(scene);
        stage.sizeToScene();
        stage.setAlwaysOnTop(true);

        getApplication().getWindowManager().attach("editor", stage);
        getApplication().getWindowManager().show("editor");

        stage.setOnCloseRequest(event -> {
            runInsideUIAsync(() -> {
                mediaPlayer.stop();
                slider.dispose();
            });

            getApplication().getEventRouter().publishEvent("mvc.clean", Arrays.asList("editor"));
        });

        slider.setEventRouter(getApplication().getEventRouter());
        elapsed.setEventRouter(getApplication().getEventRouter());

        initPlayer();
    }

    private Scene init() {
        final Scene scene = new Scene(new Group());
        scene.setFill(Color.WHITE);
        scene.getStylesheets().add("org/kordamp/bootstrapfx/bootstrapfx.css");

        final Node node = loadFromFXML();

        if (node instanceof Parent) {
            scene.setRoot((Parent) node);
        } else {
            ((Group) scene.getRoot()).getChildren().addAll(node);
        }
        connectActions(node, controller);
        connectMessageSource(node);

        title.setText(video.pathToName());

        return scene;
    }

    private void initPlayer(){
        try {
            final File file = new File(video.getPath());
            final Media media = new Media(file.getCanonicalFile().toURI().toString());

            colorAdjust = new ColorAdjust();
            mediaView.setEffect(colorAdjust);

            videoDuration = video.getDuration().toMillis();
            display = model.controls.display();

            mediaPlayer = new MediaPlayer(media);
            mediaView.setMediaPlayer(mediaPlayer);

            //mediaPlayer Event listeners
            mediaPlayer.setOnReady(() ->{
                model.isReady.set(Boolean.TRUE);
                mediaPlayer.play();
                mediaPlayer.pause();
                getApplication().getEventRouter().publishEventOutsideUI("currentTime.update", Arrays.asList(Duration.ZERO));
            });

            mediaPlayer.setOnHalted(() -> {
                //noop
            });

            duration.setText(video.getDurationFormatted());
            mediaView.boundsInLocalProperty().addListener((observable, oldValue, newValue) -> {
                model.width.set(newValue.getWidth());
                model.height.set(newValue.getHeight());

//                drawingPane.setPrefWidth(model.width.doubleValue());
//                drawingPane.setPrefHeight(model.height.doubleValue());
            });

            //Mouse, Keyboard events
            scene.setOnKeyReleased(keyReleased());

        } catch (Exception e) {
            getApplication().getEventRouter().publishEvent("status.error", Arrays.asList("video.play.error", e.getMessage()));
        }
    }

    public void play(){
        mediaPlayer.play();
        mediaPlayer.currentTimeProperty().addListener(currentTimeListener);
    }

    public void pause(){
        mediaPlayer.currentTimeProperty().removeListener(currentTimeListener);
        mediaPlayer.pause();
    }

    private EventHandler<? super KeyEvent> keyReleased() {
        return (EventHandler<KeyEvent>) event -> {
            System.out.println(event.getCode());
            if(event.getCode() == KeyCode.CONTROL){
                controller.togglePlay();
            } else {
                controller.addPoint(event.getCode(), mediaPlayer.getCurrentTime());
            }
        };
    }

    public Duration getCurrentTime() {
        return mediaPlayer.getCurrentTime();
    }

    private ChangeListener<Duration> currentTimeListener(){
        return (observable, oldValue, newValue) -> {
            getApplication().getEventRouter().publishEventOutsideUI("currentTime.update", Arrays.asList(newValue));
            Platform.runLater(() -> {
                if(! slider.isPressed()){
                    final double now = newValue.toMillis();
                    slider.setValue(now / videoDuration * 100.0);
                    elapsed.setText(DurationFormatUtils.formatDuration((long) now, "HH:mm:ss"));
                }
            });
        };
    }

    private Image getImage(String path) {
        return new Image(getClass().getClassLoader().getResourceAsStream(path));
    }

    public void refreshBrightness(Double brightness){
        Platform.runLater(() -> {
            colorAdjust.setBrightness(brightness);
        });
    }

    public void refreshSaturation(Double brightness){
        Platform.runLater(() -> {
            colorAdjust.setSaturation(brightness);
        });
    }

    public void refreshContrast(Double contrast) {
        Platform.runLater(() -> {
            colorAdjust.setContrast(contrast);
        });
    }

    public void refreshHue(Double hue) {
        Platform.runLater(() -> {
            colorAdjust.setHue(hue);
        });
    }

    public void removePoint(Point point) {
        getApplication().getEventRouter().publishEventOutsideUI("point.removed", Arrays.asList(point));
    }

    public void rewind() {
        Duration now = mediaPlayer.getCurrentTime().subtract(Duration.seconds(30));
        if(now.lessThan(Duration.ZERO)){
            now = Duration.ZERO;
        }

        mediaPlayer.seek(now);
        slider.setValue(now.toMillis() / videoDuration * 100);
    }

    public void forward() {
        Duration now = mediaPlayer.getCurrentTime().add(Duration.seconds(30));
        if(now.greaterThan(video.getDuration())){
           now = video.getDuration();
        }

        mediaPlayer.seek(now);
        slider.setValue(now.toMillis() / videoDuration * 100);
    }

    public void refreshRate(Double rate) {
        mediaPlayer.setRate(rate);
    }

    public void refreshVolume(Double volume) {
        mediaPlayer.setVolume(volume);
    }

    public void sliderPressed() {
        mediaPlayer.currentTimeProperty().removeListener(currentTimeListener);
        mediaPlayer.pause();
    }

    public void sliderReleased(Duration now) {
        mediaPlayer.seek(now);
        Platform.runLater(() -> {
            elapsed.setText(DurationFormatUtils.formatDuration((long) now.toMillis(), "HH:mm:ss"));
            mediaPlayer.currentTimeProperty().addListener(currentTimeListener);

            if(model.isPlaying.getValue()){
                mediaPlayer.play();
            }
        });
    }

    public void sliderCurrentTime(final Duration now) {
        mediaPlayer.seek(now);
        Platform.runLater(() -> {
            elapsed.setText(DurationFormatUtils.formatDuration((long) now.toMillis(), "HH:mm:ss"));
        });
    }

    public void setDuration(Duration display) {
        this.display = display;
    }

    public void seek(Duration object) {
        mediaPlayer.seek(object);
    }
}
