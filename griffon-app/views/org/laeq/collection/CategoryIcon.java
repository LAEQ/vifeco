package org.laeq.collection;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import org.laeq.model.Category;
import org.laeq.model.Icon;


public class CategoryIcon extends Group {
    private double width = 50d;
    private double height = 60d;
    private Label label;
    private Icon icon;
    private Category category;

    public CategoryIcon(Category category, double width, double height) {
        this.category = category;
        icon = new Icon(category.getIcon(), "black");
        icon.setLayoutY(5);
        icon.setLayoutX(5);
        label = new Label(category.getName());
        label.setLayoutX(35);
        label.setLayoutY(8);
        getChildren().addAll(icon, label);
    }


    public CategoryIcon(Category category, int size, double width, double height) {
        this.width = width;
        this.height = height;

        icon = new Icon(category.getIcon(), "black", size);
        icon.setLayoutY(height / 2 - icon.getLayoutBounds().getHeight() / 2 + 6);
        icon.setLayoutX(25);

        label = new Label("-");
        label.setFont(Font.font("sans", 25));
        label.setLayoutX(85);
        label.setLayoutY(height / 2 - 15);


        Rectangle rectangle = new Rectangle(0, 0, width, height);
        rectangle.setStroke(Color.LIGHTGRAY);
        rectangle.setStrokeWidth(0);
        rectangle.setFill(Color.WHITE);

        getChildren().addAll(rectangle, icon, label);
    }

    public double getWidth(){
        return width;
    }


    private String truncate(String name){
        if(name.length() <= 20){
            return name;
        }
        return name.substring(0, 20) + "...";
    }

    public void setText(String s) {
        label.setText(s);
    }

    public void setPosition(Point2D point) {
        setLayoutX(point.getX());
        setLayoutY(point.getY());
    }
}
