package org.laeq.editor;

import griffon.core.artifact.GriffonView;
import griffon.core.i18n.MessageSource;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableMap;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
import org.laeq.HelperService;
import org.laeq.model.Icon;
import org.laeq.model.Point;
import org.laeq.model.Video;
import org.laeq.model.icon.IconPointColorized;
import org.laeq.model.icon.IconSVG;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.File;
import java.util.*;

@ArtifactProviderFor(GriffonView.class)
public class PlayerView extends AbstractJavaFXGriffonView {
    @MVCMember @Nonnull private PlayerController controller;
    @MVCMember @Nonnull private PlayerModel model;
    @MVCMember @Nonnull private Video video;

    @Inject private HelperService helperService;

    private Scene scene;

    @FXML public Label title;

    @FXML public AnchorPane timeline;
    @FXML public AnchorPane summary;

    //Video player
    private MediaPlayer mediaPlayer;
    @FXML private Pane playerPane;
    @FXML private MediaView mediaView;
    @FXML private IconPane iconPane;
    @FXML private Slider slider;
    @FXML private TextField elapsed;
    @FXML private Label duration;

    @FXML private Button addActionTarget;
    @FXML private Button playActionTarget;
    @FXML private Button stopActionTarget;
    @FXML private Button rewindActionTarget;
    @FXML private Button controlsActionTarget;

    private Boolean wasPlaying = false;
    private Double videoDuration;

    public ObservableMap<String, Duration> markers;

    private MessageSource messageSource;
    private ChangeListener<String> elapListen = elapsedListener();
    private EventHandler<KeyEvent> elapKeyListen = elapsedKeyPressed();
    private ChangeListener<Number> sliderListener = sliderListener();
    private ChangeListener<? super Duration> currentTimeListener = currentTimeListener();

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
            mediaPlayer.stop();
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

