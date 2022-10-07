package org.laeq.model.converter.jackson;

import com.fasterxml.jackson.databind.util.StdConverter;
import javafx.geometry.Point2D;

public class Point2DConverterDeserialize extends StdConverter<String, Point2D> {
    @Override
    public Point2D convert(String value) {
        final String[] split = value.split(";");
        return new Point2D(Double.parseDouble(split[0]), Double.parseDouble(split[1]));
    }
}
