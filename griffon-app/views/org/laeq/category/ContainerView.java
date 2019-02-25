package org.laeq.category;

import griffon.core.artifact.GriffonView;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.beans.binding.Bindings;
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
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;
import org.laeq.graphic.Color;
import org.laeq.graphic.IconSVG;
import org.laeq.model.Category;
import org.laeq.model.Icon;
import org.laeq.template.MiddlePaneView;

import javax.annotation.Nonnull;

@ArtifactProviderFor(GriffonView.class)
public class ContainerView extends AbstractJavaFXGriffonView {
    @MVCMember @Nonnull private ContainerController controller;
    @MVCMember @Nonnull private ContainerModel model;
    @MVCMember @Nonnull private MiddlePaneView parentView;

    @FXML private TextField nameField;
    @FXML private TextField shortCutField;
    @FXML private TextField pathField;
    @FXML private ColorPicker colorPickerField;
    @FXML private Pane svgDisplayPane;

    @FXML private TableView<Category> categoryTable;

    private SVGPath svgPath;

    @Override
    public void initUI() {
        Node node = loadFromFXML();
        parentView.addMVCGroup(getMvcGroup().getMvcId(), node);
        connectActions(node, controller);

        svgPath = new SVGPath();
//        svg.setCon
        svgPath.setSmooth(true);
        svgPath.setFill(Paint.valueOf("#000000"));
        svgPath.setContent("M18 15.422v.983c0 .771-1.862 1.396-4 1.396s-4-.625-4-1.396v-.983c.968.695 2.801.902 4 .902 1.202 0 3.035-.208 4-.902zm-4-1.363c-1.202 0-3.035-.209-4-.902v.973c0 .771 1.862 1.396 4 1.396s4-.625 4-1.396v-.973c-.968.695-2.801.902-4 .902zm0-5.86c-2.138 0-4 .625-4 1.396 0 .77 1.862 1.395 4 1.395s4-.625 4-1.395c0-.771-1.862-1.396-4-1.396zm0 3.591c-1.202 0-3.035-.209-4-.902v.977c0 .77 1.862 1.395 4 1.395s4-.625 4-1.395v-.977c-.968.695-2.801.902-4 .902zm-.5-9.79c-5.288 0-9.649 3.914-10.377 9h-3.123l4 5.917 4-5.917h-2.847c.711-3.972 4.174-7 8.347-7 4.687 0 8.5 3.813 8.5 8.5s-3.813 8.5-8.5 8.5c-3.015 0-5.662-1.583-7.171-3.957l-1.2 1.775c1.916 2.536 4.948 4.182 8.371 4.182 5.797 0 10.5-4.702 10.5-10.5s-4.703-10.5-10.5-10.5z");
        svgPath.setScaleX(4);
        svgPath.setScaleY(4);
        svgPath.setLayoutX(30);
        svgPath.setLayoutY(30);

        svgDisplayPane.getChildren().add(svgPath);

        init();
        initForm();
    }

    public void initForm(){
        colorPickerField.valueProperty().addListener(new ChangeListener<javafx.scene.paint.Color>() {
            @Override
            public void changed(ObservableValue<? extends javafx.scene.paint.Color> observable, javafx.scene.paint.Color oldValue, javafx.scene.paint.Color newValue) {
                String colorStr = "#" + Integer.toHexString(newValue.hashCode());
                try{
                    svgPath.setFill(Paint.valueOf(colorStr));
                }catch (IllegalArgumentException e){
                    svgPath.setFill(Paint.valueOf("#00000000"));
                }

                model.setColor(colorStr);
            }
        });
    }

    private void init(){
        TableColumn<Category, Void> iconColumn = new TableColumn<>("Icon");
        TableColumn<Category, String> nameColumn = new TableColumn("Name");
        TableColumn<Category, String> shortCut = new TableColumn<>("Shortcut");
        TableColumn<Category, Void> actionColumn = new TableColumn<>("Actions");

        categoryTable.getColumns().addAll(iconColumn, nameColumn, shortCut, actionColumn);

        nameColumn.setCellValueFactory(cellData -> Bindings.createStringBinding(() -> cellData.getValue().getName()));
        shortCut.setCellValueFactory(param -> Bindings.createStringBinding(() -> param.getValue().getShortcut()));


        model.nameProperty().bindBidirectional(nameField.textProperty());
        model.shortCutProperty().bindBidirectional(shortCutField.textProperty());
        model.iconProperty().bindBidirectional(pathField.textProperty());

        pathField.textProperty().addListener((observable, oldValue, newValue) -> {
           svgPath.setContent(newValue);
        });

        iconColumn.setCellFactory(iconAction());
        actionColumn.setCellFactory(addActions());
        categoryTable.setItems(this.model.getCategoryList());
    }

    private Callback<TableColumn<Category, Void>, TableCell<Category, Void>> addActions() {
        return param -> {
            final  TableCell<Category, Void> cell = new TableCell<Category, Void>(){
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
                        model.setSelectedCategory(categoryTable.getItems().get(getIndex()));
                    });


                    delete.setGraphic(new Icon(IconSVG.bin, Color.gray_dark));
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
                    } catch (Exception e){

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
}
