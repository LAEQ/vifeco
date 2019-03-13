package org.laeq.model.icon;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class IconPoint extends Group implements IconDecorator {
    protected final IconSize iconSize;
    protected final Circle circle;
    protected Point2D point;

    public IconPoint(IconSize iconSize) {
        this.iconSize = iconSize;
        circle = generateCircle();
        circle.setFill(Color.WHITE);
    }

    protected Circle generateCircle(){
        return new Circle(0, 0, this.iconSize.size / 2);
    }

    @Override
    public void decorate() {
        this.iconSize.decorate();
        getChildren().addAll(this.circle, this.iconSize);
    }

    @Override
    public void position(Point2D point) {
        this.point = point;
        setLayoutX(point.getX());
        setLayoutY(point.getY());
    }
}

