package org.laeq;

import griffon.core.artifact.GriffonView;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.util.Callback;
import org.laeq.CollectionController;
import org.laeq.CollectionModel;
import org.laeq.TranslatedView;
import org.laeq.TranslationService;
import org.laeq.model.Category;
import org.laeq.model.CategoryCheckedBox;
import org.laeq.model.Collection;
import org.laeq.model.Icon;
import org.laeq.model.icon.Color;
import org.laeq.model.icon.IconDescriptorMatrice;
import org.laeq.model.icon.IconSVG;
import org.laeq.template.MiddlePaneView;
import org.laeq.user.PreferencesService;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

@ArtifactProviderFor(GriffonView.class)
public class CollectionView extends TranslatedView {
    @MVCMember @Nonnull private CollectionController controller;
    @MVCMember @Nonnull private CollectionModel model;
    @MVCMember @Nonnull private MiddlePaneView parentView;

    @FXML private TextField nameField;
    @FXML private Group categoryContainer;
    @FXML private Button saveActionTarget;
    @FXML private Button clearActionTarget;

    @FXML private TableView<Collection> collectionTable;
    @FXML private TableColumn<Collection, String> id;
    @FXML private TableColumn<Collection, String> name;
    @FXML private TableColumn<Collection, String> categories;
    @FXML private TableColumn<Collection, Icon> isDefault;
    @FXML private TableColumn<Collection, Void> actions;


    @Override
    public void initUI() {
        Node node = loadFromFXML();
        parentView.addMVCGroup(getMvcGroup().getMvcId(), node);
        connectMessageSource(node);
        connectActions(node, controller);
        init();
        initForm();
    }

    private void init(){
        name.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getName()));
        categories.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getCategorieNames()));
        isDefault.setCellValueFactory(cellData -> cellData.getValue().getDefault() ? new SimpleObjectProperty<>(new Icon(IconSVG.tick, Color.green)) : null);
        actions.setCellFactory(addActions());

        collectionTable.setItems(this.model.collections);

        model.name.bindBidirectional(nameField.textProperty());

//        categories.setCellFactory(new Callback<TableColumn<Collection, String>, TableCell<Collection,String>>() {
//            @Override
//            public TableCell<Collection, String> call( TableColumn<Collection, String> param) {
//                final TableCell<Collection, String> cell = new TableCell<Collection, String>() {
//                    private Text text;
//                    @Override
//                    public void updateItem(String item, boolean empty) {
//                        super.updateItem(item, empty);
//                        if (!isEmpty()) {
//                            text = new Text(item);
//                            text.setLineSpacing(5.0);
//                            text.setWrappingWidth(400); // Setting the wrapping width to the Text
//                            setGraphic(text);
//                        }
//                    }
//                };
//                return cell;
//            }
//        });
    }

    public void initForm(){
        runInsideUIAsync(() -> {
            double x = 0;
            double y = 0;
            double index = 0;
            for (Category category : model.getCategories()) {
                CategoryCheckedBox checkedBox = new CategoryCheckedBox(category);
                checkedBox.setLayoutX(x);
                checkedBox.setLayoutY(y);
                x += checkedBox.getWidth() + 20;
                index++;

                if (index != 0 && index % 3 == 0) {
                    x = 0;
                    y += 45;
                }

                categoryContainer.getChildren().add(checkedBox);
                checkedBox.getBox().selectedProperty().bindBidirectional(model.categorySBP.get(category));
            }
        });
    }

    private Callback<TableColumn<Collection, Void>, TableCell<Collection, Void>> addActions() {
        return param -> {
            final  TableCell<Collection, Void> cell = new TableCell<Collection, Void>(){
                Button edit = new Button("edit");
                Button delete = new Button("delete");

                Group btnGroup = new Group();
                {
                    edit.setLayoutX(5);
                    delete.setLayoutX(105);

                    btnGroup.getChildren().addAll(edit, delete);
                    Icon icon = new Icon(IconSVG.edit, Color.gray_dark);
//                    edit.setGraphic(icon);
                    edit.getStyleClass().addAll("btn", "btn-sm", "btn-info");
                    edit.setOnMouseClicked(event -> {
                        model.setSelectedCollection(collectionTable.getItems().get(getIndex()));
                    });

//                    delete.setGraphic(new Icon(IconSVG.bin, Color.gray_dark));
                    delete.getStyleClass().addAll("btn", "btn-sm", "btn-danger");
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
//    private Callback<TableColumn<Collection, Boolean>, TableCell<Collection, Boolean>> iconAction() {
//        return  param -> {
//            TableCell<Collection, Boolean> cell = new TableCell<Collection, Boolean>() {
//
//                @Override
//                protected void updateItem(Boolean item, boolean empty) {
//                    super.updateItem(item, empty);
//
//                    Group group = new Group();
//
//                    try {
//                        Set<Category> categorySet = collectionTable.getItems().get(getIndex()).getCategorySet();
//
//                        if(categorySet != null){
//                            IconDescriptorMatrice matrix = new IconDescriptorMatrice(categorySet);
//                            group.getChildren().addAll(matrix.getIconMap().values());
//                        }
//                    } catch (ArrayIndexOutOfBoundsException e) {
//                        getLog().error(e.getMessage());
//                    } catch (Exception e) {
//                        getLog().error(e.getMessage());
//                    }
//
//                    if (empty) {
//                        setGraphic(null);
//                        setText(null);
//                    } else {
//                        setGraphic(group);
//                    }
//                }
//            };
//
//            return cell;
//        };
//    }

}
