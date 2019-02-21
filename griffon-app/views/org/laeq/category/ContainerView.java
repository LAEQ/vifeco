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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;
import org.laeq.model.Category;
import org.laeq.model.Category;
import org.laeq.model.Icon;
import org.laeq.template.MiddlePaneView;

import java.util.Collections;
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
//        svgPath.setContent("M9.5 10.287c0-.41-.336-.743-.75-.743s-.75.333-.75.743.336.743.75.743.75-.333.75-.743zm4.5.495c0-.137-.112-.248-.25-.248h-3.5c-.138 0-.25.111-.25.248s.112.248.25.248h3.5c.138-.001.25-.112.25-.248zm2-.495c0-.41-.336-.743-.75-.743s-.75.333-.75.743.336.743.75.743.75-.333.75-.743zm-8.649-3.219h-1.101c-.138 0-.25.111-.25.248v.253c0 .393.463.49.808.49l.543-.991zm9.659 1.569c-.435-.8-.866-1.597-1.342-2.382-.393-.649-.685-.96-1.375-1.083-.698-.124-1.341-.172-2.293-.172s-1.595.048-2.292.172c-.69.123-.982.433-1.375 1.083-.477.785-.907 1.582-1.343 2.382-.344.63-.49 1.194-.49 1.884 0 .653.21 1.195.5 1.89v1.094c0 .273.224.495.5.495h.75c.276 0 .5-.222.5-.495v-.495h6.5v.495c0 .273.224.495.5.495h.75c.276 0 .5-.222.5-.495v-1.094c.29-.695.5-1.237.5-1.89 0-.69-.146-1.254-.49-1.884zm-7.821-1.873c.335-.554.426-.569.695-.617.635-.113 1.228-.157 2.116-.157s1.481.044 2.116.156c.269.048.36.064.695.617.204.337.405.687.597 1.03-.728.11-2.01.266-3.408.266-1.524 0-2.759-.166-3.402-.275.19-.34.389-.686.591-1.02zm5.798 5.256h-5.974c-.836 0-1.513-.671-1.513-1.498 0-.813.253-1.199.592-1.821.52.101 1.984.348 3.908.348 1.74 0 3.28-.225 3.917-.333.332.609.583.995.583 1.805 0 .828-.677 1.499-1.513 1.499zm2.763-4.952c.138 0 .25.111.25.248v.253c0 .393-.463.49-.808.49l-.543-.99h1.101zm-5.75-7.068c-5.523 0-10 4.394-10 9.815 0 5.505 4.375 9.268 10 14.185 5.625-4.917 10-8.68 10-14.185 0-5.421-4.478-9.815-10-9.815zm0 18c-4.419 0-8-3.582-8-8s3.581-8 8-8c4.419 0 8 3.582 8 8s-3.581 8-8 8z");
        svgPath.setScaleX(4);
        svgPath.setScaleY(4);
        svgPath.setLayoutX(30);
        svgPath.setLayoutY(30);

        svgDisplayPane.getChildren().add(svgPath);

        init();
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
        model.svgPathProperty().bindBidirectional(pathField.textProperty());
//        model.colorProperty().bindBidirectional(Bindings.createObjectBinding(colorPickerField.getValue().toString()));

        pathField.textProperty().addListener((observable, oldValue, newValue) -> {
           svgPath.setContent(newValue);
        });

        iconColumn.setCellFactory(iconAction());
        categoryTable.setItems(this.model.getCategoryList());
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
