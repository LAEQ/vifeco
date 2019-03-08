package org.laeq.video.category;

import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import org.laeq.model.Category;
import org.laeq.model.Icon;

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

        Label textShortCut = new Label(category.getShortcut());
        textShortCut.setStyle("-fx-font-family: sans; -fx-fons-size: 9px;");
        textShortCut.setLayoutX(80);
        textShortCut.setLayoutY(30);

        textLabel = new Text("12 / 123");
        textLabel.setFont(new Font("Arial", 23));
        textLabel.setLayoutY(40);
        textLabel.setTextAlignment(TextAlignment.RIGHT);

        setRightAnchor(textLabel, 15d);

        Icon icon = new Icon(category, size);

        getChildren().addAll(background, icon, textShortCut, textLabel);
    }

    public Text getTextLabel() {
        return textLabel;
    }
}