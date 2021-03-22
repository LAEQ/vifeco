package org.laeq.editor;

import griffon.core.event.EventRouter;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import org.reactfx.EventStream;
import org.reactfx.EventStreams;
import org.reactfx.Subscription;
import org.reactfx.util.Tuple2;

import java.util.Arrays;


public class VideoSlider extends Slider {
    private EventStream<MouseEvent> clicks;
    private EventStream<MouseEvent> pressed;

    EventRouter router;
    Subscription subscription = () -> {};

    public VideoSlider() {
        clicks = EventStreams.eventsOf(this, MouseEvent.MOUSE_RELEASED);
        clicks.subscribe(parent -> {
            router.publishEventAsync("slider.release", Arrays.asList(this.valueProperty().get()));
            dispose();
        });

        pressed = EventStreams.eventsOf(this, MouseEvent.MOUSE_PRESSED);
        pressed.subscribe( pressedValue -> {
            router.publishEventAsync("slider.pressed"); 
            subscribe();
        });

    }

    public VideoSlider(double min, double max, double value) {
        super(min, max, value);
    }

    void subscribe(){
        EventStream<Number> value = EventStreams.valuesOf(this.valueProperty());
        EventStream<MouseEvent> mouseEventEventStream = EventStreams.eventsOf(this, MouseEvent.MOUSE_PRESSED);
        EventStream<Tuple2<MouseEvent, Number>> combine = EventStreams.combine(mouseEventEventStream, value);
        manageSubscription(combine.subscribe( tuple -> {
            router.publishEventAsync("slider.currentTime", Arrays.asList(tuple._2));
        }));
    }

    void manageSubscription(Subscription other) {
        subscription = subscription.and(other);
    }

    public void setEventRouter(EventRouter eventRouter) {
        this.router = eventRouter;
    }

    void dispose() {
        subscription.unsubscribe();
    }
}
