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