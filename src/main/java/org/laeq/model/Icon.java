package org.laeq.model;


import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Icon extends Group {
    private double size;
    private final String path;
    private final String color;
    private final SVGPath svg;
    private final float svgRatio;

    public Icon(Category category, double size)  {
        this.path = category.getIcon();
        this.size = size;
        this.color = category.getColor();
        this.svg = new SVGPath();
        this.svgRatio = 0.50f;
        svg.setContent(path);
        svg.setLayoutX(getX());
        svg.setLayoutY(getY());
        svg.setScaleX(getScale());
        svg.setScaleY(getScale());

        getChildren().addAll(getCanvas(), svg);
    }

    private Canvas getCanvas(){
        Canvas canvas = new Canvas(size, size);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.valueOf("#efefef"));
        gc.fillOval(0,0, size, size);
        canvas.setOpacity(1);

        return canvas;
    }

    private double getScale(){
        return svgRatio / (svg.getLayoutBounds().getHeight() / size);
    }

    private double getX(){
        return size / 2 - svg.getLayoutBounds().getWidth() / 2;
    }

    public double getY(){
        return size / 2 - svg.getLayoutBounds().getHeight() / 2;
    }

    public void setPath(String s) {
        this.svg.setContent(s);
    }

    public void setSize(double s){
        this.size = s;
    }
}
