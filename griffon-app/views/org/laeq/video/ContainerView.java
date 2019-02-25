package org.laeq.video;

import griffon.core.artifact.GriffonView;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.util.Callback;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;
import org.kordamp.ikonli.fontawesome.FontAwesome;
import org.kordamp.ikonli.javafx.FontIcon;
import org.laeq.model.CategoryCollection;
import org.laeq.model.User;
import org.laeq.model.Video;
import org.laeq.template.MiddlePaneView;

import javax.annotation.Nonnull;
import java.awt.event.ActionEvent;

@ArtifactProviderFor(GriffonView.class)
public class ContainerView extends AbstractJavaFXGriffonView {
    @MVCMember @Nonnull private ContainerController controller;
    @MVCMember @Nonnull private ContainerModel model;
    @MVCMember @Nonnull private MiddlePaneView parentView;

    @FXML private TableView<Video> videoTable;
    @FXML private ComboBox<User> userComboBox;
    @FXML private ComboBox<CategoryCollection> collectionComboBox;

    @Override
    public void initUI() {
        Node node = loadFromFXML();
        parentView.addMVCGroup(getMvcGroup().getMvcId(), node);
        connectActions(node, controller);
        init();
    }

    private void init(){
        TableColumn<Video, String> dateColumn = new TableColumn<>("Created At");
        TableColumn<Video, String> pathColumn = new TableColumn("Name");
        TableColumn<Video, String> userColumn = new TableColumn<>("User");
        TableColumn<Video, String> durationColumn = new TableColumn("Duration");
        TableColumn<Video, String> collectionColumn = new TableColumn("Collection");
        TableColumn<Video, Number> totalColumn = new TableColumn<>("Total");
        TableColumn<Video, Number> lastPointColumn = new TableColumn<>("Last point");
        TableColumn<Video, Void> actionColumn = new TableColumn<>("Actions");

        videoTable.getColumns().addAll(dateColumn, pathColumn, userColumn, durationColumn, collectionColumn, totalColumn, lastPointColumn, actionColumn);

        dateColumn.setCellValueFactory(param -> Bindings.createStringBinding(() -> param.getValue().getCreatedFormatted()));
        pathColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        durationColumn.setCellValueFactory(cellData -> cellData.getValue().durationProperty().asString());
        userColumn.setCellValueFactory(cellData -> cellData.getValue().getUser().getName());
        totalColumn.setCellValueFactory(cellData -> cellData.getValue().totalProperty());
        collectionColumn.setCellValueFactory(cellData -> cellData.getValue().getCategoryCollection().nameProperty());
//        lastPointColumn.setCellValueFactory(cellData -> cellData.getValue().lastProperty());

        actionColumn.setCellFactory(addActions());

        videoTable.setItems(this.model.getVideoList());

        videoTable.getSelectionModel().selectedItemProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                Video v = videoTable.getSelectionModel().getSelectedItem();
                model.setSelectedVideo(v);
                model.setUserId(v.getUser().getId());
                model.setCategoryCollectionId(v.getCategoryCollection().getId());
                userComboBox.getSelectionModel().select(model.getSelectedVideo().getUser());
                collectionComboBox.getSelectionModel().select(model.getSelectedVideo().getCategoryCollection());
            }
        });
    }

    public void initForm(){
        userComboBox.setItems(model.getUserSet());
        collectionComboBox.setItems(model.getCollectionSet());

        userComboBox.valueProperty().addListener((observable, oldValue, newValue) -> model.setUserId(newValue != null ? newValue.getId() : 0));
        collectionComboBox.valueProperty().addListener((observable, oldValue, newValue) -> model.setCategoryCollectionId(newValue != null ? newValue.getId() : 0));
    }

    private Callback<TableColumn<Video, Void>, TableCell<Video, Void>> addActions() {
        return param -> {
            final TableCell<Video, Void> cell = new TableCell<Video, Void>() {

                Button edit = new Button("");
                Button export = new Button("");
                Button delete = new Button("");

                Group btnGroup = new Group();
                {
                    edit.setLayoutX(5);
                    export.setLayoutX(45);
                    delete.setLayoutX(85);

                    btnGroup.getChildren().addAll(edit, export, delete);
                    FontIcon icon = new FontIcon(FontAwesome.EDIT);
                    edit.setGraphic(icon);
                    edit.setOnAction(event -> {
                        Video video = videoTable.getItems().get(getIndex());
                        controller.editVideo(video);
                    });
                    export.setGraphic(new FontIcon(FontAwesome.ARROW_CIRCLE_UP));
                    export.setOnAction(event -> {
                        Video video = videoTable.getItems().get(getIndex());
//                        controller.exportVideo(video);
                    });

                    delete.setGraphic(new FontIcon(FontAwesome.TRASH));
                    delete.setOnAction(event -> {
                        Video video = videoTable.getItems().get(getIndex());
//                        controller.deleteVideo(video);
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

    public void resetComboBox() {
        userComboBox.getSelectionModel().clearSelection();
        collectionComboBox.getSelectionModel().clearSelection();
    }
}
