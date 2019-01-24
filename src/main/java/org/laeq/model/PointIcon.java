package org.laeq.model;


import javafx.scene.Group;
import java.io.FileNotFoundException;

public class PointIcon extends Group implements CategoryIcon{
    private String imagePath;
    private double width;
    private double height;

    public PointIcon( double width, double height, String imagePath) throws FileNotFoundException {
        this.width = width;
        this.height = height;
        this.imagePath = imagePath;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }
}
