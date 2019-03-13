package org.laeq.model.icon;


import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class IconDescriptor extends Group implements IconDecorator {
    protected final IconSize iconSize;
    protected final Label label;
    protected final Rectangle rectangle;

    /**
     * Constructs a group.
     */
    public IconDescriptor(IconSize iconSize) {
        this.iconSize = iconSize;
        this.rectangle = new Rectangle(-10 ,-10, 180, 40);
        this.rectangle.setFill(Color.LIGHTGRAY);
        this.label = new Label(iconSize.category.getName());
        this.label.setStyle("-fx-font-family: sans; -fx-font-size: 13px;");
        this.label.setLayoutX(this.iconSize.size);
        this.label.setLayoutY(2);
    }

    @Override
    public void decorate() {
        this.iconSize.decorate();
        this.iconSize.setLayoutX(this.iconSize.x);
        this.iconSize.setLayoutY(this.iconSize.y);
        getChildren().addAll(this.rectangle, this.iconSize, this.label);
    }

    @Override
    public void position(Point2D point) {
        this.setLayoutX(point.getX());
        this.setLayoutY(point.getY());
    }
}
