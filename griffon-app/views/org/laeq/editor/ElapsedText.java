package org.laeq.editor;

import griffon.core.event.EventRouter;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ElapsedText extends TextField {

    private EventRouter router;

    public ElapsedText() {
        this.focusedProperty().addListener( (observable, oldValue, newValue) -> {
            if(newValue){
                router.publishEventOutsideUI("elapsed.focus.on");
            }
        });

        this.setOnKeyPressed(elapsedKeyPressed());

        this.textProperty().addListener((observable, oldValue, newValue) -> {
            if(validTimeString(newValue) == false){
               router.publishEventOutsideUI("status.error", Arrays.asList("duration.pattern.invalid"));
            }
        });
    }

    private EventHandler<KeyEvent> elapsedKeyPressed(){
        return event -> {
            if( event.getCode() == KeyCode.ENTER ) {
                String time = this.textProperty().get();

                if(validTimeString(time)){
                    String[] split = time.split(":");
                    Double hours = Double.parseDouble(split[0]);
                    Double minutes = Double.parseDouble(split[1]);
                    Double seconds = Double.parseDouble(split[2]);

                    Duration seekDuration = Duration.hours(hours).add(Duration.minutes(minutes)).add(Duration.seconds(seconds));
                    this.setFocusTraversable(false);
                    router.publishEventAsync("elapsed.currentTime", Arrays.asList(seekDuration));
                }
            }
        };
    }

    private boolean validTimeString(String time){
        Pattern pattern = Pattern.compile("[0-9]{1,2}:[0-9]{1,2}:[0-9]{1,2}");
        Matcher matcher = pattern.matcher(time);

        if(matcher.find()){
            return Boolean.TRUE;
        }

        return Boolean.FALSE;
    }

    public ElapsedText(String text) {
        super(text);
    }
    

    public void setEventRouter(EventRouter eventRouter) {
        this.router = eventRouter;
    }


}
