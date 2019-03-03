package org.laeq.collection;

import griffon.core.artifact.GriffonView;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.util.Callback;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;
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
public class ContainerView extends AbstractJavaFXGriffonView {
    @MVCMember @Nonnull private ContainerController controller;
    @MVCMember @Nonnull private ContainerModel model;
    @MVCMember @Nonnull private MiddlePaneView parentView;

    @FXML private Text nameText;
    @FXML private TextField nameField;
    @FXML private CheckBox defaultBox;
    @FXML private Group categoryContainer;

    @FXML private TableView<Collection> collectionTable;

    @Override
    public void initUI() {
        Node node = loadFromFXML();
        parentView.addMVCGroup(getMvcGroup().getMvcId(), node);
        connectActions(node, controller);
        init();
        initForm();
    }

    private void init(){
        TableColumn<Collection, String> nameColumn = new TableColumn("Name");
        TableColumn categoryListColumn = new TableColumn("Categories");
        TableColumn<Collection, Void> actionColumn = new TableColumn<>("Actions");

        collectionTable.getColumns().addAll(nameColumn, categoryListColumn, actionColumn);

        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        actionColumn.setCellFactory(addActions());

        categoryListColumn.setCellValueFactory(new PropertyValueFactory<Collection, Boolean>("prout"));

        categoryListColumn.setCellFactory(iconAction());

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

                    } catch (Exception e) {

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
}
