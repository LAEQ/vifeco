package org.laeq.editor;

import griffon.core.artifact.GriffonView;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;
import org.laeq.model.*;
import org.laeq.model.icon.IconPointColorized;
import org.laeq.model.icon.IconSVG;
import org.laeq.model.icon.IconSize;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
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
    @FXML public TableColumn<CategoryCount, String> shortcutTS;
    @FXML public TableColumn<CategoryCount, Number> totalTS;

    @FXML public TableView<Point> timelineTable;
    @FXML public TableColumn<Point, Icon> iconTD;
    @FXML public TableColumn<Point, String> startTD;
    @FXML public TableColumn<Point, Number> xTD;
    @FXML public TableColumn<Point, Number> yTD;
    @FXML public TableColumn<Point, Void>  delete;

    //Video player
    private MediaPlayer mediaPlayer;
    @FXML private Pane playerPane;
    @FXML private MediaView mediaView;
    @FXML private Pane iconPane;
    @FXML private Slider slider;
    @FXML private TextField elapsed;
    @FXML private Label duration;

    @FXML private Button addActionTarget;
    @FXML private Button playActionTarget;
    @FXML private Button stopActionTarget;
    @FXML private Button controlsActionTarget;

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
        shortcutTS.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().category.getShortcut()));
        totalTS.setCellValueFactory(cellData -> cellData.getValue().total);
        summaryTable.setItems(model.summary);

        iconTD.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCategory().getIcon2()));
        startTD.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStartFormatted()));
        xTD.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getX()));
        yTD.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getY()));
        delete.setCellFactory(deleteActions());

        FXCollections.sort(model.points);
        timelineTable.setItems(model.points);

        return scene;
    }

    //Rendering method
    private void updateValues() {
        Platform.runLater(() -> {
            displayPoints();
//            slider.setDisable(editor.getDuration().isUnknown());
            if (!slider.isDisabled() && video.getDuration().greaterThanOrEqualTo(Duration.ZERO) && !slider.isValueChanging()) {
                slider.setValue(mediaPlayer.getCurrentTime().divide(video.getDuration()).toMillis() * 100.0);
            }

            elapsed.setText(DurationFormatUtils.formatDuration((long) mediaPlayer.getCurrentTime().toMillis(), "HH:mm:ss"));
        });
    }

    public void refresh() {
        displayPoints();
    }

    private void displayPoints() {
        iconPane.getChildren().clear();

        Duration currentTime = mediaPlayer.getCurrentTime();
        Duration startDuration = currentTime.subtract(model.controls.display());

        FilteredList<Point> points = model.points.filtered(point ->
                point.getStart().greaterThanOrEqualTo(startDuration) && point.getStart().lessThanOrEqualTo(currentTime)
        );

        points.forEach(p -> {
            IconPointColorized icon = new IconPointColorized(new IconSize(p.getCategory(), 40));
            icon.decorate();
            icon.setLayoutX(p.getX() * model.width.doubleValue());
            icon.setLayoutY(p.getY() * model.height.doubleValue());
            iconPane.getChildren().add(icon);
        });
    }

    private Callback<TableColumn<Point, Void>, TableCell<Point, Void>> deleteActions() {
        return param -> {
            final  TableCell<Point, Void> cell = new TableCell<Point, Void>(){
                Button delete = new Button("");
                {
                    delete.setLayoutX(5);

                    delete.setGraphic(new Icon(IconSVG.bin, org.laeq.model.icon.Color.gray_dark));
                    delete.setOnAction(event -> {
                        controller.deletePoint(timelineTable.getItems().get(getIndex()));
                    });
                }

                @Override
                public void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(delete);
                    }
                }
            };

            return cell;
        };
    }

    //Listeners
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
                model.mousePosition[0] = event.getX();
                model.mousePosition[1] = event.getY();
//                getApplication().getEventRouter().publishEvent("status.info.parametrized", Arrays.asList("debug", model.normalPosition().toString()));
            }
        };
    }
}
