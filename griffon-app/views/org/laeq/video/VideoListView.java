package org.laeq.video;

import griffon.core.artifact.GriffonView;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.util.Callback;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;
import org.kordamp.ikonli.fontawesome.FontAwesome;
import org.kordamp.ikonli.javafx.FontIcon;
import org.laeq.VifecoView;
import org.laeq.model.VideoUser;
import javax.annotation.Nonnull;

@ArtifactProviderFor(GriffonView.class)
public class VideoListView extends AbstractJavaFXGriffonView {
    @MVCMember @Nonnull
    private VideoListController controller;

    @MVCMember @Nonnull
    private VideoListModel model;

    @MVCMember @Nonnull
    private VifecoView parentView;

    @FXML private AnchorPane videoListPane;

    @FXML private TableView<VideoUser> videoListTable;

    @FXML private TabPane tabPane;

    @Nonnull
    public TabPane getTabPane() {
        return tabPane;
    }

    private ObservableList<VideoUser> videoList;

    @Override
    public void initUI() {
        Node node = loadFromFXML();

        videoList = FXCollections.observableList(this.model.getVideoList());
        TableColumn<VideoUser, String> dateColumn = new TableColumn<>("Created At");
        TableColumn<VideoUser, String> pathColumn = new TableColumn("Name");
        TableColumn<VideoUser, String> userColumn = new TableColumn<>("User");
        TableColumn<VideoUser, String> durationColumn = new TableColumn("Duration");
        TableColumn<VideoUser, Number> totalColumn = new TableColumn<>("Total");
        TableColumn<VideoUser, Number> lastPointColumn = new TableColumn<>("Last point");
        TableColumn<VideoUser, Void> actionColumn = new TableColumn<>("Actions");

        videoListTable.getColumns().addAll(dateColumn, pathColumn, userColumn, durationColumn, totalColumn, lastPointColumn, actionColumn);

        dateColumn.setCellValueFactory(param -> Bindings.createStringBinding(() -> param.getValue().getVideo().getCreatedAt().toString()));
        pathColumn.setCellValueFactory(cellData -> cellData.getValue().getVideo().nameProperty());
        durationColumn.setCellValueFactory(cellData -> cellData.getValue().getVideo().durationProperty().asString());
        userColumn.setCellValueFactory(cellData -> cellData.getValue().getUser().getName());
        totalColumn.setCellValueFactory(cellData -> cellData.getValue().totalProperty());
        lastPointColumn.setCellValueFactory(cellData -> cellData.getValue().lastProperty());
        actionColumn.setCellFactory(addActions());


        videoListTable.setItems(videoList);
        parentView.getMiddlePane().getItems().add(node);
    }

    public void addVideo(VideoUser video){
        videoList.add(video);
    }

    private Callback<TableColumn<VideoUser, Void>, TableCell<VideoUser, Void>> addActions() {
        return param -> {
            final TableCell<VideoUser, Void> cell = new TableCell<VideoUser, Void>() {

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
                        VideoUser videoUser = videoListTable.getItems().get(getIndex());
                        controller.editVideo(videoUser);
                    });
                    export.setGraphic(new FontIcon(FontAwesome.ARROW_CIRCLE_UP));
                    export.setOnAction(event -> {
                        VideoUser videoUser = videoListTable.getItems().get(getIndex());
                        controller.exportVideo(videoUser);
                    });

                    delete.setGraphic(new FontIcon(FontAwesome.TRASH));
                    delete.setOnAction(event -> {
                        VideoUser videoUser = videoListTable.getItems().get(getIndex());
                        controller.deleteVideo(videoUser);
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

}
