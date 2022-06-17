package org.laeq.editor;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.util.Duration;

public final class Controls {
    public final Double[] speedValue = new Double[]{0.1d, 10.0};
    public final Double[] opacityValue = new Double[]{.1, 1.0};
    public final Double[] sizeValue = new Double[]{10d, 80d};
    public final Double[] durationValue = new Double[]{0.1d, 30d};
    public final Double[] volumeValue = new Double[]{0d, 1.0};

    public final SimpleDoubleProperty duration = new SimpleDoubleProperty(3);
    public final SimpleDoubleProperty speed = new SimpleDoubleProperty(1);
    public final SimpleDoubleProperty size = new SimpleDoubleProperty(50);
    public final SimpleDoubleProperty opacity = new SimpleDoubleProperty(.7);
    public final SimpleDoubleProperty volume = new SimpleDoubleProperty(.6);

    public final Double scale(){ return size.getValue() / 100 ;}
    public final Duration display(){
        return Duration.seconds(duration.get());
    }

    public final void speedUp() {
        if(speed.doubleValue() <= speedValue[1] - .1){
            speed.set(speed.doubleValue() + .1);
        }
    }

    public final void speedDown() {
        if(speed.doubleValue() >= speedValue[0] + .1){
            speed.set(speed.doubleValue() - .1);
        }
    }
}
