package org.laeq.model.icon;

import javafx.geometry.Point2D;

interface IconDecorator {
    void decorate();
    void position(Point2D point);
}
