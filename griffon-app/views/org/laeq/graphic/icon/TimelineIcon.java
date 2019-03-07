package org.laeq.graphic.icon;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class TimelineIcon extends Circle {
    private int identifier;

    /**
     * Creates an empty instance of Circle.
     */
    public TimelineIcon(double x, double y, double size, int id) {
        super(x, y, size);
        this.identifier = id;
        this.setFill(Color.DARKGRAY);
    }

    public int getIdentifier() {
        return identifier;
    }
}
