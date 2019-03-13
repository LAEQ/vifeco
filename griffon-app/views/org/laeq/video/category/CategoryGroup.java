package org.laeq.video.category;

import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import org.laeq.model.Category;
import org.laeq.model.icon.IconPoint;
import org.laeq.model.icon.IconSize;

public class CategoryGroup extends AnchorPane {
    private final Text textLabel;
    private final Category category;

    /**
     * Creates an AnchorPane layout.
     */
    public CategoryGroup(Category category, int size) {
        this.category = category;

        Pane background = new Pane();

        setLeftAnchor(background, 10d);
        setRightAnchor(background, 10d);

        Label textShortCut = new Label(category.getShortcut());
        textShortCut.setStyle("-fx-font-family: sans; -fx-fons-size: 9px;");
        textShortCut.setLayoutX(80);
        textShortCut.setLayoutY(30);

        textLabel = new Text("12 / 123");
        textLabel.setFont(new Font("Arial", 23));
        textLabel.setLayoutY(40);
        textLabel.setTextAlignment(TextAlignment.RIGHT);

        setRightAnchor(textLabel, 15d);

        IconPoint iconPoint = new IconPoint(new IconSize(category, size));
        iconPoint.decorate();
        iconPoint.position(new Point2D(size / 2, size / 2));

        getChildren().addAll(background, iconPoint, textShortCut, textLabel);
    }

    public Text getTextLabel() {
        return textLabel;
    }
}