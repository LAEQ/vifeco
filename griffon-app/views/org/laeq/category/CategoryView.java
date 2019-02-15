package org.laeq.category;

import griffon.core.artifact.GriffonView;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;
import org.kordamp.ikonli.fontawesome.FontAwesome;
import org.kordamp.ikonli.javafx.FontIcon;
import org.laeq.VifecoView;
import org.laeq.model.Icon;
import org.laeq.video.VideoListView;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import javax.annotation.Nonnull;
import javax.imageio.ImageIO;

@ArtifactProviderFor(GriffonView.class)
public class CategoryView extends AbstractJavaFXGriffonView {
    private CategoryController controller;
    private CategoryModel model;
    @FXML private TextField nameField;
    @FXML private TextField shortcutField;
    @FXML private Text filePath;
    @FXML private Pane iconView;

    @MVCMember @Nonnull private VideoListView parentView;

    private Tab tab;
    private TabPane parentPane;
    private Icon icon;

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
        parentPane = parentView.getTabPane();

        tab = new Tab();
        tab.setGraphic(new FontIcon(FontAwesome.PLUS_SQUARE));
        tab.setText("New Category");
        tab.setId("Create new category");
        tab.setContent(node);
        tab.setClosable(true);

        tab.setOnClosed(closeTab());

        connectActions(node, controller);

        parentPane.getTabs().add(tab);
        parentPane.getSelectionModel().select(tab);
        System.out.println(model);
        System.out.println(nameField);
        model.nameProperty().bindBidirectional(nameField.textProperty());
        model.shortcutProperty().bindBidirectional(shortcutField.textProperty());
        model.iconProperty().bindBidirectional(filePath.textProperty());
    }

    @Override
    public void mvcGroupDestroy(){
        runInsideUISync(()-> {
            parentPane.getTabs().remove(tab);
        });
    }

    private EventHandler<Event> closeTab(){
        return event -> controller.closeTab();
    }

    public void generateIcon() {
        try {
            iconView.getChildren().clear();
            Image image = new Image(model.iconProperty().getValue());
            ImageView view = new ImageView(image);
            iconView.getChildren().add(view);
        } catch (Exception e) {
            getLog().error("Generate icon: " + e.getMessage());
        }
    }
}
