package org.laeq.editor;

import griffon.core.artifact.GriffonView;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;
import org.laeq.model.Drawing;
import org.reactfx.EventStream;
import org.reactfx.EventStreams;
import org.reactfx.Subscription;

import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicReference;

@ArtifactProviderFor(GriffonView.class)
public class ZoomView extends AbstractJavaFXGriffonView {

    @MVCMember ZoomController controller;
    @MVCMember ZoomModel model;

    @FXML private Slider zoomSlider;
    @FXML private Label zoomLabel;

    @FXML private Rectangle background;
    @FXML private Rectangle frame;

    private Scene scene;

    private EventStream<MouseEvent> pressed;
    private EventStream<MouseEvent> released;
    private EventStream<Point2D> mouseDragged;

    private Subscription draggedSubscription;
    private Subscription pressedSubscription;
    private Subscription releaseSubscription;

    private Point2D mousePoint;

    @Override
    public void mvcGroupDestroy(){
        getApplication().getEventRouter().publishEvent("zoom");
    }

    @Override
    public void initUI() {
        Stage stage = (Stage) getApplication().createApplicationContainer(Collections.<String,Object>emptyMap());
        stage.setTitle(getApplication().getMessageSource().getMessage("zoom.window.title"));
        stage.getIcons().add( getImage("favicon-32x32.png"));
        scene = init();
        stage.setScene(scene);
        stage.sizeToScene();
        stage.setAlwaysOnTop(true);

        getApplication().getWindowManager().attach("zoom", stage);
        getApplication().getWindowManager().show("zoom");

        stage.setOnCloseRequest(event -> {
            getApplication().getEventRouter().publishEvent("mvc.clean", Arrays.asList("zoom"));
        });
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

        initZoomSlider();
        initEvents();

        return scene;
    }

    private void initEvents(){
        frame.setOnMouseDragged(event -> {
            double x = event.getX() - mousePoint.getX();
            double y = event.getY() - mousePoint.getY();

            frame.setTranslateX(frame.getTranslateX() + x);
            frame.setTranslateY(frame.getTranslateY() + y);


            if(frame.getTranslateX() < 0 ){
                frame.setTranslateX(0);
                x = 0;
            }

            if(frame.getTranslateX() > model.frameWidthInit - model.frameWidth.doubleValue()){
                frame.setTranslateX(model.frameWidthInit - model.frameWidth.doubleValue());
                x = model.frameWidthInit - model.frameWidth.doubleValue();
            }

            if(frame.getTranslateY() < 0){
                frame.setTranslateY(0);
                y = 0;
            }

            if(frame.getTranslateY() > model.frameHeightInit - model.frameHeight.doubleValue()){
                frame.setTranslateY(model.frameHeightInit - model.frameHeight.doubleValue());
                y = model.frameHeightInit - model.frameHeight.doubleValue();
            }

            Point2D translatePoint = new Point2D(x, y);

            getApplication().getEventRouter().publishEvent("zoom.translate", Arrays.asList(translatePoint));

            event.consume();
        });

        EventStream<MouseEvent> moves = EventStreams.eventsOf(frame, MouseEvent.MOUSE_MOVED);
        EventStream<Point2D> mouseCoordinates = moves.map(event -> new Point2D(event.getX(), event.getY()));
        mouseCoordinates.subscribe(point -> mousePoint = point);
    }

    private void initZoomSlider() {
        model.zoomProperty().addListener((observable, oldValue, newValue) -> {
            final double zoomFactor = newValue.doubleValue();
            model.setFrameWidth(model.frameWidthInit / zoomFactor);
            model.setFrameHeight(model.frameHeightInit / zoomFactor);
            controller.setZoomProperty();
        });

        model.frameWidthProperty().addListener(((observable, oldValue, newValue) -> frame.setWidth(newValue.doubleValue())));
        model.frameHeightProperty().addListener(((observable, oldValue, newValue) -> frame.setHeight(newValue.doubleValue())));

        model.frameXProperty().addListener((observable, oldValue, newValue) -> frame.setX(newValue.doubleValue()));
        model.frameYProperty().addListener((observable, oldValue, newValue) -> frame.setY(newValue.doubleValue()));

        zoomLabel.setText(String.format("%.2f", model.zoomProperty().getValue()));
        zoomSlider.setValue(model.minZoom);
        zoomSlider.setMin(model.minZoom);
        zoomSlider.setMax(model.maxZoom);
        zoomSlider.setMajorTickUnit(1);
        zoomSlider.setMinorTickCount(1);
        zoomSlider.setShowTickMarks(true);
        zoomSlider.setShowTickLabels(true);
        zoomSlider.valueProperty().addListener((obs, oldval, newVal) -> {
            final double value = Math.round(newVal.doubleValue() * 100) / 100f;
            zoomSlider.setValue(value);
            zoomLabel.setText(String.format("%.2f", value));
            model.zoomProperty().setValue(value);
        });
    }


    private String translate(String key){
        return getApplication().getMessageSource().getMessage(key);
    }

    private Image getImage(String path) {
        return new Image(getClass().getClassLoader().getResourceAsStream(path));
    }
}
