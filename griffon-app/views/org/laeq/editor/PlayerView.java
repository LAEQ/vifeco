package org.laeq.editor;

import griffon.core.artifact.GriffonView;
import griffon.core.i18n.MessageSource;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.SetChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
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
import org.laeq.HelperService;
import org.laeq.model.CategoryCount;
import org.laeq.model.Icon;
import org.laeq.model.Point;
import org.laeq.model.Video;
import org.laeq.model.icon.IconPointColorized;
import org.laeq.model.icon.IconSVG;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;

@ArtifactProviderFor(GriffonView.class)
public class PlayerView extends AbstractJavaFXGriffonView {
    @MVCMember @Nonnull private PlayerController controller;
    @MVCMember @Nonnull private PlayerModel model;
    @MVCMember @Nonnull private Video video;

    @Inject private HelperService helperService;

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

    private Boolean wasPlaying = false;


    private MessageSource messageSource;
    private ChangeListener<String> elapListen = elapsedListener();
    private EventHandler<KeyEvent> elapKeyListen = elapsedKeyPressed();
    private ChangeListener<Number> sliderListener = sliderListener();
    private ChangeListener<? super Duration> currentTimeListener = currentTimeListener();

    @Override
    public void initUI() {
        Stage stage = (Stage) getApplication()
            .createApplicationContainer(Collections.<String,Object>emptyMap());
        stage.setTitle(getApplication().getConfiguration().getAsString(getApplication().getMessageSource().getMessage("editor.window.title")));
        scene = init();
        stage.setScene(scene);
        stage.sizeToScene();
        stage.setAlwaysOnTop(true);

        getApplication().getWindowManager().attach("editor", stage);
        getApplication().getWindowManager().show("editor");

        stage.setOnCloseRequest(event -> {
            mediaPlayer.pause();
            getApplication().getEventRouter().publishEvent("mvc.clean", Arrays.asList("display"));
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

        initPlayer();
        messageSource = getApplication().getMessageSource();
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
        timelineTable.setPlaceholder(new Label(""));

        timelineTable.getSelectionModel().selectedItemProperty().addListener(rowlistener());

        return scene;
    }

    private void initPlayer(){
        try {
            File file = new File(video.getPath());
            Media media = new Media(file.getCanonicalFile().toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            mediaView.setMediaPlayer(mediaPlayer);
            mediaPlayer.setOnReady(() ->{
                model.isReady.set(Boolean.TRUE);
                mediaPlayer.play();
                mediaPlayer.pause();
            });
            duration.setText(video.getDurationFormatted());
            mediaView.boundsInLocalProperty().addListener((observable, oldValue, newValue) -> {
                model.width.set(newValue.getWidth());
                model.height.set(newValue.getHeight());
                iconPane.setPrefWidth(model.width.doubleValue());
                iconPane.setPrefHeight(model.height.doubleValue());
                iconPane.getChildren().clear();
                model.displayed.forEach(p ->{
                    IconPointColorized icon = p.getIconPoint();
                    Double x = p.getX() * model.width.doubleValue();
                    Double y = p.getY() * model.height.doubleValue();
                    icon.setLayoutX(x);
                    icon.setLayoutY(y);
                    iconPane.getChildren().add(icon);
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

            model.displayed.addListener((SetChangeListener<Point>) change ->  {
                if(change.wasAdded()){
                    IconPointColorized icon = change.getElementAdded().getIconPoint();
                    Double x = change.getElementAdded().getX() * model.width.doubleValue();
                    Double y = change.getElementAdded().getY() * model.height.doubleValue();
                    icon.setLayoutX(x);
                    icon.setLayoutY(y);
                    icon.setScaleX(model.controls.scale());
                    icon.setScaleY(model.controls.scale());
                    icon.setOpacity(model.controls.opacity.getValue());
                    runInsideUIAsync(() -> {
                        iconPane.getChildren().add(icon);
                    });
                } else if(change.wasRemoved()){
                    runInsideUIAsync(() -> {
                        iconPane.getChildren().remove(change.getElementRemoved().getIconPoint());
                    });
                }
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

            model.setCurrentTime(mediaPlayer.getCurrentTime());
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
                    updateValues();
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
        return (EventHandler<KeyEvent>) event -> { controller.addPoint(event.getCode(), mediaPlayer.getCurrentTime());};
    }
    private EventHandler<? super MouseEvent> mouseexit() {
        return (EventHandler<MouseEvent>) event -> { model.enabled = Boolean.FALSE; };
    }
    private EventHandler<? super MouseEvent> mouseenter() {
        return (EventHandler<MouseEvent>) event -> { model.enabled = Boolean.TRUE; };
    }
    private EventHandler<MouseEvent> mousemove(){
        return event -> {
            if(model.enabled){
                model.mousePosition[0] = event.getX();
                model.mousePosition[1] = event.getY();
            }
        };
    }

    //Rendering method
    private void updateValues() {
        Platform.runLater(() -> {
            displayPoints();
            if (! slider.isDisabled()
                    && ! slider.isPressed()
                    && video.getDuration().greaterThanOrEqualTo(Duration.ZERO)
                    && !slider.isValueChanging()) {
                slider.setValue(mediaPlayer.getCurrentTime().divide(video.getDuration()).toMillis() * 100.0);
                elapsed.setText(DurationFormatUtils.formatDuration((long) mediaPlayer.getCurrentTime().toMillis(), "HH:mm:ss"));
            }
        });
    }

    public Duration getCurrentTime() {
        return mediaPlayer.getCurrentTime();
    }

    public void displayPoints() {
        runOutsideUIAsync(() -> {
            model.setCurrentTime(mediaPlayer.getCurrentTime());
        });
    }

    private Callback<TableColumn<Point, Void>, TableCell<Point, Void>> deleteActions() {
        return param -> {
            final  TableCell<Point, Void> cell = new TableCell<Point, Void>(){
                Button delete = new Button(translate("btn.delete"));
                {
                    delete.setLayoutX(5);
                    delete.getStyleClass().addAll("btn", "btn-danger", "btn-sm");
                    delete.setOnAction(event -> controller.deletePoint(timelineTable.getItems().get(getIndex())));
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

    private String translate(String key) {
        return messageSource.getMessage(key);
    }

    //Listeners
    private ChangeListener<Number> sliderListener(){
        return (observable, oldValue, newValue) -> {
            runInsideUIAsync(() -> {
                Duration now = video.getDuration().multiply(slider.getValue() / 100);
                controller.updateCurrentTime(now);
                mediaPlayer.seek(now);
                runOutsideUI(() -> {
                    model.setCurrentTime(now);
                });
            });
        };
    }
    private ChangeListener<Duration> currentTimeListener(){
        return (observable, oldValue, newValue) -> {
            updateValues();
        };
    }

    private ChangeListener<Point> rowlistener(){
        return (observable, oldValue, newValue) -> {
            runInsideUIAsync(() -> {
                controller.updateCurrentTime(newValue.getStart());
                mediaPlayer.seek(newValue.getStart());
                runOutsideUI(() -> {
                    model.setCurrentTime(newValue.getStart());
                });
            });
        };
    }

//    public void setCurrentTime(Duration currentTime) {
//        Platform.runLater(() -> {
//            mediaPlayer.seek(currentTime);
//        });
//    }
}
