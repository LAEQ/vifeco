package org.laeq.graphic.icon;

import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import org.laeq.model.Point;

public class TimelineIcon extends Group {
    private int identifier;
    private final Label label;
    private final Circle circle;

    /**
     * Creates an empty instance of Circle.
     */
    public TimelineIcon(double x, double y, double size, Point point) {
        this.identifier = point.getId();
        circle = new Circle(x, y, size);

        label = new Label(point.getCategory().getShortcut());

        getChildren().addAll(circle, label);
    }

    public int getIdentifier() {
        return identifier;
    }
}
