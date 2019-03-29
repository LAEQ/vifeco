package org.laeq.model.icon;

import javafx.scene.Group;
import javafx.scene.image.ImageView;

public class IconPointPNG extends Group {
    private final ImageView imageOff;
    private final ImageView imageOn;
    public IconPointPNG(ImageView imageOff, ImageView imageOn){
        this.imageOff = imageOff;
        this.imageOn = imageOn;

        getChildren().add(this.imageOff);

        setOnMouseEntered(event -> colorize());
        setOnMouseExited(event -> reset());
    }

    public void reset(){
        getChildren().add(this.imageOff);
        getChildren().remove(this.imageOn);
    }

    public void colorize(){
        getChildren().add(this.imageOn);
        getChildren().remove(this.imageOff);
    }


}
