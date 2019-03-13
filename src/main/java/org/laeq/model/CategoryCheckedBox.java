package org.laeq.model;

import javafx.scene.Group;
import javafx.scene.control.CheckBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.laeq.model.Category;
import org.laeq.model.Icon;

public class CategoryCheckedBox extends Group {
        private double width = 50;
        private CheckBox box;

        public CategoryCheckedBox(Category category) {
            Icon icon = new Icon(category.getIcon(), category.getColor());
            icon.setLayoutY(5);
            icon.setLayoutX(5);

            box = new CheckBox(category.getName());
            box.setLayoutX(width);

            width += 150;

            Rectangle rectangle = new Rectangle(0, 0, 45, 30);
            rectangle.setStroke(Color.LIGHTGRAY);
            rectangle.setStrokeWidth(1);
            rectangle.setFill(Color.WHITE);

            getChildren().addAll(rectangle, icon, box);
    }

    public CheckBox getBox() {
        return box;
    }

    public double getWidth(){
        return width;
    }
}
