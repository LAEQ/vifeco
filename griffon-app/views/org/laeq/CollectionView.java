package org.laeq;

import griffon.core.artifact.GriffonView;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.util.Callback;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;
import org.laeq.model.*;
import org.laeq.model.icon.Color;
import org.laeq.model.icon.IconSVG;

import javax.annotation.Nonnull;

@ArtifactProviderFor(GriffonView.class)
public class CollectionView extends AbstractJavaFXGriffonView {
    @MVCMember @Nonnull private CollectionController controller;
    @MVCMember @Nonnull private CollectionModel model;
    @MVCMember @Nonnull private VifecoView parentView;

    @FXML private TextField nameField;
    @FXML private Group categoryContainer;
    @FXML private Button saveActionTarget;
    @FXML private Button clearActionTarget;

    @FXML private TableView<Collection> collectionTable;
    @FXML private TableColumn<Collection, Integer> id;
    @FXML private TableColumn<Collection, String> name;
    @FXML private TableColumn<Collection, String> categories;
    @FXML private TableColumn<Collection, Icon> isDefault;
    @FXML private TableColumn<Collection, Void> actions;

    @FXML private TableView<Category> categoryTable;
    @FXML private TableColumn<Category, CheckBox> checkboxColumn;
    @FXML private TableColumn<Category, Icon> iconColumn;
    @FXML private TableColumn<Category, String> categoryColumn;


    @Override
    public void initUI() {
        Node node = loadFromFXML();
        parentView.middle.getChildren().add(node);
        connectMessageSource(node);
        connectActions(node, controller);
        init();
    }

    private void init(){
        id.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getId()));
        name.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getName()));
        categories.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getCategorieNames()));
        isDefault.setCellValueFactory(cellData -> cellData.getValue().getDefault() ? new SimpleObjectProperty<>(new Icon(IconSVG.tick, Color.green)) : null);
        actions.setCellFactory(addActions());

        collectionTable.setItems(this.model.collections);
        model.name.bindBidirectional(nameField.textProperty());

        checkboxColumn.setCellValueFactory(c -> {
            Category category = c.getValue();
            CheckBox checkBox = new CheckBox();
            checkBox.selectedProperty().bindBidirectional(model.categorySBP.get(category));

            return new SimpleObjectProperty<>(checkBox);
        });
        iconColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getIcon2()));
        categoryColumn.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getName()));

        categoryTable.setItems(model.categories);
    }

    private Callback<TableColumn<Collection, Void>, TableCell<Collection, Void>> addActions() {
        return param -> {
            final  TableCell<Collection, Void> cell = new TableCell<Collection, Void>(){
                Button edit = new Button(translate("btn.edit"));
                Button delete = new Button(translate("btn.delete"));

                Group btnGroup = new Group();
                {
                    edit.setLayoutX(5);
                    delete.setLayoutX(105);

                    btnGroup.getChildren().addAll(edit, delete);
                    edit.getStyleClass().addAll("btn", "btn-sm", "btn-info");
                    edit.setOnMouseClicked(event -> {
                        model.clear();
                        model.setSelectedCollection(collectionTable.getItems().get(getIndex()));
                    });

                    delete.getStyleClass().addAll("btn", "btn-sm", "btn-danger");
                    delete.setOnMouseClicked(event ->  controller.delete(collectionTable.getItems().get(getIndex())));
                }

                @Override
                public void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(btnGroup);
                    }
                }
            };

            return cell;
        };
    }

    private String translate(String key){
        return getApplication().getMessageSource().getMessage(key);
    }
}
