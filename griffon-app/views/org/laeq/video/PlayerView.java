package org.laeq.video;

import griffon.core.artifact.GriffonView;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;
import org.laeq.VifecoView;
import org.laeq.model.Category;
import org.laeq.model.Icon;
import org.laeq.model.Point;
import org.laeq.model.VideoPoint;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.SortedSet;

@ArtifactProviderFor(GriffonView.class)
public class PlayerView extends AbstractJavaFXGriffonView {
    private static int REWIND_VALUE = 10;
    private Point2D mousePosition;
    private Media media;
    private MediaPlayer mediaPlayer;
    private Duration duration;

    @MVCMember @Nonnull private PlayerController controller;
    @MVCMember @Nonnull private PlayerModel model;
    @MVCMember @Nonnull private VifecoView parentView;
    @FXML private MediaView mediaView;
    @FXML private Pane iconPane;
    @FXML private Button playActionTarget;
    @FXML private Button rewindActionTarget;
    @FXML private Button forwardActionTarget;
    @FXML private Button backVideoActionTarget;
    @FXML private Slider timeSlider;
    @FXML private Label durationLabel;
    @Inject private VideoService videoService;

    private ControlsModel controlsModel;

    //Listeners
    private InvalidationListener sliderTimeListener;
    private InvalidationListener currentTimeListener;
    private EventHandler<KeyEvent> keyListener;
    private EventHandler<? super MouseEvent> mouseMoveListener;

    private ObservableSet<VideoPoint> displayPoint;
    private SetChangeListener<VideoPoint> displayPointListener;

    @Override
    public void initUI() {
        Node node = loadFromFXML();

        sliderTimeListener = observable -> {
            if(timeSlider.isPressed() && mediaPlayer != null){
                mediaPlayer.seek(duration.multiply(timeSlider.getValue() / 100));
            } else if (timeSlider.isValueChanging() && mediaPlayer != null) {
                mediaPlayer.seek(duration.multiply(timeSlider.getValue() / 100.0));
            }
        };
        currentTimeListener = observable -> { updateValues(); };
        keyListener = event -> { keyValues(event);};
        mouseMoveListener = mouseEvent -> {
            mousePosition = new Point2D(
                    mouseEvent.getX() / iconPane.getBoundsInLocal().getWidth(),
                    mouseEvent.getY() / iconPane.getBoundsInLocal().getHeight()
            );
        };

        VideoListView test = (VideoListView) getApplication().getMvcGroupManager().findGroup("videoList").getView();

        connectActions(node, controller);

        Tab tab = new Tab();
        tab.textProperty().bind(model.videoPathProperty());
        tab.setContent(node);

        test.getTabPane().getTabs().add(tab);

        displayPoint  = FXCollections.observableSet();
        displayPointListener = change -> {
            if(change.wasAdded()){
                Icon icon = change.getElementAdded().getIcon();
                icon.setBounds(iconPane.getBoundsInLocal());
                iconPane.getChildren().add(icon);
            }

            if(change.wasRemoved()){
                iconPane.getChildren().remove(change.getElementRemoved().getIcon());
            }
        };

        subInit();
    }

    private void subInit() {
        rewindActionTarget.setText("");
        forwardActionTarget.setText("");
        backVideoActionTarget.setText("");

        mediaView.boundsInLocalProperty().addListener((observable, oldValue, newValue) -> {
            iconPane.setPrefWidth(newValue.getWidth());
            iconPane.setPrefHeight(newValue.getHeight());
            repositionIcons();
        });
    }

    private void repositionIcons() {

    }

    private void initPlayer() {
        timeSlider.valueProperty().addListener(observable -> {
            if (timeSlider.isValueChanging()) {
                if (mediaPlayer != null) {
                    mediaPlayer.seek(duration.multiply(timeSlider.getValue() / 100.0));
                }
            }
        });
    }

    public void debug() {
//        for(int i = 0; i < 2000; i ++){
//            double x = rand(100, 800);
//            double y = rand(100, 600);
//            Point2D point = new Point2D(x / iconPane.getBoundsInLocal().getWidth(), y / iconPane.getBoundsInLocal().getHeight());
//           try {
//               videoService.addVideoIcon(point, Duration.seconds(rand(20, 1000)));
//           } catch (FileNotFoundException e) {
//               e.printStackTrace();
//           }
//        }
    }


