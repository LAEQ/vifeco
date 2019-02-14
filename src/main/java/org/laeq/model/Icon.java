package org.laeq.model;


import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Icon extends Group implements CategoryIcon{
    private String fillColor = "#EEEEEE";
    private String imagePath;
    private double width;
    private double height;
    private double opacity;
    private double x;
    private double y;

    public Icon(double size, double opacity, String imagePath) throws FileNotFoundException {
        this.width = size;
        this.height = size;
        this.opacity = opacity;
        this.imagePath = imagePath;

        Canvas canvas = new Canvas(width, height);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.valueOf(fillColor));
        gc.fillOval(0,0, width, height);
        canvas.setOpacity(this.opacity);

        getChildren().add(canvas);

        FileInputStream inputStream = new FileInputStream(imagePath);
        Image image = new Image(inputStream);
        ImageView imageView = new ImageView(image);
        imageView.setOpacity(opacity);
        imageView.setX((width- image.getWidth()) / 2);
        imageView.setY((height - image.getHeight()) / 2);

        getChildren().add(imageView);
    }

    public Icon(double size, double opacity) {
        this.width = size;
        this.height = size;
        this.opacity = opacity;
        this.imagePath = imagePath;

        Canvas canvas = new Canvas(width, height);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.valueOf(fillColor));
        gc.fillOval(0,0, width, height);
        canvas.setOpacity(this.opacity);

        getChildren().add(canvas);

        Text label = new Text("?");
        label.setFont(Font.font(18));

        getChildren().add(label);
    }

    public void setBounds(Bounds bounds){
        this.setLayoutX(getIconX(bounds));
        this.setLayoutY(getIconY(bounds));
    }

    private double getIconX(Bounds bounds){
        return this.x * bounds.getWidth() - (this.getWidth() / 2);
    }
    private double getIconY(Bounds bounds){
        return this.y * bounds.getHeight() - (this.getHeight() / 2);
    }

    public void setX(double x) {
        this.x = x;
    }
    public void setY(double y) {
        this.y = y;
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
