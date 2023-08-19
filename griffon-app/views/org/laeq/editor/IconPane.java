package org.laeq.editor;

import griffon.core.event.EventRouter;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import org.laeq.model.Drawing;
import org.laeq.model.icon.IconPointColorized;
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

    private Boolean active = Boolean.FALSE;
    private Point2D mousePosition;

    public IconPane() {
        EventStream<MouseEvent> moves = EventStreams.eventsOf(this, MouseEvent.MOUSE_MOVED);
        EventStream<Point2D> mouseCoordinates = moves.map(event -> new Point2D(event.getX(), event.getY()));
        subscription.and(mouseCoordinates.subscribe(point -> mousePosition = point));

        EventStream<MouseEvent> enter = EventStreams.eventsOf(this, MouseEvent.MOUSE_ENTERED);
        EventStream<Boolean> mouseEnter = enter.map(event -> true);
        subscription.and(mouseEnter.subscribe(value -> active = value));

        EventStream<MouseEvent> out = EventStreams.eventsOf(this, MouseEvent.MOUSE_EXITED);
        EventStream<Boolean> mouseOut = out.map(event -> false);
        subscription.and(mouseOut.subscribe(value -> active = value));

        EventStream<MouseEvent> drag = EventStreams.eventsOf(this, MouseEvent.MOUSE_DRAGGED);
        pressed = EventStreams.eventsOf(this, MouseEvent.MOUSE_PRESSED);
        mouseDragged = drag.map(event -> new Point2D(event.getX(), event.getY()));
        released = EventStreams.eventsOf(this, MouseEvent.MOUSE_RELEASED);

        EventStream<MouseEvent> clicks = EventStreams.eventsOf(this, MouseEvent.MOUSE_CLICKED);

        EventStream<ScrollEvent> scrollUp = EventStreams.eventsOf(this, ScrollEvent.SCROLL);

        scrollUp.subscribe(scrollEvent -> {
            String eventName = scrollEvent.getDeltaY() > 0 ? "media.rewind.alt" : "media.forward.alt";
            router.publishEvent(eventName);
        });

        clicks.subscribe(event -> {
            if(event.isControlDown()){
                Node node = event.getPickResult().getIntersectedNode();
                Parent parent = node.getParent();
                if(parent instanceof IconPointColorized) {
                    this.router.publishEvent("icon.deleted", Arrays.asList(parent));
                }
            } else if (event.isAltDown()){
                if (event.getButton() == MouseButton.PRIMARY){
                    this.router.publishEvent("media.rewind.alt");
                } else if (event.getButton() == MouseButton.SECONDARY){
                    this.router.publishEvent("media.forward.alt");
                }
            } else if (event.getButton() == MouseButton.PRIMARY){
                this.router.publishEvent("media.rewind.5");
            } else if (event.getButton() == MouseButton.SECONDARY){
                this.router.publishEvent("media.forward.5");
            }
        });
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

    public Point2D getMousePosition(){
        if(active){
            return mousePosition;
        } else{
            return null;
        }
    }
}