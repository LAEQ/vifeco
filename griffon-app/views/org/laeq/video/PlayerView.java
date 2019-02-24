package org.laeq.video;

import griffon.core.artifact.GriffonView;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;
import org.laeq.VifecoView;
import org.laeq.graphic.Color;
import org.laeq.graphic.IconSVG;
import org.laeq.model.*;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.*;

@ArtifactProviderFor(GriffonView.class)
public class PlayerView extends AbstractJavaFXGriffonView {
    private Point2D mousePosition;
    private Media media;
    private MediaPlayer mediaPlayer;
    private Duration duration;
    private ObservableSet<Point> pointsDisplayed;

    @MVCMember @Nonnull private PlayerController controller;
    @MVCMember @Nonnull private PlayerModel model;
    @MVCMember @Nonnull private Player2View parentView;
    @MVCMember @Nonnull private Video video;

    VifecoView rootView;

    @FXML private MediaView mediaView;
    @FXML private Pane iconPane;
    @FXML private Button playActionTarget;
    @FXML private Button rewindActionTarget;
    @FXML private Button forwardActionTarget;
    @FXML private Button backVideoActionTarget;
    @FXML private Slider timeSlider;
    @FXML private Text durationLabel;
    @Inject private VideoService videoService;

    private ControlsModel controlsModel;

    private int index = 1;
    private final Duration FADE_DURATION = Duration.millis(200);

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


    @Override
    public void initUI() {
        rootView = (VifecoView) getApplication().getMvcGroupManager().getViews().get("vifeco");
        pointsDisplayed = FXCollections.observableSet();
        pointsDisplayed.addListener(new SetChangeListener<Point>() {
            @Override
            public void onChanged(Change<? extends Point> change) {
                if(change.wasAdded()){
                    Icon icon = change.getElementAdded().getIcon(iconPane.getBoundsInLocal());
                    icon.setOpacity(model.getOpacity());
                    icon.setScaleX(model.getSize() / 100);
                    icon.setScaleY(model.getSize() / 100);
                    iconPane.getChildren().add(((Point)change.getElementAdded()).getIcon(iconPane.getBoundsInLocal()));
                }

                if(change.wasRemoved()){
                    iconPane.getChildren().remove(change.getElementRemoved().getIcon());
                }
            }
        });

        Node node = loadFromFXML();

        keyListener = event -> { keyValues(event);};
        mouseMoveListener = mouseEvent -> {
            mousePosition = new Point2D(
                    mouseEvent.getX() / iconPane.getBoundsInLocal().getWidth(),
                    mouseEvent.getY() / iconPane.getBoundsInLocal().getHeight()
            );
        };

        connectActions(node, controller);

        init();
        subInitUI();

        parentView.getPlayerPane().getChildren().add(node);
    }

    public void mvcGroupDestroy() {
        runInsideUISync(() -> {
            destroy();
        });
    }

    public void play() {
        if (model.isIsPlaying()) {
            Icon icon = (Icon) playActionTarget.getGraphic();
            icon.setPath(IconSVG.btnPlay);
            mediaPlayer.pause();
            model.setIsPlaying(false);
        } else {
            Icon icon = (Icon) playActionTarget.getGraphic();
            icon.setPath(IconSVG.btnPlay);
            mediaPlayer.play();
            model.setIsPlaying(true);
        }
    }

    @FXML
    public void playerPaneScroll(ScrollEvent event) {
        if (controlsModel == null) {
            controlsModel = (ControlsModel) getApplication().getMvcGroupManager().getAt("controls").getModel();
        }

        if (event.getDeltaY() > 0) {
            controlsModel.increaseRate();
            mediaPlayer.setRate(controlsModel.getRate());
        } else if (event.getDeltaY() < 0) {
            controlsModel.decreateRate();
            mediaPlayer.setRate(controlsModel.getRate());
        }
    }

    @FXML
    public void playerPaneMouseClicked(MouseEvent mouseEvent) {
        runOutsideUI(() -> {
            Point point = new Point();
            point.setX(mouseEvent.getX() / iconPane.getBoundsInLocal().getWidth());
            point.setY(mouseEvent.getY() / iconPane.getBoundsInLocal().getHeight());
            point.setStart(mediaPlayer.getCurrentTime());


            controller.addPoint(point, "A");
        });
    }

    public void setVolume() {
        getLog().info("set volume");
    }

