package org.laeq.editor;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.util.Duration;

public class Controls {
    public Double[] speedValue = new Double[]{0.25d, 10.0};
    public Double[] opacityValue = new Double[]{.1, 1.0};
    public Double[] sizeValue = new Double[]{10d, 80d};
    public Double[] durationValue = new Double[]{1d, 30d};
    public Double[] volumeValue = new Double[]{0d, 1.0};

    public SimpleDoubleProperty duration = new SimpleDoubleProperty(10);
    public SimpleDoubleProperty speed = new SimpleDoubleProperty(1);
    public SimpleDoubleProperty size = new SimpleDoubleProperty(50);
    public SimpleDoubleProperty opacity = new SimpleDoubleProperty(.7);
    public SimpleDoubleProperty volume = new SimpleDoubleProperty(.6);

    public Double scale(){ return size.getValue() / 100 ;}
    public Duration display(){
        return Duration.seconds(duration.get());
    }

    public void speedUp() {
        if(speed.doubleValue() <= speedValue[1] - .25){
            speed.set(speed.doubleValue() + .25);
        }
    }

    public void speedDown() {
        if(speed.doubleValue() >= speedValue[0] + .25){
            speed.set(speed.doubleValue() - .25);
        }
    }
}
