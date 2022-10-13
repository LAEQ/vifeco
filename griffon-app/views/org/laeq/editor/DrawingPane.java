package org.laeq.editor;

import griffon.core.event.EventRouter;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import org.laeq.model.Drawing;
import org.reactfx.EventStream;
import org.reactfx.EventStreams;
import org.reactfx.Subscription;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DrawingPane extends Pane {
    EventRouter router;

    private Subscription draggedSubscription;
    private Subscription pressedSubscription;
    private Subscription releaseSubscription;
    public ObservableList<Drawing> drawings = FXCollections.observableArrayList();
    private Drawing currentDrawing;
    private Canvas currentCanvas;
    private EventStream<MouseEvent> pressed;
    private EventStream<MouseEvent> moves;
    private EventStream<MouseEvent> released;
    private EventStream<Point2D> mouseDragged;

    public DrawingPane() {
        moves = EventStreams.eventsOf(this, MouseEvent.MOUSE_DRAGGED);
        mouseDragged = moves.map(event -> new Point2D(event.getX(), event.getY()));
        pressed = EventStreams.eventsOf(this, MouseEvent.MOUSE_PRESSED);
        released = EventStreams.eventsOf(this, MouseEvent.MOUSE_RELEASED);

        drawings.addListener((ListChangeListener<Drawing>) c -> {


            List<Canvas> canvasList = drawings.stream()
                    .map(drawing -> drawing.getCanvas(widthProperty().doubleValue(), heightProperty().doubleValue(), 0, 0))
                    .collect(Collectors.toList());

            getChildren().clear();
            getChildren().addAll(canvasList);
        });
    }

    public DrawingPane(Node... children) {
        super(children);
    }

    void dispose() {

    }

    public void setEventRouter(EventRouter eventRouter) {
        this.router = eventRouter;
    }

    public void startDraw(DrawingType type){
        pressedSubscription = pressed.subscribe(event -> {
            Point2D pt = new Point2D(event.getX(), event.getY());
            currentDrawing = new Drawing(type, "#00FF00", pt, pt);
            currentCanvas = currentDrawing.getCanvas(widthProperty().doubleValue(), heightProperty().doubleValue(), 25d, 5d);

            draggedSubscription = mouseDragged.subscribe(point -> {
                getChildren().remove(currentCanvas);
                currentDrawing.setEnd(point);
                currentCanvas = currentDrawing.getCanvas(widthProperty().doubleValue(), heightProperty().doubleValue(), 25d, 5d);
                getChildren().add(currentCanvas);
            });
        });

        releaseSubscription = released.subscribe(event -> {
            draggedSubscription.unsubscribe();
            getChildren().remove(currentCanvas);
            currentDrawing.setActive(true);

            router.publishEventAsync("drawing.created", Arrays.asList(currentDrawing));

            currentDrawing = null;
            stopDrawLine();
        });
    }

    public void startDrawLine() {
        startDraw(DrawingType.LINE);
    }

    public void drawRectangle() {
        startDraw(DrawingType.RECTANGLE);
    }

    public void stopDrawLine(){
        pressedSubscription.unsubscribe();
        draggedSubscription.unsubscribe();
        releaseSubscription.unsubscribe();
    }
}