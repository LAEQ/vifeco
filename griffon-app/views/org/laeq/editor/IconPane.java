package org.laeq.editor;

import griffon.core.event.EventRouter;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import org.laeq.model.icon.IconPointColorized;
import org.reactfx.EventStream;
import org.reactfx.EventStreams;
import org.reactfx.Subscription;

import java.util.Arrays;


public class IconPane extends Pane {
    EventRouter router;
    Subscription subscription = () -> {};

    public IconPane() {
        EventStream<MouseEvent> clicks = EventStreams.eventsOf(this, MouseEvent.MOUSE_CLICKED);

        manageSubscription(clicks.subscribe(event -> {
            if(event.isControlDown()){
                Node node = event.getPickResult().getIntersectedNode();
                Parent parent = node.getParent();
                if(parent instanceof IconPointColorized) {
                    router.publishEventOutsideUI("icon.delete", Arrays.asList(parent));
                }
            } else if (event.getButton() == MouseButton.PRIMARY){
                router.publishEventOutsideUI("player.rewind.5");
            } else if (event.getButton() == MouseButton.SECONDARY){
                router.publishEventOutsideUI("player.forward.5");
            }
        }));

        EventStream<MouseEvent> moves = EventStreams.eventsOf(this, MouseEvent.MOUSE_MOVED);
        EventStream<Point2D> mouseCoordinates = moves.map(event -> new Point2D(event.getX(), event.getY()));
        manageSubscription(mouseCoordinates.subscribe(point -> router.publishEventOutsideUI("mouse.position", Arrays.asList(point))));

//        EventStream<ScrollEvent> scroll = EventStreams.eventsOf(this, ScrollEvent.SCROLL);
//        manageSubscription(scroll.subscribe(scrollEvent -> {
//            String eventName = scrollEvent.getDeltaY() >= 0 ? "speed.up" : "speed.down";
//            router.publishEventOutsideUI(eventName);
//        }));
    }

    void manageSubscription(Subscription other) {
        subscription = subscription.and(other);
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