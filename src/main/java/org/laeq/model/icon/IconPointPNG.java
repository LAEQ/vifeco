package org.laeq.model.icon;

import javafx.scene.Group;
import javafx.scene.image.ImageView;

public class IconPointPNG extends Group {
    private final ImageView imageOff;
    private final ImageView imageOn;
    public IconPointPNG(ImageView imageOff, ImageView imageOn){
        this.imageOff = imageOff;
        this.imageOn = imageOn;

        double x = this.imageOff.getImage().getWidth() / 2;
        double y = this.imageOff.getImage().getHeight() / 2;

        this.imageOff.setLayoutX( - x);
        this.imageOff.setLayoutY( - y);
        this.imageOn.setLayoutX( - x);
        this.imageOn.setLayoutY( - y);

        getChildren().add(this.imageOff);

        setOnMouseEntered(event -> colorize());
        setOnMouseExited(event -> reset());
    }

    public void reset(){
        getChildren().clear();
        getChildren().add(this.imageOff);
    }

    public void colorize(){
        getChildren().clear();
        getChildren().add(this.imageOn);
    }


}
