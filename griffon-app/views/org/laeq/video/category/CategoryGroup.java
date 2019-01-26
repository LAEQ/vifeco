package org.laeq.video.category;

import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Control;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class CategoryGroup extends AnchorPane {

    private String filePath;
    private Text textLabel;

    private String fillColor = "#aaaaaa";


    public CategoryGroup(String filePath) throws FileNotFoundException {
        Pane background = new Pane();



        setLeftAnchor(background, 10d);
        setRightAnchor(background, 10d);

        this.filePath = filePath;

        FileInputStream inputStream = new FileInputStream(filePath);
        Image image = new Image(inputStream);
        ImageView imageView = new ImageView(image);
        imageView.setX(0);
        imageView.setY(0);
        imageView.setPreserveRatio(true);
        imageView.setScaleX(0.6);
        imageView.setScaleY(0.6);


        textLabel = new Text("12 / 123");
        textLabel.setFont(new Font("Arial", 23));
//        textLabel.setLayoutX(76);
        textLabel.setLayoutY(40);
        textLabel.setTextAlignment(TextAlignment.RIGHT);


        setRightAnchor(textLabel, 15d);



        Canvas canvas = new Canvas(63, 63);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.valueOf(fillColor));
        gc.fillOval(3,3, 60, 60);


        getChildren().addAll(background, imageView, textLabel);
    }

    public Text getTextLabel() {
        return textLabel;
    }
}


//<Pane prefHeight="50.0" prefWidth="299.0" style="-fx-border-color: rgb(250,250,250); -fx-background-color: rgb(250,250,250);" AnchorPane.bottomAnchor="0.0" AnchorPane.leftAnchor="10.0" AnchorPane.rightAnchor="10.0" AnchorPane.topAnchor="0.0" />
//<Pane layoutX="217.0" layoutY="3.0" prefHeight="39.0" prefWidth="100.0" style="-fx-background-color: white; -fx-border-color: white;" AnchorPane.bottomAnchor="5.0" AnchorPane.rightAnchor="15.0" AnchorPane.topAnchor="5.0">
//<children>
//<Text layoutX="-1.0" layoutY="36.0" strokeType="OUTSIDE" strokeWidth="0.0" text="150" textAlignment="CENTER" wrappingWidth="101.99998730421066">
//<font>
//<Font size="22.0" />
//</font>
//</Text>
//</children>
//</Pane>
//<Pane layoutX="6.0" layoutY="3.0" prefHeight="42.0" prefWidth="65.0" style="-fx-background-color: white; -fx-border-color: white;" AnchorPane.bottomAnchor="5.0" AnchorPane.leftAnchor="15.0" AnchorPane.topAnchor="5.0">
//<children>
//<ImageView fitHeight="60.0" fitWidth="65.0" pickOnBounds="true" preserveRatio="true" />
//</children>
//</Pane>