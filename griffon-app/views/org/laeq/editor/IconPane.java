package org.laeq.editor;

import griffon.core.event.EventRouter;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import org.reactfx.EventStream;
import org.reactfx.EventStreams;
import org.reactfx.Subscription;
import java.util.Arrays;


public class IconPane extends Pane {
    EventRouter router;
    Subscription subscription = () -> {};

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