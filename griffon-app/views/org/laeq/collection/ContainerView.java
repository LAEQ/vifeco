package org.laeq.collection;

import griffon.core.artifact.GriffonView;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import org.laeq.TranslatedView;
import org.laeq.graphic.Color;
import org.laeq.graphic.IconSVG;
import org.laeq.graphic.icon.CategoryMatrice;
import org.laeq.graphic.icon.IconType;
import org.laeq.model.Category;
import org.laeq.model.Collection;
import org.laeq.model.Icon;
import org.laeq.template.MiddlePaneView;

import javax.annotation.Nonnull;
import java.util.Set;

@ArtifactProviderFor(GriffonView.class)
public class ContainerView extends TranslatedView {
    @MVCMember @Nonnull private ContainerController controller;
    @MVCMember @Nonnull private ContainerModel model;
    @MVCMember @Nonnull private MiddlePaneView parentView;

    @FXML private Label nameLabel;
    @FXML private TextField nameField;
    @FXML private Group categoryContainer;
    @FXML private Label titleLabel;
    @FXML private Label categoryLabel;
    @FXML private TableView<Collection> collectionTable;
    @FXML private Button saveActionTarget;
    @FXML private Button clearActionTarget;

    @Override
    public void initUI() {
        Node node = loadFromFXML();
        parentView.addMVCGroup(getMvcGroup().getMvcId(), node);
        connectActions(node, controller);
        init();
        initForm();

        textFields.put(nameLabel, "org.laeq.collection.name");
        textFields.put(titleLabel, "org.laeq.collection.title_create");
        textFields.put(categoryLabel, "org.laeq.collection.category_title");
        textFields.put(saveActionTarget, "org.laeq.collection.save_btn");
        textFields.put(clearActionTarget, "org.laeq.collection.clear_btn");

        translate();
    }

    private void init(){
        TableColumn<Collection, String> nameColumn = new TableColumn("");
        TableColumn categoryListColumn = new TableColumn("");
        TableColumn<Collection, Icon>  isDefaultColumn = new TableColumn("");
        TableColumn<Collection, Void> actionColumn = new TableColumn<>("");

        columnsMap.put(nameColumn, "org.laeq.collection.column.name");
        columnsMap.put(isDefaultColumn, "org.laeq.collection.column.is_default");
        columnsMap.put(categoryListColumn, "org.laeq.collection.column.categories");
        columnsMap.put(actionColumn, "org.laeq.collection.column.actions");

        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        isDefaultColumn.setCellValueFactory(cellData -> cellData.getValue().isIsDefault() ? new SimpleObjectProperty<>(new Icon(IconSVG.tick, Color.green)) : null);
        actionColumn.setCellFactory(addActions());
        categoryListColumn.setCellValueFactory(new PropertyValueFactory<Collection, Boolean>("prout"));
        categoryListColumn.setCellFactory(iconAction());

        collectionTable.getColumns().addAll(nameColumn, categoryListColumn, isDefaultColumn, actionColumn);
        collectionTable.setItems(this.model.getCollections());
    }

    public void initForm(){
        model.nameProperty().bindBidirectional(nameField.textProperty());
        model.isDefaultProperty().setValue(false);

        double x = 0;
        double y = 0;
        double index = 0;
        for (Category category: model.getCategories()) {
            CategoryCheckedBox checkedBox = new CategoryCheckedBox(category);
            checkedBox.setLayoutX(x);
            checkedBox.setLayoutY(y);
            x += checkedBox.getWidth() + 20;
            index++;

            if(index != 0 && index % 3 == 0) {
                x = 0;
                y += 45;
            }

            categoryContainer.getChildren().add(checkedBox);
            checkedBox.getBox().selectedProperty().bindBidirectional(model.getSBP(category));
        }
    }

    private Callback<TableColumn<Collection, Void>, TableCell<Collection, Void>> addActions() {
        return param -> {
            final  TableCell<Collection, Void> cell = new TableCell<Collection, Void>(){
                Button edit = new Button("");
                Button delete = new Button("");

                Group btnGroup = new Group();
                {
                    edit.setLayoutX(5);
                    delete.setLayoutX(55);

                    btnGroup.getChildren().addAll(edit, delete);
                    Icon icon = new Icon(IconSVG.edit, org.laeq.graphic.Color.gray_dark);
                    edit.setGraphic(icon);
                    edit.setOnMouseClicked(event -> {
                        model.setSelectedCollection(collectionTable.getItems().get(getIndex()));
                        translate(titleLabel, "org.laeq.collection.title_edit");
                    });

                    delete.setGraphic(new Icon(IconSVG.bin, Color.gray_dark));
                    delete.setOnMouseClicked(event -> {
                        controller.delete(collectionTable.getItems().get(getIndex()));
                    });
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
    private Callback<TableColumn<Collection, Boolean>, TableCell<Collection, Boolean>> iconAction() {
        return  param -> {
            TableCell<Collection, Boolean> cell = new TableCell<Collection, Boolean>() {

                @Override
                protected void updateItem(Boolean item, boolean empty) {
                    super.updateItem(item, empty);

                    Group group = new Group();

                    try {
                        Set<Category> categorySet = collectionTable.getItems().get(getIndex()).getCategorySet();

                        if(categorySet != null){
                            CategoryMatrice matrice = new CategoryMatrice(categorySet, IconType.REGULAR);
                            group.getChildren().addAll(matrice.getIconMap().values());
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        getLog().error(e.getMessage());
                    } catch (Exception e) {
                        getLog().error(e.getMessage());
                    }

                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        setGraphic(group);
                    }
                }
            };

            return cell;
        };
    }

    public void clear() {
        translate(titleLabel, "org.laeq.collection.title_create");
    }
}
