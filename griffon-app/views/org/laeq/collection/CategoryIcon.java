package org.laeq.collection;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import org.laeq.model.Category;
import org.laeq.model.Icon;

public class CategoryIcon extends Group {

    private double width = 50d;
    private double height = 60d;
    private Text text;
    private Icon icon;

    public CategoryIcon(Category category) {
        icon = new Icon(category.getIcon(), "black");
        icon.setLayoutY(5);
        icon.setLayoutX(5);
        text = new Text(category.getName());
        text.setFont(Font.font("Sans"));
        text.setSmooth(true);
        text.setLayoutX(40);
        text.setLayoutY(22);

        width += text.getLayoutBounds().getWidth();

        Rectangle rectangle = new Rectangle(0, 0, 100, 30);
        rectangle.setStroke(Color.LIGHTGRAY);
        rectangle.setStrokeWidth(1);
        rectangle.setFill(Color.WHITE);

        getChildren().addAll(rectangle, icon, text);
    }


    public CategoryIcon(Category category, int size) {
        width = 170;

        icon = new Icon(category.getIcon(), "black", size);
        icon.setLayoutY(height / 2 - icon.getLayoutBounds().getHeight() / 2 + 6);
        icon.setLayoutX(25);
        text = new Text(category.getName());
        text.setFont(Font.font("Sans", 30));
        text.setSmooth(true);
        text.setLayoutX(80);
        text.setLayoutY(height / 2 + 9 );

        Rectangle rectangle = new Rectangle(0, 0, width, height);
        rectangle.setStroke(Color.LIGHTGRAY);
        rectangle.setStrokeWidth(1);
        rectangle.setFill(Color.WHITE);

        getChildren().addAll(rectangle, icon, text);
    }
    public double getWidth(){
        return width;
    }

    public void setText(String s) {
        text.setText(s);
    }
}
