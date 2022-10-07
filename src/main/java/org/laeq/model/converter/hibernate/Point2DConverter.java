package org.laeq.model.converter.hibernate;

import javafx.geometry.Point2D;

import javax.persistence.AttributeConverter;

public class Point2DConverter implements AttributeConverter<Point2D, String> {
    @Override
    public String convertToDatabaseColumn(Point2D value) {
        return String.format("%f;%f", value.getX(), value.getY());
    }

    @Override
    public Point2D convertToEntityAttribute(String value) {
        final String[] split = value.split(";");
        return new Point2D(Double.parseDouble(split[0]), Double.parseDouble(split[1]));
    }
}
