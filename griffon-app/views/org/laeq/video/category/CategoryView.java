package org.laeq.video.category;

import griffon.core.artifact.GriffonView;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;
import org.laeq.VifecoView;
import org.laeq.model.Category;
import org.laeq.video.CategoryController;
import org.laeq.video.CategoryModel;

import javax.annotation.Nonnull;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;

@ArtifactProviderFor(GriffonView.class)
public class CategoryView extends AbstractJavaFXGriffonView {
    private CategoryController controller;
    private CategoryModel model;


    @MVCMember @Nonnull
    private VifecoView parentView;

    @FXML private Pane categoryPane;

    @MVCMember
    public void setController(@Nonnull CategoryController controller) {
        this.controller = controller;
    }

    @MVCMember
    public void setModel(@Nonnull CategoryModel model) {
        this.model = model;
    }

    @Override
    public void initUI() {
        Node node = loadFromFXML();

        String[] icons = new String[]{
                "icons/truck-mvt-blk.png",
                "icons/truck-mvt-red.png",
                "icons/icon-bicycle-mvt-64.png",
                "icons/icon-car-co2-black-64.png",
                "icons/icon-constr-black-64.png",
                "icons/iconmonstr-car-23-64.png",
                "icons/icon-car-elec-black-64.png",
        };


        HashMap<Category, CategoryGroup> categoryList = new HashMap<>();
        try {
            for (int i = 0; i < icons.length; i++) {
                String name = icons[i].substring(6, icons[i].lastIndexOf('.') - 1);
                String path = getApplication().getResourceHandler().getResourceAsURL(icons[i]).getPath();
                Category category = new Category(name, path, "1");

                CategoryGroup group = new CategoryGroup(path);
                group.setLayoutX(10);
                group.setLayoutY(65 * i);

                categoryList.put(category, group);

            }
        } catch (FileNotFoundException e){
            getLog().error(e.getMessage());
        }


        categoryPane.getChildren().addAll(categoryList.values());

        model.generateProperties(categoryList.keySet());

        categoryList.forEach((k, v) -> {
            v.getLabel().textProperty().bind(model.getCategoryProperty(k).asString());
        });

        parentView.getMiddlePane().getItems().add(node);
    }


    private Group generateCategory(String filePath) throws FileNotFoundException {
        Group group = new Group();

        String path = getApplication().getResourceHandler().getResourceAsURL(filePath).getPath();

        FileInputStream inputStream = new FileInputStream(path);
        Image image = new Image(inputStream);
        ImageView imageView = new ImageView(image);
        imageView.setX(0);
        imageView.setY(0);
        imageView.setPreserveRatio(true);
        imageView.setScaleX(0.6);
        imageView.setScaleY(0.6);


        Label label = new Label("12 / 123");
        label.setFont(new Font("Arial", 15));
        label.setLayoutX(76);
        label.setLayoutY(22);
        label.setPrefHeight(17);
        label.setPrefWidth(124);

        group.getChildren().addAll(imageView, label);

        return group;
    }
}