        initPlayer();
    }

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

        title.setText(video.pathToName());

        return scene;
    }

    private void initPlayer(){
        try {
            File file = new File(video.getPath());
            Media media = new Media(file.getCanonicalFile().toURI().toString());

            videoDuration = video.getDuration().toMillis();

            markers = media.getMarkers();
            model.collection.points.forEach(point -> {
                markers.put(point.getId().toString(), point.getStart());
            });

            mediaPlayer = new MediaPlayer(media);
            mediaView.setMediaPlayer(mediaPlayer);
            mediaPlayer.setOnReady(() ->{
                model.isReady.set(Boolean.TRUE);
                mediaPlayer.play();
                mediaPlayer.pause();
            });

            mediaPlayer.setOnHalted(() -> {
                System.out.println("On halted");
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

                runInsideUIAsync(() -> {
                    double ratioX = newValue.getWidth() * newValue.getWidth();
                    double ratioY = newValue.getHeight() * newValue.getHeight();
                    iconPane.getChildren().forEach(node -> {
                        node.setLayoutX(node.getLayoutX()*ratioX);
                        node.setLayoutX(node.getLayoutY()*ratioY);
                    });
                });
            });

            mediaPlayer.rateProperty().bind(model.controls.speed);
            iconPane.setOnMouseMoved(mousemove());
            iconPane.setOnMouseExited(mouseexit());
            iconPane.setOnMouseEntered(mouseenter());
            iconPane.setOnMouseClicked(mouseclick());
            scene.setOnKeyReleased(keyReleased());

            slider.addEventFilter(MouseEvent.MOUSE_RELEASED, event -> {
                setPlayingListeners(true);
                if(model.isPlaying.getValue()){
                    mediaPlayer.play();
                    getApplication().getEventRouter().publishEventOutsideUI("player.play");
                }
            });
            slider.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
                mediaPlayer.pause();
                getApplication().getEventRouter().publishEventOutsideUI("player.pause");
                setPlayingListeners(false);
            });
            elapsed.focusedProperty().addListener((observable, oldValue, newValue) -> {
                if(newValue){
                    getApplication().getEventRouter().publishEventOutsideUI("player.pause");
                    removePlayingListeners();
                    mediaPlayer.pause();

                    elapsed.textProperty().addListener(elapListen);
                    elapsed.setOnKeyPressed(elapKeyListen);
                }else{
                    elapsed.textProperty().removeListener(elapListen);
                    elapsed.removeEventFilter(KeyEvent.KEY_PRESSED, elapKeyListen);
                }
            });

        } catch (Exception e) {
            getApplication().getEventRouter().publishEvent("status.error", Arrays.asList("video.play.error", e.getMessage()));
        }
    }

    public void play(){
        mediaPlayer.play();
        setPlayingListeners(true);
    }

    public void pause(){
        setPlayingListeners(false);
        mediaPlayer.pause();
    }

    private void removePlayingListeners(){
        slider.valueProperty().removeListener(sliderListener);
        mediaPlayer.currentTimeProperty().removeListener(currentTimeListener);
    }

    private void setPlayingListeners(boolean playing){
        if(playing){
            slider.valueProperty().removeListener(sliderListener);
            mediaPlayer.currentTimeProperty().addListener(currentTimeListener);
        } else {
            mediaPlayer.currentTimeProperty().removeListener(currentTimeListener);
            slider.valueProperty().addListener(sliderListener);
        }
    }

    // Mouse and keyboard events
    private EventHandler<KeyEvent> elapsedKeyPressed(){
        return event -> {
            if( event.getCode() == KeyCode.ENTER ) {
                String time = elapsed.textProperty().get();

                if(helperService.validTimeString(time)){
                    String[] split = time.split(":");
                    Double hours = Double.parseDouble(split[0]);
                    Double minutes = Double.parseDouble(split[1]);
                    Double seconds = Double.parseDouble(split[2]);

                    Duration seekDuration = Duration.hours(hours).add(Duration.minutes(minutes)).add(Duration.seconds(seconds));
                    elapsed.setFocusTraversable(false);
                    mediaPlayer.seek(seekDuration);
                    controller.updateCurrentTime(seekDuration);
                }
            }
        };
    }
    private ChangeListener<String> elapsedListener(){
        return (observable, oldValue, newValue) -> {
            if(HelperService.validTimeString(newValue) == false){
                getApplication().getEventRouter().publishEvent("status.error", Arrays.asList("duration.pattern.invalid"));
            }
        };
    }
    private EventHandler<? super MouseEvent> mouseclick() {
        return (EventHandler<MouseEvent>) event -> {
            Node node = event.getPickResult().getIntersectedNode();
            Parent parent = node.getParent();
            if(parent instanceof IconPointColorized) {
                controller.deletePoint((IconPointColorized) parent);
            }
        };
    }
    private EventHandler<? super KeyEvent> keyReleased() {
        return (EventHandler<KeyEvent>) event -> {
            runInsideUIAsync(() -> {
                controller.addPoint(event.getCode(), mediaPlayer.getCurrentTime());
            });
        };
    }
    private EventHandler<? super MouseEvent> mouseexit() {
        return (EventHandler<MouseEvent>) event -> { model.enabled = Boolean.FALSE; };
    }
    private EventHandler<? super MouseEvent> mouseenter() {
        return (EventHandler<MouseEvent>) event -> { model.enabled = Boolean.TRUE; };
    }
    private EventHandler<MouseEvent> mousemove(){
        return event -> model.setMousePosition(new Point2D(event.getX(), event.getY()));
    }

    public Duration getCurrentTime() {
        return mediaPlayer.getCurrentTime();
    }

    private ChangeListener<Number> sliderListener(){
        return (observable, oldValue, newValue) -> {
            slider.setValue((Double) newValue);

            Duration now = video.getDuration().multiply((Double) newValue / 100);
            refresh(now);
        };
    }

    private void refresh(Duration now){
        Collection<IconPointColorized> icons = model.setCurrentTime(now);
        Platform.runLater(() -> {
            iconPane.getChildren().clear();
            iconPane.getChildren().addAll(icons);

            elapsed.setText(DurationFormatUtils.formatDuration((long) now.toMillis(), "HH:mm:ss"));
            mediaPlayer.seek(now);
        });
    }

    private ChangeListener<Duration> currentTimeListener(){
        return (observable, oldValue, newValue) -> {
            Collection<IconPointColorized> icons = model.setCurrentTime(newValue);
            double now = newValue.toMillis();
            Platform.runLater(() -> {
                slider.setValue(now / videoDuration * 100.0);
                elapsed.setText(DurationFormatUtils.formatDuration((long) now, "HH:mm:ss"));
                iconPane.getChildren().removeIf(node -> icons.contains(node) == false);
            });
        };
    }

    public void rewind() {
        Duration start = getCurrentTime().subtract(Duration.seconds(30));

        if(start.lessThan(Duration.ZERO)){
            start = Duration.ZERO;
        }

        refresh(start);
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
        iconPane.getChildren().forEach(node -> node.setOpacity(opacity));
    }

    public void refreshSize(Double size) {
        iconPane.getChildren().forEach(node -> {
            node.setScaleX(size / 100);
            node.setScaleY(size / 100);
        });
    }

    public void removePoint(Point point) {
        markers.remove(point.getId().toString());
        Platform.runLater(() ->{
            iconPane.getChildren().remove(point.getIconPoint());
        });
    }

    public void rewind(Duration now) {
        refresh(now);
    }
}
