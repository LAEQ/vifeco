package org.laeq.editor;

import griffon.core.artifact.GriffonView;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;
import org.laeq.model.Drawing;
import org.laeq.tools.ColorUtils;

import java.util.Arrays;
import java.util.Collections;

@ArtifactProviderFor(GriffonView.class)
public class DrawingView extends AbstractJavaFXGriffonView {

    @MVCMember DrawingController controller;
    @MVCMember DrawingModel model;
    @FXML private TableView<Drawing> drawTable;
    @FXML private TableColumn<Drawing, Boolean> selected;
    @FXML private TableColumn<Drawing, String> draw_type;
    @FXML private TableColumn<Drawing, String> color;
    @FXML private TableColumn<Drawing, Void> actions;

    @FXML private Button lineButton;
    @FXML private Button rectangleButton;
    @FXML private ColorPicker colorPicker;
    private Scene scene;

    @Override
    public void mvcGroupDestroy(){
        getApplication().getEventRouter().publishEvent("drawing.destroy");
    }

    @Override
    public void initUI() {
        Stage stage = (Stage) getApplication().createApplicationContainer(Collections.<String,Object>emptyMap());
        stage.setTitle(getApplication().getMessageSource().getMessage("drawing.window.title"));
        stage.getIcons().add( getImage("favicon-32x32.png"));
        scene = init();
        stage.setScene(scene);
        stage.sizeToScene();
        stage.setAlwaysOnTop(true);

        getApplication().getWindowManager().attach("drawing", stage);
        getApplication().getWindowManager().show("drawing");

        stage.setOnCloseRequest(event -> {
            model.drawingList.clear();
            getApplication().getEventRouter().publishEvent("mvc.clean", Arrays.asList("drawing"));
        });
    }

    private Scene init() {
        Scene scene = new Scene(new Group());
        scene.setFill(Color.WHITE);
        scene.getStylesheets().add("org/kordamp/bootstrapfx/bootstrapfx.css");

        Node node = loadFromFXML();
        if (node instanceof Parent) {
            scene.setRoot((Parent) node);
        } else {
            ((Group) scene.getRoot()).getChildren().addAll(node);
        }
        connectActions(node, controller);
        connectMessageSource(node);

        colorPicker.setOnAction(event -> {
            String colorStr = ColorUtils.colorToHex(colorPicker.getValue());

            model.color.set(colorStr);
        });

        initTable();

        return scene;
    }

    private void initTable() {
        drawTable.setEditable(true);
        drawTable.setItems(this.model.drawingList);

        selected.setCellValueFactory(param -> {
            param.getValue().activeProperty().addListener((observable, oldValue, newValue) -> {
                controller.updateSelection();
            });

            return param.getValue().activeProperty();
        });
        selected.setCellFactory(CheckBoxTableCell.forTableColumn(selected));
        draw_type.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getType().toString()));
        color.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getColor()));
        actions.setCellFactory(addActions());
    }

    private Callback<TableColumn<Drawing, Void>, TableCell<Drawing, Void>> addActions() {
        return param -> {
            final  TableCell<Drawing, Void> cell = new TableCell<Drawing, Void>(){
                Button delete = new Button(translate("btn.delete"));

                Group btnGroup = new Group();
                {
                    delete.setLayoutX(105);
                    delete.getStyleClass().addAll("btn", "btn-sm", "btn-danger");

                    btnGroup.getChildren().addAll(delete);
                    delete.setOnAction(event -> {
                        controller.delete(model.drawingList.get(getIndex()));
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

    private String translate(String key){
        return getApplication().getMessageSource().getMessage(key);
    }

    private Image getImage(String path) {
        return new Image(getClass().getClassLoader().getResourceAsStream(path));
    }
}
