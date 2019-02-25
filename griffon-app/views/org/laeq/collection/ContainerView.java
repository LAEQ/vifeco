package org.laeq.collection;

import griffon.core.artifact.GriffonView;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Callback;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;
import org.laeq.graphic.Color;
import org.laeq.graphic.IconSVG;
import org.laeq.model.Category;
import org.laeq.model.CategoryCollection;
import org.laeq.model.Icon;
import org.laeq.template.MiddlePaneView;

import javax.annotation.Nonnull;

@ArtifactProviderFor(GriffonView.class)
public class ContainerView extends AbstractJavaFXGriffonView {
    @MVCMember @Nonnull private ContainerController controller;
    @MVCMember @Nonnull private ContainerModel model;
    @MVCMember @Nonnull private MiddlePaneView parentView;

    @FXML private Text nameText;
    @FXML private TextField nameField;
    @FXML private CheckBox defaultBox;
    @FXML private Group categoryContainer;

    @FXML private TableView<CategoryCollection> collectionTable;

    @Override
    public void initUI() {
        Node node = loadFromFXML();
        parentView.addMVCGroup(getMvcGroup().getMvcId(), node);
        connectActions(node, controller);
        init();
        initForm();
    }

    private void init(){
        TableColumn<CategoryCollection, String> nameColumn = new TableColumn("Name");
        TableColumn<CategoryCollection, Void> categoryListColumn = new TableColumn("Categories");
        TableColumn<CategoryCollection, Void> actionColumn = new TableColumn<>("Actions");


        collectionTable.getColumns().addAll(nameColumn, categoryListColumn, actionColumn);
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        categoryListColumn.setCellFactory(iconAction());
        actionColumn.setCellFactory(addActions());

        collectionTable.setItems(this.model.getCollections());
    }

    public void initForm(){
        model.nameProperty().bindBidirectional(nameField.textProperty());
        model.isDefaultProperty().bindBidirectional(defaultBox.selectedProperty());

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

    private Callback<TableColumn<CategoryCollection, Void>, TableCell<CategoryCollection, Void>> addActions() {
        return param -> {
            final  TableCell<CategoryCollection, Void> cell = new TableCell<CategoryCollection, Void>(){
                Button edit = new Button("");
                Button delete = new Button("");

                Group btnGroup = new Group();
                {
                    edit.setLayoutX(5);
                    delete.setLayoutX(55);

                    btnGroup.getChildren().addAll(edit, delete);
                    Icon icon = new Icon(IconSVG.edit, org.laeq.graphic.Color.gray_dark);
                    edit.setGraphic(icon);
                    edit.setOnAction(event -> {
                        model.setSelectedCollection(collectionTable.getItems().get(getIndex()));
                    });


                    delete.setGraphic(new Icon(IconSVG.bin, Color.gray_dark));
                    delete.setOnAction(event -> {
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
    private Callback<TableColumn<CategoryCollection, Void>, TableCell<CategoryCollection, Void>> iconAction() {
        return  param -> {
            final TableCell<CategoryCollection, Void> cell = new TableCell<CategoryCollection, Void>() {

                Group container = new CategoryGroup();

                @Override
                public void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);

                    try {
                        CategoryCollection cc = collectionTable.getItems().get(getIndex());

                        container = new CategoryGroup(cc.getCategorySet());
                    }catch (ArrayIndexOutOfBoundsException e){

                    } catch (Exception e){

                    }

                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(container);
                    }
                }
            };

            return cell;
        };
    }
}
