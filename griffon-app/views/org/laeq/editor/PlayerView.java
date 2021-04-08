package org.laeq.editor;

import griffon.core.artifact.GriffonView;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableMap;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
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
import org.reactfx.EventStream;
import org.reactfx.EventStreams;
import org.reactfx.Subscription;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.*;

@ArtifactProviderFor(GriffonView.class)
public class PlayerView extends AbstractJavaFXGriffonView {
    @MVCMember @Nonnull private PlayerController controller;
    @MVCMember @Nonnull private PlayerModel model;
    @MVCMember @Nonnull private Video video;

    private Scene scene;

    @FXML public Label title;
    @FXML public AnchorPane timeline;
    @FXML public AnchorPane summary;

    //Video player
    private MediaPlayer mediaPlayer;
    @FXML private Pane playerPane;
    @FXML private MediaView mediaView;
    @FXML private IconPane iconPane;
    @FXML private VideoSlider slider;
    @FXML private ElapsedText elapsed;
    @FXML private Label duration;

    @FXML private Button addActionTarget;
    @FXML private Button playActionTarget;
    @FXML private Button stopActionTarget;
    @FXML private Button rewindActionTarget;
    @FXML private Button forwardActionTarget;
    @FXML private Button controlsActionTarget;

    private Boolean wasPlaying = false;
    private Double videoDuration;

    public ObservableMap<String, Duration> markers;
    public Subscription currentTimeSubscription;

    private ChangeListener<? super Duration> currentTimeListener = currentTimeListener();
    private Duration display;

    private Subscription subscription = () -> {};

    @Override
    public void mvcGroupInit(@Nonnull Map<String, Object> args){
        Map<String, Object> video = new HashMap<>();
        video.put("video", args.get("video"));
        createMVCGroup("timeline", video);
        createMVCGroup("category_sum", video);
    }

