package org.laeq.model.icon;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.paint.Paint;
import javafx.scene.shape.SVGPath;
import org.laeq.model.Category;


public abstract class BaseIcon extends Group implements IconDecorator {
    protected final Category category;
    protected final SVGPath svgPath;

    /**
     * Constructs a group.
     */
    public BaseIcon(Category category) {
        this.category = category;
        this.svgPath = new SVGPath();
        this.svgPath.setContent(category.getIcon());
        this.svgPath.setFill(Paint.valueOf(category.getColor()));
    }

    @Override
    public void decorate(){
        getChildren().add(this.svgPath);
    }

    public void position(Point2D point) {
        this.setLayoutX(point.getX());
        this.setLayoutY(point.getY());
    }
}