    public void play() {
//        debug();
        if (model.isIsPlaying()) {
            mediaPlayer.pause();
        } else {
            mediaPlayer.play();
        }

        model.setIsPlaying(!model.isIsPlaying());
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
        System.out.println("mouse clicked");
//        Point point = new Point();
//        point.setX(mouseEvent.getX() / iconPane.getBoundsInLocal().getWidth());
//        point.setY(mouseEvent.getY() / iconPane.getBoundsInLocal().getHeight());
//        point.setCategory(model.getCategory("1").get());
//        point.setVideo(model.getVideo());
//        point.setUser(model.getUser());
//        point.setStart(mediaPlayer.getCurrentTime());
//
//        model.addVideoPoint(point);
    }

    public void setVolume() {
        getLog().info("set volume");
    }

    private double rand(double min, double max) {
        return min + Math.random() * (max - min);
    }

    private void clear() {
        try {
            playActionTarget.setDisable(true);
            timeSlider.valueProperty().removeListener(sliderTimeListener);
            parentView.getScene().removeEventHandler(KeyEvent.KEY_RELEASED, keyListener);
            displayPoint.removeListener(displayPointListener);

            media = null;
            mediaPlayer = null;
            iconPane.getChildren().clear();
            displayPoint.clear();

        } catch (Exception e) {
            getLog().error(e.getMessage());
        }
    }

    private void setUp() {
        File file = new File(model.videoPathProperty().getValue());

        if (file.exists()) {
            try {
                media = new Media(file.getCanonicalFile().toURI().toString());
            } catch (IOException e) {
                getLog().error(String.format("PlayerView: file not exits %s", model.videoPathProperty().getValue()));
            }
            mediaPlayer = new MediaPlayer(media);
            mediaView.setMediaPlayer(mediaPlayer);
            playActionTarget.setDisable(false);

            mediaPlayer.setOnReady(() -> {
                duration = mediaPlayer.getMedia().getDuration();

                displayPoint.addListener(displayPointListener);

                displayPoints();
            });

        } else {
            getLog().error(String.format("PlayerView: file not exits %s", model.videoPathProperty().getValue()));
        }
    }

    private void displayPoints() {
        SortedSet<VideoPoint> points = model.display(mediaPlayer.getCurrentTime());
        displayPoint.retainAll(points);
        points.parallelStream().filter(videoPoint -> ! displayPoint.contains(videoPoint)).forEach(videoPoint -> displayPoint.add(videoPoint));
    }

    public void init() {
        //Remove listeners
        clear();

        //Create media
        setUp();

        //Add Listeners
        mediaPlayer.currentTimeProperty().addListener(currentTimeListener);
        timeSlider.valueProperty().addListener(sliderTimeListener);
        parentView.getScene().setOnKeyPressed(keyListener);

        iconPane.setOnMouseEntered(event -> {
            iconPane.setOnMouseMoved(mouseMoveListener);
            System.out.println("Mouse entered");
        });

        iconPane.setOnMouseExited(event -> {
            System.out.println("Mouse exited");
            iconPane.removeEventHandler(MouseEvent.MOUSE_MOVED, mouseMoveListener);
            mousePosition = null;
        });

        iconPane.widthProperty().addListener(observable -> {
            iconPane.getChildren().forEach(node -> ((Icon)node).setBounds(iconPane.getBoundsInLocal()));
        });

        iconPane.heightProperty().addListener(observable -> {
            iconPane.getChildren().forEach(node -> ((Icon)node).setBounds(iconPane.getBoundsInLocal()));
        });
    }

    private void keyValues(KeyEvent event) {
        Optional<Category> category = model.getCategory(event.getCode().getName());

        if(mediaPlayer != null && category.isPresent() && mousePosition != null){
            Point relPoint = new Point();
            relPoint.setX(mousePosition.getX());
            relPoint.setY(mousePosition.getY());
            relPoint.setCategory(category.get());
            relPoint.setVideo(model.getVideo());
            relPoint.setUser(model.getUser());
            relPoint.setStart(mediaPlayer.getCurrentTime());

            controller.savePoint(relPoint);
        }
    }

    private void updateValues() {
        Platform.runLater(() -> {
            displayPoints();

            timeSlider.setDisable(duration.isUnknown());

            if (!timeSlider.isDisabled() && duration.greaterThan(Duration.ZERO) && !timeSlider.isValueChanging()) {
                timeSlider.setValue(mediaPlayer.getCurrentTime().divide(duration).toMillis() * 100.0);
            }

            durationLabel.setText(
                    String.format("%s / %s",
                            videoService.formatDuration(mediaPlayer.getCurrentTime()),
                            videoService.formatDuration(mediaPlayer.getTotalDuration())
                    )
            );
        });
    }
}
