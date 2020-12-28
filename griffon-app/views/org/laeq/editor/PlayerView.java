package org.laeq.editor;

import griffon.core.artifact.GriffonView;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
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
import org.laeq.model.Category;
import org.laeq.model.CategoryCount;
import org.laeq.model.Icon;
import org.laeq.model.Video;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import javax.annotation.Nonnull;

@ArtifactProviderFor(GriffonView.class)
public class PlayerView extends AbstractJavaFXGriffonView {
    @MVCMember @Nonnull private PlayerController controller;
    @MVCMember @Nonnull private PlayerModel model;
    @MVCMember @Nonnull private Video video;

    private Scene scene;

    @FXML public Label title;
    @FXML public TableView<CategoryCount> summaryTable;
    @FXML public TableColumn<CategoryCount, Icon> iconTS;
    @FXML public TableColumn<CategoryCount, String> nameTS;
    @FXML public TableColumn<CategoryCount, Number> totalTS;

    //Video player
    private MediaPlayer mediaPlayer;
    @FXML private Pane playerPane;
    @FXML private MediaView mediaView;
    @FXML private Pane iconPane;
    @FXML private Slider slider;
    @FXML private TextField elapsed;
    @FXML private Label duration;

    @Override
    public void initUI() {
        Stage stage = (Stage) getApplication()
            .createApplicationContainer(Collections.<String,Object>emptyMap());
        stage.setTitle(getApplication().getConfiguration().getAsString(getApplication().getMessageSource().getMessage("editor.window.title")));
        scene = init();
        stage.setScene(scene);
        stage.sizeToScene();
        stage.setAlwaysOnTop(true);

        getApplication().getWindowManager().attach("player", stage);
        getApplication().getWindowManager().show("player");

        initPlayer();
    }

    public void play(){
        mediaPlayer.play();
    }

    public void pause(){
        mediaPlayer.pause();
    }

    private void initPlayer(){
        try {
            File file = new File(video.getPath());
            Media media = new Media(file.getCanonicalFile().toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            mediaView.setMediaPlayer(mediaPlayer);

            duration.setText(video.getDurationFormatted());

            mediaView.boundsInLocalProperty().addListener((observable, oldValue, newValue) -> {
                model.width.set(newValue.getWidth());
                model.height.set(newValue.getHeight());
                iconPane.setPrefWidth(model.width.doubleValue());
                iconPane.setPrefHeight(model.height.doubleValue());
            });

            slider.valueProperty().addListener(sliderListener());
            mediaPlayer.currentTimeProperty().addListener(currentTimeListener());
            iconPane.setOnMouseMoved(mousemove());

            iconPane.setOnMouseExited(mouseexit());
            iconPane.setOnMouseEntered(mouseenter());

            scene.setOnKeyReleased(keyReleased());

            updateValues();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private EventHandler<? super KeyEvent> keyReleased() {
        return (EventHandler<KeyEvent>) event -> { controller.addPoint(event.getCode(), mediaPlayer.getCurrentTime());};
    }


    private EventHandler<? super MouseEvent> mouseexit() {
        return (EventHandler<MouseEvent>) event -> { model.enabled = Boolean.FALSE; };
    }

    private EventHandler<? super MouseEvent> mouseenter() {
        return (EventHandler<MouseEvent>) event -> { model.enabled = Boolean.TRUE; };
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

        iconTS.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().category.getIcon2()));
        nameTS.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().category.getName()));
        totalTS.setCellValueFactory(cellData -> cellData.getValue().total);

        summaryTable.setItems(model.summary);

        return scene;
    }

    //Rendering method
    private void updateValues() {
        Platform.runLater(() -> {
//            displayPoints();

//            slider.setDisable(editor.getDuration().isUnknown());

            if (!slider.isDisabled() && video.getDuration().greaterThanOrEqualTo(Duration.ZERO) && !slider.isValueChanging()) {
                slider.setValue(mediaPlayer.getCurrentTime().divide(video.getDuration()).toMillis() * 100.0);
            }

            elapsed.setText(DurationFormatUtils.formatDuration((long) mediaPlayer.getCurrentTime().toMillis(), "HH:mm:ss"));
        });
    }

    //Listeners

    //Slider
    private InvalidationListener sliderListener(){
        return  observable -> {
            if(slider.isPressed()){
                updateValues();
                mediaPlayer.seek(video.getDuration().multiply(slider.getValue() / 100));
            }
        };
    }
    private InvalidationListener currentTimeListener(){
        return (observable -> updateValues());
    }

    //Icon pane
    private EventHandler<MouseEvent> mousemove(){
        return event -> {
            if(model.enabled){
                model.mousePositon[0] = event.getX();
                model.mousePositon[1] = event.getY();
                getApplication().getEventRouter().publishEvent("status.info.parametrized", Arrays.asList("debug", model.normalPosition().toString()));
            }
        };
    }
//
//    private ChangeListener<Number> resize() {
//        return (observable, oldValue, newValue) -> {
//            System.out.println(mediaView.getFitWidth() + " - " + mediaView.getFitHeight());
//            iconPane.resize(Math.random() * 1000, Math.random() * 1000);
//        };
//    }
}
