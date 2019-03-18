package org.laeq.video.player;

import griffon.core.artifact.GriffonView;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.collections.SetChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;
import org.laeq.VifecoView;
import org.laeq.model.Category;
import org.laeq.model.Icon;
import org.laeq.model.Point;
import org.laeq.model.Video;
import org.laeq.model.icon.Color;
import org.laeq.model.icon.IconPointColorized;
import org.laeq.model.icon.IconSVG;
import org.laeq.video.VideoService;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@ArtifactProviderFor(GriffonView.class)
public class PlayerView extends AbstractJavaFXGriffonView {
    private Point2D mousePosition;
    private Node nodeOver;
    private Media media;
    private MediaPlayer mediaPlayer;
    private Duration duration;

    @MVCMember @Nonnull private PlayerController controller;
    @MVCMember @Nonnull private PlayerModel model;
    @MVCMember @Nonnull private ContainerView parentView;
    @MVCMember @Nonnull private Video video;
    @MVCMember @Nonnull private VideoEditor editor;

    @FXML private Pane playerPane;
    @FXML private MediaView mediaView;
    @FXML private Pane iconPane;
    @FXML private Button playActionTarget;
    @FXML private Button rewindActionTarget;
    @FXML private Button forwardActionTarget;
    @FXML private Button backVideoActionTarget;
    @FXML private Slider timeSlider;
    @FXML private Text durationLabel;
    @Inject private VideoService videoService;

    private VifecoView rootView;

    //Listeners
    private EventHandler<KeyEvent> keyListener;
    private EventHandler<? super MouseEvent> mouseMoveListener;

    private Map<String, Icon> playerIcons;
    private static final String playStr = "play";
    private static final String pauseStr = "pause";
    private static final String backwardStr = "backward";
    private static final String forwardStr = "forward";
    private static final String backStr = "back";

    private ChangeListener<Number> iconWidthPropertyListener;
    private EventHandler<? super MouseEvent> mouseExitListener;
    private EventHandler<? super MouseEvent> mouseEnterListener;
    private ChangeListener<Number> iconHeightPropertyListener;
    private InvalidationListener currentTimeListener;
    private InvalidationListener sliderListener;

    private EventHandler<ScrollEvent> scrollListener;
    private EventHandler<MouseEvent> clickListener;

    @Override
    public void initUI() {
        Node node = loadFromFXML();
        rootView = (VifecoView) getApplication().getMvcGroupManager().getViews().get("vifeco");

        keyListener = event -> { keyValues(event);};
        mouseMoveListener = mouseEvent -> {
            mousePosition = new Point2D(
                    mouseEvent.getX() / iconPane.getBoundsInLocal().getWidth(),
                    mouseEvent.getY() / iconPane.getBoundsInLocal().getHeight()
            );

            nodeOver = (Node)(mouseEvent.getTarget());
        };

        connectActions(node, controller);

        init();
        subInitUI();

        parentView.getPlayerPane().getChildren().add(node);
        editor.getVideoPane().addListener((SetChangeListener<IconPointColorized>) change -> {
            if(change.wasAdded()){
                change.getElementAdded().setOpacity(model.getOpacity());
                change.getElementAdded().setScaleX(model.getSize() / 100);
                change.getElementAdded().setScaleY(model.getSize() / 100);
                try{
                    iconPane.getChildren().add(change.getElementAdded());
                } catch (Exception e){
                    getLog().error(e.getMessage());
                }

            } else if(change.wasRemoved()){
                iconPane.getChildren().remove(change.getElementRemoved());
            }
        });
    }

    private EventHandler<ScrollEvent> scrollListener(){
        return event -> {
            String eventName = event.getDeltaY() > 0 ? "rate.increase" : "rate.decrease";

            controller.updateRate(eventName);
            editor.display(mediaPlayer.getCurrentTime());
            controller.dispatchDuration(mediaPlayer.getCurrentTime());
        };
    }

    @Override
    public void mvcGroupDestroy() {
        runInsideUISync(() -> {
            destroy();
        });
    }

    public void play() {
        if (model.isIsPlaying()) {
            model.setIsPlaying(false);
            mediaPlayer.pause();
        } else {
            model.setIsPlaying(true);
            mediaPlayer.play();
        }
    }

