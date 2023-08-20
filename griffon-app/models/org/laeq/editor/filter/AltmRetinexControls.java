package org.laeq.editor.filter;

import javafx.beans.property.SimpleDoubleProperty;

public class AltmRetinexControls {
    public final Double[] rParamValue = new Double[]{0.1d, 1.0};
    public final Double[] gParamValue = new Double[]{0.1d, 1.0};
    public final Double[] bParamValue = new Double[]{0.1d, 1.0};

    public final SimpleDoubleProperty rParam = new SimpleDoubleProperty(.5);
    public final SimpleDoubleProperty gParam = new SimpleDoubleProperty(.5);
    public final SimpleDoubleProperty bParam = new SimpleDoubleProperty(.5);
}