    @Override
    public void initUI() {
        Stage stage = (Stage) getApplication().createApplicationContainer(Collections.<String,Object>emptyMap());
        stage.setTitle(getApplication().getMessageSource().getMessage("editor.window.title"));
        stage.getIcons().add( getImage("favicon-32x32.png"));
        scene = init();
        stage.setScene(scene);
        stage.sizeToScene();
        stage.setAlwaysOnTop(false);

        getApplication().getWindowManager().attach("editor", stage);
        getApplication().getWindowManager().show("editor");

        stage.setOnCloseRequest(event -> {
            runInsideUISync(()->{
                mediaPlayer.stop();
                mediaPlayer.dispose();
//                iconPane.dispose();
                subscription.unsubscribe();
                slider.dispose();
            });

            getApplication().getEventRouter().publishEvent("mvc.clean", Arrays.asList("editor"));
        });

        Icon icon = new Icon(IconSVG.video_plus, org.laeq.model.icon.Color.white);
        addActionTarget.setGraphic(icon);
        addActionTarget.setText("");

        icon = new Icon(IconSVG.btnPlay, org.laeq.model.icon.Color.white);
        playActionTarget.setGraphic(icon);
        playActionTarget.setText("");

        icon = new Icon(IconSVG.btnPause, org.laeq.model.icon.Color.white);
        stopActionTarget.setGraphic(icon);
        stopActionTarget.setText("");

        icon = new Icon(IconSVG.controls, org.laeq.model.icon.Color.gray_dark);
        controlsActionTarget.setGraphic(icon);
        controlsActionTarget.setText("");

        icon = new Icon(IconSVG.backward30, org.laeq.model.icon.Color.gray_dark);
        rewindActionTarget.setGraphic(icon);
        rewindActionTarget.setText("");

        icon = new Icon(IconSVG.forward30, org.laeq.model.icon.Color.gray_dark);
        forwardActionTarget.setGraphic(icon);
        forwardActionTarget.setText("");

        iconPane.setEventRouter(getApplication().getEventRouter());
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

            videoDuration = video.getDuration().toMillis();
            display = model.controls.display();

            markers = media.getMarkers();
            model.collection.points.forEach(point -> {
                markers.put(point.getId().toString(), point.getStart());
            });

            mediaPlayer = new MediaPlayer(media);
            mediaView.setMediaPlayer(mediaPlayer);

            //mediaPlayer Event listeners
            mediaPlayer.setOnReady(() ->{
                model.isReady.set(Boolean.TRUE);
                mediaPlayer.play();
                mediaPlayer.pause();
            });
            mediaPlayer.setOnHalted(() -> {
                //noop
            });
            mediaPlayer.setOnMarker(event -> {
                Platform.runLater(() -> {
                    try {
                        iconPane.getChildren().add(model.getIcon(event.getMarker().getKey()));
                    }catch (Exception e){
                        //noop
                    }
                });
            });

            duration.setText(video.getDurationFormatted());
            mediaView.boundsInLocalProperty().addListener((observable, oldValue, newValue) -> {
                model.width.set(newValue.getWidth());
                model.height.set(newValue.getHeight());

                iconPane.setPrefWidth(model.width.doubleValue());
                iconPane.setPrefHeight(model.height.doubleValue());

                Platform.runLater(() -> {
                    final Collection<IconPointColorized> icons = model.setCurrentTime(mediaPlayer.getCurrentTime());
                    iconPane.getChildren().clear();
                    iconPane.getChildren().addAll(icons);
                });
            });

            //Mouse, Keyboard events
            scene.setOnKeyReleased(keyReleased());

            EventStream<MouseEvent> clicks = EventStreams.eventsOf(iconPane, MouseEvent.MOUSE_CLICKED);

            manageSubscription(clicks.subscribe(event -> {
                if(event.isControlDown()){
                    Node node = event.getPickResult().getIntersectedNode();
                    Parent parent = node.getParent();
                    if(parent instanceof IconPointColorized) {
                        controller.deletePoint((IconPointColorized) parent);
                    }
                } else if (event.getButton() == MouseButton.PRIMARY){
                    controller.rewind(5d);
                    getApplication().getEventRouter().publishEvent("player.rewind.5");
                } else if (event.getButton() == MouseButton.SECONDARY){
                    controller.forward(5d);
                    getApplication().getEventRouter().publishEvent("player.forward.5");
                }
            }));

        } catch (Exception e) {
            getApplication().getEventRouter().publishEvent("status.error", Arrays.asList("video.play.error", e.getMessage()));
        }
    }

    void manageSubscription(Subscription other) {
        subscription.and(other);
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
            controller.addPoint(event.getCode(), mediaPlayer.getCurrentTime());
        };
    }

    public Duration getCurrentTime() {
        return mediaPlayer.getCurrentTime();
    }

    private ChangeListener<Duration> currentTimeListener(){
        return (observable, oldValue, newValue) -> {
            Platform.runLater(() -> {
                if(! slider.isPressed()){
                    final double now = newValue.toMillis();
                    Duration before = newValue.subtract(display);
                    slider.setValue(now / videoDuration * 100.0);
                    elapsed.setText(DurationFormatUtils.formatDuration((long) now, "HH:mm:ss"));
                    iconPane.getChildren().removeIf(node -> ((IconPointColorized)node).obsolete(before));
                }
            });
        };
    }

    private Image getImage(String path) {
        return new Image(getClass().getClassLoader().getResourceAsStream(path));
    }

    public void addPoint(Point point) {
        markers.put(point.getId().toString(), point.getStart());

        Platform.runLater(() -> {
            iconPane.getChildren().add(model.getIcon(point.getId().toString()));
        });
    }

    public void refreshOpacity(Double opacity) {
        Platform.runLater(() ->{
            iconPane.getChildren().forEach(node -> node.setOpacity(opacity));
        });
    }

    public void refreshSize(Double size) {
        Platform.runLater(() -> {
            iconPane.getChildren().forEach(node -> {
                node.setScaleX(size / 100);
                node.setScaleY(size / 100);
            });
        });
    }

    public void removePoint(Point point) {
        markers.remove(point.getId().toString());
        Platform.runLater(() -> {
            iconPane.getChildren().remove(point.getIconPoint());
        });
    }

    public void rewind(Duration now) {
        final Collection<IconPointColorized> icons = model.setCurrentTime(now);
        mediaPlayer.seek(now);

        Platform.runLater(() -> {
            iconPane.getChildren().clear();
            iconPane.getChildren().addAll(icons);
            elapsed.setText(DurationFormatUtils.formatDuration((long) now.toMillis(), "HH:mm:ss"));

            if(model.isPlaying.getValue() == false){
                slider.setValue(now.toMillis() / videoDuration * 100);
            }
        });
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
        final Collection<IconPointColorized> icons = model.setCurrentTime(now);
        mediaPlayer.seek(now);
        Platform.runLater(() -> {
            iconPane.getChildren().clear();
            iconPane.getChildren().addAll(icons);
            elapsed.setText(DurationFormatUtils.formatDuration((long) now.toMillis(), "HH:mm:ss"));
            mediaPlayer.currentTimeProperty().addListener(currentTimeListener);

            if(model.isPlaying.getValue()){
                mediaPlayer.play();
            }
        });
    }

    public void sliderCurrentTime(final Duration now) {
        final Collection<IconPointColorized> icons = model.setCurrentTime(now);
        mediaPlayer.seek(now);
        Platform.runLater(() -> {
            iconPane.getChildren().clear();
            iconPane.getChildren().addAll(icons);
            elapsed.setText(DurationFormatUtils.formatDuration((long) now.toMillis(), "HH:mm:ss"));
        });
    }

    public void setDuration(Duration display) {
        this.display = display;
    }
}
