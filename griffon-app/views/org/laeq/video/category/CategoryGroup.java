package org.laeq.video.category;

import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class CategoryGroup extends Group {
    private String filePath;
    private Label label;

    private String fillColor = "#f7f7f7";


    public CategoryGroup(String filePath) throws FileNotFoundException {
        this.filePath = filePath;

        FileInputStream inputStream = new FileInputStream(filePath);
        Image image = new Image(inputStream);
        ImageView imageView = new ImageView(image);
        imageView.setX(0);
        imageView.setY(0);
        imageView.setPreserveRatio(true);
        imageView.setScaleX(0.6);
        imageView.setScaleY(0.6);


        label = new Label("12 / 123");
        label.setFont(new Font("Arial", 15));
        label.setLayoutX(76);
        label.setLayoutY(22);
        label.setPrefHeight(17);
        label.setPrefWidth(124);

        Canvas canvas = new Canvas(63, 63);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.valueOf(fillColor));
        gc.fillOval(3,3, 60, 60);

        getChildren().addAll(canvas, imageView, label);
    }

    public Label getLabel() {
        return label;
    }

    public void setLabel(Label label) {
        this.label = label;
    }
}
