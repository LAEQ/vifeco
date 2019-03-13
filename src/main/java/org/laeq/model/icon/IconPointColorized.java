package org.laeq.model.icon;


import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import org.laeq.model.Category;


public class IconPointColorized extends IconPoint {
    private final Circle circle2;
    private final IconSize iconSize2;
    private final EventHandler<MouseEvent> mouseEnter;
    private final EventHandler<MouseEvent> mouseExit;


    public IconPointColorized(IconSize iconSize) {
        super(iconSize);
        circle2 = generateCircle();
        circle2.setFill(Paint.valueOf(iconSize.category.getColor()));

        Category c = null;
        try {
            c = (Category)(iconSize.category.clone());
            c.setColor(Color.WHITE.toString());
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        iconSize2 = new IconSize(c, iconSize.size);
        this.iconSize2.decorate();

        mouseEnter = event -> colorize();
        mouseExit = event -> reset();

        listen();
    }

    public void listen(){
        setOnMouseEntered(mouseEnter);
        setOnMouseExited(mouseExit);
    }

    public void colorize(){
        getChildren().clear();
        getChildren().addAll(this.circle2, this.iconSize2);
    }

    public void reset(){
        getChildren().clear();
        getChildren().addAll(this.circle, this.iconSize);
    }
}
