package org.laeq.graphic.icon;

import javafx.geometry.Point2D;
import javafx.scene.Group;

public abstract class IconAbstract extends Group {
    public abstract void setPosition(Point2D point);
    public abstract void setText(String s);
    public abstract void colorize(String colorStroke, String colorBg);
    public abstract void reset();
}

