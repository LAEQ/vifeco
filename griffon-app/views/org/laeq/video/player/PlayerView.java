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
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.laeq.TranslatedView;
import org.laeq.VifecoView;
import org.laeq.model.Icon;
import org.laeq.model.Point;
import org.laeq.model.icon.Color;
import org.laeq.model.icon.IconPointPNG;
import org.laeq.model.icon.IconSVG;
import org.laeq.video.VideoService;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

@ArtifactProviderFor(GriffonView.class)
public class PlayerView extends TranslatedView {
    private Point2D mousePosition;
    private Node nodeOver;

    @MVCMember @Nonnull private PlayerController controller;
    @MVCMember @Nonnull private PlayerModel model;
    @MVCMember @Nonnull private ContainerView parentView;
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
        parentView.getPlayerPane().getChildren().add(node);
        rootView = (VifecoView) getApplication().getMvcGroupManager().getViews().get("vifeco");

        keyListener = event -> keyValues(event);
        mouseMoveListener = mouseEvent -> {
            mousePosition = new Point2D(
                    mouseEvent.getX() / iconPane.getBoundsInLocal().getWidth(),
                    mouseEvent.getY() / iconPane.getBoundsInLocal().getHeight()
            );

            nodeOver = (Node)(mouseEvent.getTarget());
        };

        editor.getVideoPane().addListener((SetChangeListener<IconPointPNG>) change -> {
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

        connectActions(node, controller);
        init();
        subInitUI();
    }

    private EventHandler<ScrollEvent> scrollListener(){
        return event -> {
            if(event.getDeltaY() > 0){
                editor.increaseRate();
            } else {
                editor.decreateRate();
            }

            controller.updateRate(editor.getRate());
        };
    }

    @Override
    public void mvcGroupDestroy() {
        runInsideUISync(() -> destroy());
    }

    public void play() {
        editor.play();
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

        editor.isPlayingProperty().addListener((observable, oldValue, newValue) -> {
            runInsideUISync(() -> {
                if(newValue.booleanValue()){
                    playActionTarget.setGraphic(playerIcons.get(pauseStr));
                } else {
                    playActionTarget.setGraphic(playerIcons.get(playStr));
                }
            });

        });
    }

    private void displayPoints() {
        runInsideUISync(() -> editor.display());
    }

    private void init() {
        if(editor.isValid()){
            mediaView.setMediaPlayer(editor.getMediaPlayer());
            playActionTarget.setDisable(false);
            updateValues();
        } else {
            playActionTarget.setDisable(true);
            alert("org.laeq.title.error", "org.laeq.video.file.error");
        }

        //Settings all the listeners ....
        currentTimeListener = currentTimeListener();
        editor.getMediaPlayer().currentTimeProperty().addListener(currentTimeListener);

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

        editor.getMediaPlayer().currentTimeProperty().removeListener(currentTimeListener);
        currentTimeListener = null;

        timeSlider.valueProperty().removeListener(sliderListener);
        sliderListener = null;

        rootView.getScene().removeEventHandler(KeyEvent.KEY_PRESSED, keyListener);
        keyListener = null;

        iconPane.getChildren().clear();

        editor.getMediaPlayer().dispose();

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
        return (observable -> updateValues());
    }

    private InvalidationListener sliderListener(){
        return  observable -> {
            if(timeSlider.isPressed()){
                updateValues();
                editor.getMediaPlayer().seek(editor.getDuration().multiply(timeSlider.getValue() / 100));
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
        if(event.getCode().equals(KeyCode.SPACE)){
            controller.play();
            return;
        }

        if((event.getCode().equals(KeyCode.ESCAPE) || event.getCode().equals(KeyCode.DELETE)) && nodeOver !=  null && nodeOver.getParent() instanceof IconPointPNG){
            Point point = editor.deleteVideoIcon((IconPointPNG) (nodeOver.getParent()));

            if(point != null){
                controller.deletePoint(point);
            }

            return;
        }

        if(mousePosition != null){
            Point point = editor.addPoint(mousePosition, event);

            if(point != null){
                controller.addPoint(point);
            }
        }
    }
    private void updateValues() {
        Platform.runLater(() -> {
            displayPoints();

            timeSlider.setDisable(editor.getDuration().isUnknown());

            if (!timeSlider.isDisabled() && editor.getDuration().greaterThanOrEqualTo(Duration.ZERO) && !timeSlider.isValueChanging()) {
                timeSlider.setValue(editor.getMediaPlayer().getCurrentTime().divide(editor.getDuration()).toMillis() * 100.0);
            }

            durationLabel.setText(videoService.getDurationText(editor.getMediaPlayer().getCurrentTime(), editor.getDuration()));
        });
    }
    private Icon generatePlayerIcon(String path, String color){
        return new Icon(path, color);
    }

    public void forward(int seconds) {
        Duration nowPlus30 = editor.getMediaPlayer().getCurrentTime().add(Duration.millis(seconds * 1000));
        Duration now = nowPlus30.lessThan(editor.getTotalDuration())? nowPlus30 : editor.getTotalDuration();

        editor.getMediaPlayer().seek(now);
        runInsideUISync(() -> editor.display());

    }
    public void backward(int seconds) {
        Duration nowMinus = editor.getMediaPlayer().getCurrentTime().subtract(Duration.millis(seconds * 1000));
        Duration now = nowMinus.greaterThan(editor.getMediaPlayer().getStartTime())? nowMinus : editor.getMediaPlayer().getStartTime();

        editor.getMediaPlayer().seek(now);
        runInsideUISync(() -> editor.display());
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
        runInsideUISync(() -> iconPane.getChildren().forEach(n -> {
            FadeTransition transition = new FadeTransition(Duration.millis(100), n);
            transition.setInterpolator(Interpolator.LINEAR);
            transition.setFromValue(oldvalue);
            transition.setToValue(newValue);
            transition.setCycleCount(1);
            transition.play();
        }));
    }
    public void volume(Double value) {
        final Timeline volumeTimeline = new Timeline(new KeyFrame(Duration.millis(200), new KeyValue(editor.getMediaPlayer().volumeProperty(), value)));
        volumeTimeline.setCycleCount(1);
        volumeTimeline.play();
    }

    public void setDuration(Double value) {
        editor.setDuration(value);
        runInsideUISync(() ->  editor.display());
    }

    public void rate(Double rate) {
        editor.getMediaPlayer().setRate(rate);
    }
}
