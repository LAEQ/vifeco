package org.laeq.model.icon;

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import org.laeq.model.Category;


public class IconButton extends Group implements IconDecorator {
    protected final int size;
    protected final IconSquare iconOff;
    protected final IconSquare iconOn;
    protected final Rectangle bgOff;
    protected final Rectangle bgOn;
    protected final Rectangle bgHover;

    private final EventHandler<MouseEvent> mouseEnter;
    private final EventHandler<MouseEvent> mouseExited;
    protected Point2D point;

    public IconButton(IconSquare iconSquare, int size) {
        this.size = size;
        this.iconOff = iconSquare;

        Category category2 = new Category();
        category2.setIcon(this.iconOff.category.getIcon());
        category2.setColor(Color.white);

        this.iconOn = new IconSquare(category2);
        this.iconOn.decorate();
        double x = (this.size - this.iconOn.getLayoutBounds().getWidth()) / 2;
        double y = (this.size - this.iconOn.getLayoutBounds().getHeight()) / 2;
        this.iconOn.position(new Point2D(x, y));

        bgOff = generateRectangle();
        bgOff.setFill(Paint.valueOf(Color.white));

        bgOn = generateRectangle();
        bgOn.setFill(Paint.valueOf(Color.cyan));

        bgHover = generateRectangle();
        bgHover.setFill(Paint.valueOf(Color.white));
        bgHover.setOpacity(0);

        mouseEnter = event -> {
            getChildren().removeAll(this.iconOff, this.bgOff);
            getChildren().add(0, this.bgOn);
            getChildren().add(1, this.iconOn);
        };

        mouseExited = event -> {
            getChildren().removeAll(this.bgOn, this.iconOn);
            getChildren().add(0, this.bgOff);
            getChildren().add(1, this.iconOff);
        };

        listen();
    }

    protected Rectangle generateRectangle(){
        return new Rectangle(0,0, this.size, this.size);
    }

    @Override
    public void decorate() {
        this.iconOff.decorate();

        double x = (this.size - this.iconOff.getLayoutBounds().getWidth()) / 2;
        double y = (this.size - this.iconOff.getLayoutBounds().getHeight()) / 2;
        this.iconOff.position(new Point2D(x, y));

        getChildren().addAll(this.bgOff, this.iconOff, this.bgHover);
    }

    @Override
    public void clear() {
        //noop
    }

    @Override
    public void position(Point2D point) {
        //noop
    }

    public void listen(){
        setOnMouseEntered(mouseEnter);
        setOnMouseExited(mouseExited);
    }
}
