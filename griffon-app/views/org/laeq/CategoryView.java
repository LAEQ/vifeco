package org.laeq;

import griffon.core.artifact.GriffonView;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.SVGPath;
import javafx.util.Callback;
import javafx.util.Duration;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;
import org.laeq.model.Category;
import org.laeq.model.Icon;
import org.laeq.model.icon.IconSVG;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ArtifactProviderFor(GriffonView.class)
public class CategoryView extends AbstractJavaFXGriffonView {
    @MVCMember @Nonnull private CategoryController controller;
    @MVCMember @Nonnull private CategoryModel model;
    @MVCMember @Nonnull private VifecoView parentView;

    @FXML private TextField nameField;
    @FXML private TextField shortCutField;
    @FXML private TextField pathField;
    @FXML private TextField colorPickerField;
    @FXML private Pane svgDisplayPane;
    @FXML private TableView<Category> categoryTable;
    @FXML private TableColumn<Category, String> id;
    @FXML private TableColumn<Category, Void> icon;
    @FXML private TableColumn<Category, String> name;
    @FXML private TableColumn<Category, String> shortcut;
    @FXML private TableColumn<Category, Void> actions;

    @Override
    public void initUI() {
        Node node = loadFromFXML();
        parentView.middle.getChildren().add(node);
        connectMessageSource(node);
        connectActions(node, controller);

        init();
    }

    private void init(){
        //Table
        id.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getId().toString()));
        name.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getName()));
        shortcut.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getShortcut()));
        icon.setCellFactory(iconAction());
        actions.setCellFactory(addActions());

        pathField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.length() > 0){
                createSVG();
            } else {
                deleteSVG();
            }
        });

        colorPickerField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.length() > 0){
                Pattern pattern = Pattern.compile("^#[0-9A-F]{6}$");
                Matcher matcher = pattern.matcher(newValue);

                if(matcher.find()){
                    createSVG();
                    getApplication().getEventRouter().publishEvent("status.reset");
                } else {
                    deleteSVG();
                    getApplication().getEventRouter().publishEventAsync("status.error", Arrays.asList("category.color.invalid"));
                }
            }
        });

        //Form
        model.name.bindBidirectional(nameField.textProperty());
        model.shortCut.bindBidirectional(shortCutField.textProperty());
        model.color.bindBidirectional(colorPickerField.textProperty());
        model.icon.bindBidirectional(pathField.textProperty());

        categoryTable.setItems(this.model.categoryList);
    }

    private void deleteSVG() {
        svgDisplayPane.getChildren().clear();
    }

    private void createSVG() {
        deleteSVG();
        try{
            SVGPath svgPath = new SVGPath();
            svgPath.setSmooth(true);
            svgPath.setScaleX(4);
            svgPath.setScaleY(4);
            svgPath.setLayoutX(50);
            svgPath.setLayoutY(50);
            svgPath.setContent(pathField.textProperty().getValue());
            svgPath.setFill(Paint.valueOf(colorPickerField.textProperty().get()));
            svgDisplayPane.getChildren().add(svgPath);
        } catch (Exception e){
            deleteSVG();
        }
    }

    private Callback<TableColumn<Category, Void>, TableCell<Category, Void>> addActions() {
        return param -> {
            final  TableCell<Category, Void> cell = new TableCell<Category, Void>(){
                Button edit = new Button(translate("btn.edit"));
                Button delete = new Button(translate("btn.delete"));

                Group btnGroup = new Group();
                {
                    edit.setLayoutX(5);
                    edit.getStyleClass().addAll("btn", "btn-sm", "btn-info");
                    delete.setLayoutX(105);
                    delete.getStyleClass().addAll("btn", "btn-sm", "btn-danger");

                    btnGroup.getChildren().addAll(edit, delete);
                    edit.setOnAction(event -> {
                        model.setSelectedCategory(categoryTable.getItems().get(getIndex()));
                        Category category = categoryTable.getItems().get(getIndex());
                        colorPickerField.setText(category.getColor());
                    });

                    delete.setOnAction(event -> {
                        controller.delete(categoryTable.getItems().get(getIndex()));
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
    private Callback<TableColumn<Category, Void>, TableCell<Category, Void>> iconAction() {
        return  param -> {
            final TableCell<Category, Void> cell = new TableCell<Category, Void>() {

                SVGPath icon = new SVGPath();

                @Override
                public void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    try{
                        Category category = categoryTable.getItems().get(getIndex());
                        icon.setContent(category.getIcon());
                        icon.setFill(Paint.valueOf(category.getColor()));

                        colorPickerField.setText(category.getColor());
                    } catch (IndexOutOfBoundsException e){
                        // noop
                    }

                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(icon);
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
