package org.laeq.graphic.icon;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import org.laeq.model.Point;

public class TimelineIcon extends Circle {
    private int identifier;

    /**
     * Creates an empty instance of Circle.
     */
    public TimelineIcon(double x, double y, double size, Point point) {
        super(x, y, size);
        this.identifier = point.getId();
        this.setFill(Paint.valueOf(point.getCategory().getColor()));
    }

    public int getIdentifier() {
        return identifier;
    }
}
