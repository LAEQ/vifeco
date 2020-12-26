package org.laeq;

import griffon.core.artifact.GriffonView;
import griffon.inject.MVCMember;
import griffon.javafx.support.JavaFXUtils;
import griffon.metadata.ArtifactProviderFor;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.util.Callback;
import org.laeq.model.*;
import org.laeq.model.icon.Color;
import org.laeq.model.icon.IconCounter;
import org.laeq.model.icon.IconSVG;
import org.laeq.template.MiddlePaneView;
import org.laeq.user.PreferencesService;
import javax.annotation.Nonnull;
import javax.inject.Inject;


@ArtifactProviderFor(GriffonView.class)
public class VideoView extends TranslatedView {
    @MVCMember @Nonnull private VideoController controller;
    @MVCMember @Nonnull private VideoModel model;
    @MVCMember @Nonnull private MiddlePaneView parentView;

    @FXML private TableView<Video> videoTable;
    @FXML private TableView<CategoryCount> categoryTable;

    @FXML private Label titleValue;
    @FXML private Label durationValue;
    @FXML private Label totalValue;
    @FXML private Group categoryGroup;

    @FXML private Button clearActionTarget;
    @FXML private Button editActionTarget;

    @FXML private TableColumn<Video, String> createdAt;
    @FXML private TableColumn<Video, String> name;
    @FXML private TableColumn<Video, String> path;
    @FXML private TableColumn<Video, String> duration;
    @FXML private TableColumn<Video, User>  user;
    @FXML private TableColumn<Video, Collection> collection;
    @FXML private TableColumn<Video, String> total;
    @FXML private TableColumn<Video, Void> actions;

    @FXML private TableColumn<CategoryCount, String> category;
    @FXML private TableColumn<CategoryCount, String> count;

    @Inject private PreferencesService prefService;

    @Override
    public void initUI() {
        Node node = loadFromFXML();

        parentView.addMVCGroup(getMvcGroup().getMvcId(), node);
        connectActions(node, controller);
        connectMessageSource(node);
        init();

        model.name.bindBidirectional(titleValue.textProperty());
        model.duration.bindBidirectional(durationValue.textProperty());
        model.total.bindBidirectional(totalValue.textProperty());
    }

    private void init(){
        videoTable.setEditable(true);

        createdAt.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getCreatedAtFormatted()));
        name.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().pathToName()));
        path.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getPath()));
        duration.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getDurationFormatted()));
        total.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(String.format("%d", cellData.getValue().getPoints().size())));
        actions.setCellFactory(addActions());

        ObservableList<User> users = FXCollections.observableArrayList(model.getUserSet());
        user.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getUser()));
        user.setMinWidth(140);
        user.setCellFactory(ComboBoxTableCell.forTableColumn(users));
        user.setOnEditCommit(event -> controller.updateUser(event));

        ObservableList<Collection> collections = FXCollections.observableArrayList(model.getCollectionSet());
        collection.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getCollection()));
        collection.setMinWidth(140);
        collection.setCellFactory(ComboBoxTableCell.forTableColumn(collections));
        collection.setOnEditCommit(event -> controller.updateCollection(event.getRowValue(), event.getNewValue()));

        videoTable.setItems(this.model.videoList);
        categoryTable.setItems(this.model.categoryCounts);

        category.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().category.getName()));
        count.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().count.toString()));
    }

    private Callback<TableColumn<Video, Void>, TableCell<Video, Void>> addActions() {
        return param -> {
            final  TableCell<Video, Void> cell = new TableCell<Video, Void>(){
                Button export = new Button(translate("btn.export"));
                Button edit = new Button(translate("btn.details"));
                Button delete = new Button(translate("btn.delete"));


                Group btnGroup = new Group();
                {
                    edit.setLayoutX(5);
                    export.setLayoutX(100);
                    delete.setLayoutX(200);

                    edit.getStyleClass().addAll("btn", "btn-info", "btn-sm");
                    export.getStyleClass().addAll("btn", "btn-warning", "btn-sm");
                    delete.getStyleClass().addAll("btn", "btn-danger", "btn-sm");

                    btnGroup.getChildren().addAll(edit, export, delete);
//                    Icon icon = new Icon(IconSVG.edit, Color.white);
//                    edit.setGraphic(icon);
                    edit.setOnAction(event -> {
                        controller.select(videoTable.getItems().get(getIndex()));
                    });

                    export.setOnAction(event -> {
                       controller.export(videoTable.getItems().get(getIndex()));
                    });

//                    delete.setGraphic(new Icon(IconSVG.bin, Color.white));
                    delete.setOnAction(event -> {
                        controller.delete(videoTable.getItems().get(getIndex()));
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