    public void addPoint(Point point) {
        runInsideUISync(() -> {
            pointsDisplayed.add(point);
        });
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
            repositionIcons();
        });
    }

    private void repositionIcons() {


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
            });

        } else {
            getLog().error(String.format("PlayerView: file not exits %s", video));
        }
    }

    private void displayPoints() {
        SortedSet<Point> newPoint = model.displayPoints(mediaPlayer.getCurrentTime());

        pointsDisplayed.retainAll(newPoint);

        newPoint.forEach(p ->{
            if(!pointsDisplayed.contains(p)){
                pointsDisplayed.add(p);
            }
        });
    }

    private void init() {
        //Create media
        setUp();

        currentTimeListener = currentTimeListener();
        mediaPlayer.currentTimeProperty().addListener(currentTimeListener);

        sliderListener = sliderListener();
        timeSlider.valueProperty().addListener(sliderListener);

        displayPoints();


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
        pointsDisplayed.clear();

        mediaPlayer.dispose();
        mediaPlayer = null;
    }

    //Mouse move: icon pane
    private EventHandler<? super MouseEvent> mouseEnterListener() {
        return event -> {
            iconPane.setOnMouseMoved(mouseMoveListener);
            rootView.getScene().setOnKeyPressed(keyListener);
        };
    }
    private EventHandler<MouseEvent> mouseExitListener(){
        return event -> {
            mousePosition = null;
            iconPane.removeEventHandler(MouseEvent.MOUSE_MOVED, mouseMoveListener);
            rootView.getScene().removeEventHandler(KeyEvent.KEY_PRESSED, keyListener);
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
                mediaPlayer.seek(duration.multiply(timeSlider.getValue() / 100));
            } else if (timeSlider.isValueChanging() && mediaPlayer != null) {
                mediaPlayer.seek(duration.multiply(timeSlider.getValue() / 100.0));
            }
        };
    }

    private ChangeListener<Number> iconWidthPropertyListener(){
        return new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                pointsDisplayed.forEach(point -> {
                    point.repositionX((Double) newValue);
                });
            }
        };
    }
    private ChangeListener<Number> iconHeightPropertyListener(){
        return (observable, oldValue, newValue) -> pointsDisplayed.forEach(point -> {
            point.repositionY((Double) newValue);
        });
    }

    private void keyValues(KeyEvent event) {
        Optional<Category> category = model.getCategory(event.getCode().getName());

        System.out.println("key: " + event.getCode().getName() + ":" + category.isPresent());

//
//        if(mediaPlayer != null && category.isPresent() && mousePosition != null){
//            Point relPoint = new Point();
//            relPoint.setX(mousePosition.getX());
//            relPoint.setY(mousePosition.getY());
//            relPoint.setCategory(category.get());
//            relPoint.setVideo(model.getVideo());
//            relPoint.setStart(mediaPlayer.getCurrentTime());
//
//            controller.savePoint(relPoint);
//        }
    }
    private void updateValues() {
        Platform.runLater(() -> {
            displayPoints();

            timeSlider.setDisable(duration.isUnknown());
//            System.out.println("HERE" + index);
//            index++;

            if (!timeSlider.isDisabled() && duration.greaterThanOrEqualTo(Duration.ZERO) && !timeSlider.isValueChanging()) {
                timeSlider.setValue(mediaPlayer.getCurrentTime().divide(duration).toMillis() * 100.0);
            }

            durationLabel.setText(videoService.getDurationText(mediaPlayer.getCurrentTime(), duration));
        });
    }
    private Icon generatePlayerIcon(String path, String color){
        return new Icon(path, color);
    }

    public void reload() {
        runInsideUISync(() -> {
            mediaPlayer.stop();
            mediaPlayer.seek(mediaPlayer.getStartTime());
            pointsDisplayed.clear();
        });
    }
    public void forward() {
        Duration nowPlus30 = mediaPlayer.getCurrentTime().add(Duration.millis(30000));
        if(nowPlus30.lessThan(duration)){
            mediaPlayer.seek(nowPlus30);
        } else {
            mediaPlayer.seek(duration);
        }
    }
    public void backward() {
        Duration nowMinus30 = mediaPlayer.getCurrentTime().subtract(Duration.millis(30000));
        if(nowMinus30.greaterThan(mediaPlayer.getStartTime())){
            mediaPlayer.seek(nowMinus30);
        } else {
            mediaPlayer.seek(mediaPlayer.getStartTime());
        }
    }

    public void rate(Double newValue) {
        if(mediaPlayer != null){
           final Timeline rateTimeline = new Timeline(new KeyFrame(FADE_DURATION, new KeyValue(mediaPlayer.rateProperty(), newValue)));
           rateTimeline.setCycleCount(1);
           rateTimeline.play();
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
            final Timeline volumeTimeline = new Timeline(new KeyFrame(FADE_DURATION, new KeyValue(mediaPlayer.volumeProperty(), value)));
            volumeTimeline.setCycleCount(1);
            volumeTimeline.play();
        }
    }
}
