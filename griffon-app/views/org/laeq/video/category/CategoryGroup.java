package org.laeq.video.category;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import org.laeq.model.Category;
import org.laeq.model.Icon;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class CategoryGroup extends AnchorPane {

    private String filePath;
    private Text textLabel;

    private String fillColor = "#aaaaaa";
    private Category category;

    /**
     * Creates an AnchorPane layout.
     */
    public CategoryGroup(Category category, int size) {
        this.category = category;

        Pane background = new Pane();

        setLeftAnchor(background, 10d);
        setRightAnchor(background, 10d);

        textLabel = new Text("12 / 123");
        textLabel.setFont(new Font("Arial", 23));
        textLabel.setLayoutY(40);
        textLabel.setTextAlignment(TextAlignment.RIGHT);

        setRightAnchor(textLabel, 15d);

        Icon icon = new Icon(category, size);

        getChildren().addAll(background, icon, textLabel);
    }

    public Text getTextLabel() {
        return textLabel;
    }
}