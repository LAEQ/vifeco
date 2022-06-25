package org.laeq.editor;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.util.Duration;

public final class ImageControls {
    public final Double[] brightnessValue = new Double[]{0.0, 1.0};
    public final Double[] saturationValue = new Double[]{0.0, 1.0};


    public final SimpleDoubleProperty brightness = new SimpleDoubleProperty(1);
    public final SimpleDoubleProperty saturation = new SimpleDoubleProperty(1);


}
