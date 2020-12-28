package org.laeq.editor;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.util.Duration;

public class Controls {
    public SimpleIntegerProperty displayDuration = new SimpleIntegerProperty(10);
    public SimpleDoubleProperty speed = new SimpleDoubleProperty(1);
    public SimpleIntegerProperty size = new SimpleIntegerProperty(20);
    public SimpleDoubleProperty opacity = new SimpleDoubleProperty(.5);


    public Duration display(){
        return Duration.seconds(displayDuration.get());
    }
}
