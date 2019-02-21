package org.laeq.category;

import griffon.core.artifact.GriffonView;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;
import org.kordamp.ikonli.fontawesome.FontAwesome;
import org.kordamp.ikonli.javafx.FontIcon;
import org.laeq.model.Icon;
import org.laeq.video.VideoListView;

import javax.annotation.Nonnull;

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

    @FXML private Button saveActionTarget;

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

        model.nameProperty().bindBidirectional(nameField.textProperty());
        model.shortcutProperty().bindBidirectional(shortcutField.textProperty());
        model.iconProperty().bindBidirectional(filePath.textProperty());
    }

    public void disableSave(Boolean value){
        saveActionTarget.setDisable(value);
    }

    public void clearIcon(){
        iconView.getChildren().clear();
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
