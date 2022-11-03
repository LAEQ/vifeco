package org.laeq.model.converter.jackson;

import com.fasterxml.jackson.databind.util.StdConverter;
import javafx.geometry.Point2D;

public class Point2DConverterSerialize extends StdConverter<Point2D, String> {
    @Override
    public String convert(Point2D value) {
        return String.format("%f;%f", value.getX(), value.getY());
    }
}
