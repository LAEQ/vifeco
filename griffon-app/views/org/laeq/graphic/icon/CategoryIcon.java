package org.laeq.graphic.icon;


import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import org.laeq.graphic.Color;
import org.laeq.model.Category;
import org.laeq.model.Icon;

public class CategoryIcon extends IconAbstract {
    private double width = 50d;
    private double height = 60d;
    private Label label;
    private Icon icon;
    private Category category;
    private Rectangle rectangle;

    public CategoryIcon(Category category, double width, double height) {
        this.category = category;
        icon = new Icon(category.getIcon(), category.getColor());
        icon.setLayoutY(5);
        icon.setLayoutX(5);
        label = new Label(truncate(category.getName()));
        label.setLayoutX(35);
        label.setLayoutY(8);

        rectangle = new Rectangle(0,0,width, height);
        rectangle.setFill(Paint.valueOf(Color.light));
        rectangle.setOpacity(0.5);

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

    @Override
    public void colorize(String borderColor, String bgColor){
        rectangle.setFill(Paint.valueOf(bgColor));
        rectangle.setStroke(Paint.valueOf(borderColor));
    }

    @Override
    public void reset() {

    }
}