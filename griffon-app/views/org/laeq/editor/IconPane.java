package org.laeq.editor;

import griffon.core.event.EventRouter;
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

public class IconPane extends Pane {
    EventRouter router;
    Subscription subscription = () -> {};

    private Subscription draggedSubscription;
    private Subscription pressedSubscription;
    private Subscription releaseSubscription;

    private EventStream<MouseEvent> pressed;
    private EventStream<MouseEvent> released;
    private EventStream<Point2D> mouseDragged;

    private Drawing currentDrawing;
    private Canvas currentCanvas;

    public IconPane() {
        EventStream<MouseEvent> moves = EventStreams.eventsOf(this, MouseEvent.MOUSE_MOVED);
        EventStream<Point2D> mouseCoordinates = moves.map(event -> new Point2D(event.getX(), event.getY()));
        subscription.and(mouseCoordinates.subscribe(point -> router.publishEventOutsideUI("mouse.position", Arrays.asList(point))));

        EventStream<MouseEvent> enter = EventStreams.eventsOf(this, MouseEvent.MOUSE_ENTERED);
        EventStream<Boolean> mouseEnter = enter.map(event -> true);
        subscription.and(mouseEnter.subscribe(value -> router.publishEventAsync("mouse.active", Arrays.asList(value))));

        EventStream<MouseEvent> out = EventStreams.eventsOf(this, MouseEvent.MOUSE_EXITED);
        EventStream<Boolean> mouseOut = out.map(event -> false);
        subscription.and(mouseOut.subscribe(value -> router.publishEventAsync("mouse.active", Arrays.asList(value))));

        EventStream<MouseEvent> drag = EventStreams.eventsOf(this, MouseEvent.MOUSE_DRAGGED);
        pressed = EventStreams.eventsOf(this, MouseEvent.MOUSE_PRESSED);
        mouseDragged = drag.map(event -> new Point2D(event.getX(), event.getY()));
        released = EventStreams.eventsOf(this, MouseEvent.MOUSE_RELEASED);
    }

    public void startDraw(DrawingType type, String color){
        pressedSubscription = pressed.subscribe(event -> {
            double width = widthProperty().doubleValue();
            double height = heightProperty().doubleValue();
            Point2D start = new Point2D(event.getX() / width , event.getY() / height);
            currentDrawing = new Drawing(type, color, start, start);
            currentCanvas = currentDrawing.getCanvas(width, height, 25d, 5d);

            draggedSubscription = mouseDragged.subscribe(point -> {
                Point2D end = new Point2D(point.getX() / width , point.getY() / height);
                getChildren().remove(currentCanvas);
                currentDrawing.setEnd(end);
                currentCanvas = currentDrawing.getCanvas(width, height, 25d, 5d);
                getChildren().add(currentCanvas);
            });
        });

        releaseSubscription = released.subscribe(event -> {
            draggedSubscription.unsubscribe();
            getChildren().remove(currentCanvas);

            router.publishEventAsync("drawing.created", Arrays.asList(currentDrawing));

            currentDrawing = null;
            stopDrawLine();
        });
    }


    public void startDrawLine(String color) {
        startDraw(DrawingType.LINE, color);
    }

    public void drawRectangle(String color) {
        startDraw(DrawingType.RECTANGLE, color);
    }

    public void stopDrawLine(){
        pressedSubscription.unsubscribe();
        draggedSubscription.unsubscribe();
        releaseSubscription.unsubscribe();
    }

    public IconPane(Node... children) {
        super(children);
    }

    void dispose() {
        subscription.unsubscribe();
    }

    public void setEventRouter(EventRouter eventRouter) {
        this.router = eventRouter;
    }
}