package org.laeq.model.icon;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public class IconCounter extends Group implements IconDecorator {
    protected final IconSize iconSize;
    protected final Label label;
    protected final Rectangle rectangle;
    protected final double width;
    protected final double height;

    /**
     * Constructs a group.
     */
    public IconCounter(IconSize iconSize, double width, double height) {
        this.width = width;
        this.height = height;
        this.iconSize = iconSize;
        this.rectangle = new Rectangle(0 ,0, this.width, this.height);
        this.rectangle.setFill(Color.WHITE);
        this.label = new Label("-");
        this.label.setFont(new Font("sans", 25));
        this.label.setLayoutX(95);
        this.label.setLayoutY(height / 2 - 15);
    }

    public void setText(String text){
        this.label.setText(text);
    }

    @Override
    public void decorate() {
        this.iconSize.decorate();
        this.iconSize.setLayoutY(this.height / 2 );
        this.iconSize.setLayoutX(40);
        getChildren().addAll(this.rectangle, this.iconSize, this.label);
    }

    @Override
    public void clear() {

    }

    @Override
    public void position(Point2D point) {
        this.setLayoutX(point.getX());
        this.setLayoutY(point.getY());
    }

    public void reset() {
        this.setText("-");
    }
}