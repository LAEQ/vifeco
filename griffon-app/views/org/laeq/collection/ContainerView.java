package org.laeq.collection;

import griffon.core.artifact.GriffonView;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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

    @FXML private TableView<CategoryCollection> collectionTable;

    @Override
    public void initUI() {
        Node node = loadFromFXML();
        parentView.addMVCGroup(getMvcGroup().getMvcId(), node);
        connectActions(node, controller);
        init();
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
//                        model.setSelectedCategory(categoryTable.getItems().get(getIndex()));
                    });


                    delete.setGraphic(new Icon(IconSVG.bin, Color.gray_dark));
                    delete.setOnAction(event -> {
//                        controller.delete(categoryTable.getItems().get(getIndex()));
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
                    }catch (Exception e){

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
