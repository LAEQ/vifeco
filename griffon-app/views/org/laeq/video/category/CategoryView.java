package org.laeq.video.category;

import griffon.core.artifact.GriffonView;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;
import org.laeq.model.Category;
import org.laeq.video.CategoryController;
import org.laeq.video.CategoryModel;
import org.laeq.video.player.ContainerView;

import javax.annotation.Nonnull;
import java.util.HashMap;

import static javafx.scene.layout.AnchorPane.setLeftAnchor;
import static javafx.scene.layout.AnchorPane.setRightAnchor;

@ArtifactProviderFor(GriffonView.class)
public class CategoryView extends AbstractJavaFXGriffonView {
    @MVCMember @Nonnull private CategoryModel model;
    @MVCMember @Nonnull private CategoryController controller;
    @MVCMember @Nonnull private ContainerView parentView;

    @FXML private Pane categoryPane;
    @FXML private Text totalLabel;

    private Node parent;
    private HashMap<Category, CategoryGroup>  categoryList;

    @Override
    public void initUI() {
        parent = loadFromFXML();
        categoryList = new HashMap<>();

        parentView.getCategoryPane().getChildren().add(parent);
    }

    public void clearView(){
        categoryList.forEach((k, v) -> {
            v.getTextLabel().textProperty().unbind();
        });

        totalLabel.textProperty().unbind();

        ((AnchorPane)parent).getChildren().removeAll(categoryList.values());
        categoryList.clear();
    }

    public void initView() {
        clearView();

        int i = 0;
        for (Category category: model.getCategorySet()) {
            CategoryGroup group = new CategoryGroup(category, 60);
            group.setStyle("-fx-background-color: rgb(250,250,250);");
            group.setLayoutX(10);
            group.setLayoutY(61 * i + 44);
            setLeftAnchor(group, 1d);
            setRightAnchor(group, 1d);
            categoryList.put(category, group);
            ++i;

            group.getPrevBtn().setOnMouseClicked(event -> {
                controller.previousPoint(category);
            });

            group.getNextBtn().setOnMouseClicked(event -> {
                controller.nextPoint(category);
            });
        }

        ((AnchorPane) parent).getChildren().addAll(categoryList.values());

        categoryList.forEach((k, v) -> {
            v.getTextLabel().textProperty().bind(model.getCategoryProperty(k).asString());
        });

        totalLabel.textProperty().bind(model.totalProperty().asString());
    }

}
