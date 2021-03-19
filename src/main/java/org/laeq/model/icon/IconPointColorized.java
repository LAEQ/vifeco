package org.laeq.model.icon;


import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import org.laeq.model.Category;


public class IconPointColorized extends IconPoint {
    private final Circle circleOver;
    private final Circle circle2;
    private final IconSize iconSize2;
    private final EventHandler<MouseEvent> mouseEnter;
    private final EventHandler<MouseEvent> mouseExit;
    private boolean isColorized = false;
    private Duration start;

    public IconPointColorized(IconSize iconSize, Duration start) {
        super(iconSize);
        this.start = start;
        circle2 = generateCircle();
        circle2.setFill(Paint.valueOf(iconSize.category.getColor()));
        circleOver = generateCircle();
        circleOver.setFill(Color.WHITE);
        circleOver.setOpacity(0);

        Category c = new Category();
        c.setName(iconSize.category.getName());
        c.setIcon(iconSize.category.getIcon());
        c.setColor(Color.WHITE.toString());

        iconSize2 = new IconSize(c, iconSize.size);
        this.iconSize2.decorate();

        mouseEnter = event -> colorize();
        mouseExit = event -> reset();

        listener();
    }

    public boolean obsolete(Duration now){
        return start.lessThan(now);
    }

    @Override
    public void clear(){
        removeListener();
        this.iconSize.clear();
        getChildren().clear();
    }

    @Override
    public void decorate(){
        this.iconSize.decorate();
        getChildren().addAll(this.circle, this.iconSize, this.circleOver);
    }

    private void removeListener(){
        removeEventHandler(MouseEvent.MOUSE_ENTERED, mouseEnter);
        removeEventHandler(MouseEvent.MOUSE_EXITED, mouseExit);
    }

    public void listener(){
        setOnMouseEntered(mouseEnter);
        setOnMouseExited(mouseExit);
    }

    public void colorize(){
        if(!isColorized){
            getChildren().removeAll(this.circle, this.iconSize);
            getChildren().add(0, this.circle2);
            getChildren().add(1, this.iconSize2);
            isColorized = true;
        }
    }

    public void reset(){
        if(isColorized){
            getChildren().removeAll(this.circle2, this.iconSize2);
            getChildren().add(0, this.circle);
            getChildren().add(1, this.iconSize);
            isColorized = false;
        }
    }


}