    private EventHandler<MouseEvent> clickListener(){
        return mouseEvent -> {
            if(mouseEvent.getButton().equals(MouseButton.SECONDARY)){
                forward(5);
            } else if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                backward(5);
            }
        };
    }

    private void subInitUI() {
        playerIcons = new HashMap<>();
        playerIcons.put(playStr, generatePlayerIcon(IconSVG.btnPlay, Color.gray_dark));
        playerIcons.put(pauseStr, generatePlayerIcon(IconSVG.btnPause, Color.gray_dark));
        playerIcons.put(backwardStr, generatePlayerIcon(IconSVG.backward30, Color.gray_dark));
        playerIcons.put(forwardStr, generatePlayerIcon(IconSVG.forward30, Color.gray_dark));
        playerIcons.put(backStr, generatePlayerIcon(IconSVG.btnBack, Color.gray_dark));

        playActionTarget.setText("");
        playActionTarget.setGraphic(playerIcons.get(playStr));
        rewindActionTarget.setText("");
        rewindActionTarget.setGraphic(playerIcons.get(backwardStr));
        forwardActionTarget.setText("");
        forwardActionTarget.setGraphic(playerIcons.get(forwardStr));
        backVideoActionTarget.setText("");
        backVideoActionTarget.setGraphic(playerIcons.get(backStr));

        playActionTarget.setLayoutX(10);
        backVideoActionTarget.setLayoutX(60);
        rewindActionTarget.setLayoutX(110);
        forwardActionTarget.setLayoutX(160);

        mediaView.boundsInLocalProperty().addListener((observable, oldValue, newValue) -> {
            iconPane.setPrefWidth(newValue.getWidth());
            iconPane.setPrefHeight(newValue.getHeight());
        });
    }

    private void setUp() {
        File file = new File(video.getPath());

        if (file.exists()) {
            try {
                media = new Media(file.getCanonicalFile().toURI().toString());
            } catch (IOException e) {
                getLog().error(String.format("PlayerView: video does not exits %s", video));
            }

            mediaPlayer = new MediaPlayer(media);
            mediaView.setMediaPlayer(mediaPlayer);
            playActionTarget.setDisable(false);

            mediaPlayer.setOnReady(() -> {
                duration = mediaPlayer.getMedia().getDuration();
                updateValues();

                controller.dispatchDuration(duration);
                getLog().info("Dispatch duration");
            });

        } else {
            getLog().error(String.format("PlayerView: file not exits %s", video));
        }
    }

    private void displayPoints() {
        editor.display(mediaPlayer.getCurrentTime());
    }

    private void init() {
        setUp();

        currentTimeListener = currentTimeListener();
        mediaPlayer.currentTimeProperty().addListener(currentTimeListener);

        sliderListener = sliderListener();
        timeSlider.valueProperty().addListener(sliderListener);

        scrollListener = scrollListener();
        playerPane.setOnScroll(scrollListener);
        clickListener = clickListener();
        playerPane.setOnMouseClicked(clickListener);

        mouseExitListener = mouseExitListener();
        iconPane.setOnMouseExited(mouseExitListener);

        mouseEnterListener = mouseEnterListener();
        iconPane.setOnMouseEntered(mouseEnterListener);

        iconWidthPropertyListener = iconWidthPropertyListener();
        iconPane.widthProperty().addListener(iconWidthPropertyListener);

        iconHeightPropertyListener = iconHeightPropertyListener();
        iconPane.heightProperty().addListener(iconHeightPropertyListener);
    }

    private void destroy(){
        iconPane.removeEventHandler(MouseEvent.MOUSE_EXITED, mouseExitListener);
        mouseExitListener = null;

        iconPane.removeEventHandler(MouseEvent.MOUSE_ENTERED, mouseEnterListener);
        mouseEnterListener = null;

        iconPane.removeEventHandler(MouseEvent.MOUSE_MOVED, mouseMoveListener);
        mouseMoveListener = null;

        iconPane.widthProperty().removeListener(iconWidthPropertyListener);
        iconWidthPropertyListener = null;

        iconPane.heightProperty().removeListener(iconHeightPropertyListener);
        iconHeightPropertyListener = null;

        mediaPlayer.currentTimeProperty().removeListener(currentTimeListener);
        currentTimeListener = null;

        timeSlider.valueProperty().removeListener(sliderListener);
        sliderListener = null;

        rootView.getScene().removeEventHandler(KeyEvent.KEY_PRESSED, keyListener);
        keyListener = null;

        iconPane.getChildren().clear();

        mediaPlayer.dispose();
        mediaPlayer = null;

        playerPane.removeEventHandler(ScrollEvent.SCROLL, scrollListener);
        scrollListener = null;

        playerPane.removeEventHandler(MouseEvent.MOUSE_CLICKED, clickListener);
        clickListener = null;
    }

    private EventHandler<? super MouseEvent> mouseEnterListener() {
        return event -> {
            iconPane.setOnMouseMoved(mouseMoveListener);
            rootView.getScene().setOnKeyReleased(keyListener);
        };
    }
    private EventHandler<MouseEvent> mouseExitListener(){
        return event -> {
            mousePosition = null;
            nodeOver = null;
            iconPane.removeEventHandler(MouseEvent.MOUSE_MOVED, mouseMoveListener);
            rootView.getScene().removeEventHandler(KeyEvent.KEY_RELEASED, keyListener);
        };
    }
    private InvalidationListener currentTimeListener(){
        return (observable -> {
            updateValues();
        });
    }
    private InvalidationListener sliderListener(){
        return  observable -> {
            if(timeSlider.isPressed() && mediaPlayer != null){
                displayPoints();
                Duration t = duration.multiply(timeSlider.getValue() / 100);
                controller.update(t);
                mediaPlayer.seek(t);
            }
        };
    }
    private ChangeListener<Number> iconWidthPropertyListener(){
        return (observable, oldValue, newValue) -> {
            editor.setPaneWidth(newValue.doubleValue());
        };
    }
    private ChangeListener<Number> iconHeightPropertyListener(){
        return (observable, oldValue, newValue) -> {
            editor.setPaneHeight(newValue.doubleValue());
        };
    }

    private void keyValues(KeyEvent event) {
        Optional<Category> category = model.getCategory(event.getCode().getName());

        if(event.getCode().equals(KeyCode.SPACE)){
            controller.play();
            return;
        }

        if((event.getCode().equals(KeyCode.ESCAPE) || event.getCode().equals(KeyCode.DELETE)) && nodeOver !=  null && nodeOver.getParent() instanceof IconPointColorized){
            Point point = editor.deleteVideoIcon((IconPointColorized)(nodeOver.getParent()));

            if(point != null){
                controller.deletePoint(point);
            }

            return;
        }

        if(mediaPlayer != null && category.isPresent() && mousePosition != null){
            Point newPoint = new Point();
            newPoint.setX(mousePosition.getX());
            newPoint.setY(mousePosition.getY());
            newPoint.setCategory(category.get());
            newPoint.setVideo(video);
            newPoint.setStart(mediaPlayer.getCurrentTime());

            if(!editor.addPoint(newPoint)){
                //@todo display message error
            } else {
                controller.addPoint(newPoint);
            }
        }
    }
    private void updateValues() {
        Platform.runLater(() -> {
            displayPoints();

            timeSlider.setDisable(duration.isUnknown());

            if (!timeSlider.isDisabled() && duration.greaterThanOrEqualTo(Duration.ZERO) && !timeSlider.isValueChanging()) {
                timeSlider.setValue(mediaPlayer.getCurrentTime().divide(duration).toMillis() * 100.0);
            }

            durationLabel.setText(videoService.getDurationText(mediaPlayer.getCurrentTime(), duration));
        });
    }
    private Icon generatePlayerIcon(String path, String color){
        return new Icon(path, color);
    }

    public void forward(int seconds) {
        Duration nowPlus30 = mediaPlayer.getCurrentTime().add(Duration.millis(seconds * 1000));
        Duration now = nowPlus30.lessThan(duration)? nowPlus30 : duration;

        controller.update(now);
        mediaPlayer.seek(now);
        editor.display(now);
        timeSlider.setValue(mediaPlayer.getCurrentTime().divide(duration).toMillis() * 100.0);
    }
    public void backward(int seconds) {
        Duration nowMinus = mediaPlayer.getCurrentTime().subtract(Duration.millis(seconds * 1000));
        Duration now = nowMinus.greaterThan(mediaPlayer.getStartTime())? nowMinus : mediaPlayer.getStartTime();

        controller.update(now);
        mediaPlayer.seek(now);
        editor.display(now);
        timeSlider.setValue(mediaPlayer.getCurrentTime().divide(duration).toMillis() * 100.0);
    }
    public void rate(Double newValue) {
        if(mediaPlayer != null){
            mediaPlayer.setRate(newValue);
            controller.update(mediaPlayer.getCurrentTime());
//           final Timeline rateTimeline = new Timeline(new KeyFrame(Duration.millis(200), new KeyValue(mediaPlayer.rateProperty(), newValue)));
//           rateTimeline.setCycleCount(1);
//           rateTimeline.play();
        }
    }
    public void size(Double size){
        runInsideUISync(() -> {
            iconPane.getChildren().forEach(n -> {
                ScaleTransition transition = new ScaleTransition(Duration.millis(100), n);
                transition.setInterpolator(Interpolator.LINEAR);
                transition.setToX(size / 100);
                transition.setToY(size / 100);
                transition.setCycleCount(1);
                transition.play();
            });
        });
    }
    public void opacity(Double oldvalue, Double newValue) {
        runInsideUISync(() -> {
            iconPane.getChildren().forEach(n -> {

                FadeTransition transition = new FadeTransition(Duration.millis(100), n);
                transition.setInterpolator(Interpolator.LINEAR);
                transition.setFromValue(oldvalue);
                transition.setToValue(newValue);
                transition.setCycleCount(1);
                transition.play();
            });
        });
    }
    public void volume(Double value) {
        if(mediaPlayer != null){
            final Timeline volumeTimeline = new Timeline(new KeyFrame(Duration.millis(200), new KeyValue(mediaPlayer.volumeProperty(), value)));
            volumeTimeline.setCycleCount(1);
            volumeTimeline.play();
        }
    }

    public void setDuration(Double value) {
        editor.setDuration(value);
        if(! model.isIsPlaying()){
            editor.display(mediaPlayer.getCurrentTime());
        }
    }
}
