package org.laeq.category;

import griffon.core.artifact.GriffonView;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.SetChangeListener;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;
import org.kordamp.ikonli.fontawesome.FontAwesome;
import org.kordamp.ikonli.javafx.FontIcon;
import org.laeq.model.Category;
import org.laeq.video.VideoListView;

import java.util.Collections;
import javax.annotation.Nonnull;

@ArtifactProviderFor(GriffonView.class)
public class CategoryCollectionView extends AbstractJavaFXGriffonView {
    @MVCMember @Nonnull private CategoryCollectionController controller;
    @MVCMember @Nonnull private CategoryCollectionModel model;
    @MVCMember @Nonnull VideoListView parentView;
    @FXML private Group categoryGroup;
    @FXML private TextField nameField;

    private TabPane parentPane;
    private Tab tab;
    private Point2D[] positions;

    @Override
    public void initUI() {
       Node node = loadFromFXML();

        parentPane = parentView.getTabPane();

        tab = new Tab();
        tab.setGraphic(new FontIcon(FontAwesome.PLUS_SQUARE));
        tab.setText("New Category collection");
        tab.setId("category_collection_form");
        tab.setContent(node);
        tab.setClosable(true);

        tab.setOnClosed(closeTab());

        connectActions(node, controller);

        parentPane.getTabs().add(tab);
        parentPane.getSelectionModel().select(tab);

        model.nameProperty().bindBidirectional(nameField.textProperty());

        model.getCategories().addListener(new SetChangeListener<Category>() {
            @Override
            public void onChanged(Change<? extends Category> change) {
                if(change.wasAdded()){
                    CheckBox checkBox = new CheckBox(change.getElementAdded().getName());
                    Point2D point = positions[categoryGroup.getChildren().size()];
                    checkBox.setLayoutX(point.getX());
                    checkBox.setLayoutY(point.getY());
                    categoryGroup.getChildren().add(checkBox);
                    SimpleBooleanProperty sbp = model.addCategory(change.getElementAdded());
                    checkBox.selectedProperty().bindBidirectional(sbp);
                }

                //@todo: change.wasRemoved()
            }
        });

    }

    public void initPositions(int size){
        int x = 0;
        int y = 0;

        Point2D[] points = new Point2D[size];

        for (int i = 0; i < size; i++) {
            points[i] = new Point2D(x, y);

            x += 160;

            if(i % 4 == 0 && i != 0){
                x = 0;
                y += 35;
            }
        }

        this.positions = points;
    }

    private EventHandler<Event> closeTab(){
        return event -> controller.closeTab();
    }

    @Override
    public void mvcGroupDestroy(){
        runInsideUISync(()-> {
            parentPane.getTabs().remove(tab);
        });
    }

}
