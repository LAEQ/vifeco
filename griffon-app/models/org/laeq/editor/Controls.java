package org.laeq.editor;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.util.Duration;

public class Controls {
    public Double[] speedValue = new Double[]{0.25, 2.0};
    public Double[] opacityValue = new Double[]{.1, 1.0};
    public Double[] sizeValue = new Double[]{10d, 80d};
    public Double[] durationValue = new Double[]{1d, 30d};

    public SimpleDoubleProperty duration = new SimpleDoubleProperty(10);
    public SimpleDoubleProperty speed = new SimpleDoubleProperty(1);
    public SimpleDoubleProperty size = new SimpleDoubleProperty(50);
    public SimpleDoubleProperty opacity = new SimpleDoubleProperty(.7);

    public Double scale(){ return size.getValue() / 100 ;}
    public Duration display(){
        return Duration.seconds(duration.get());
    }
}
