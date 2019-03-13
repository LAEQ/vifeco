package org.laeq.model;

import javafx.scene.Group;
import javafx.scene.paint.Paint;
import javafx.scene.shape.SVGPath;

abstract class BaseIcon extends Group implements IconInterface {
    private final Category category;
    private final SVGPath svgPath;


    /**
     * Constructs a group.
     */
    public BaseIcon(Category category) {
        this.category = category;
        this.svgPath = new SVGPath();
        this.svgPath.setContent(category.getIcon());
        this.svgPath.setFill(Paint.valueOf(this.category.getColor()));
    }



    @Override
    public void decorate() {



    }
}
