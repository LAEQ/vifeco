package org.laeq;

import griffon.core.artifact.GriffonView;
import griffon.inject.MVCMember;
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
        model.setPrefs(prefService.getPreferences());
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
        collection.setOnEditCommit(event -> controller.updateCollection(event));

        videoTable.setItems(this.model.videoList);
        categoryTable.setItems(this.model.categoryCounts);

        category.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().category.getName()));
        count.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().count.toString()));
    }

    private Callback<TableColumn<Video, Void>, TableCell<Video, Void>> addActions() {
        return param -> {
            final  TableCell<Video, Void> cell = new TableCell<Video, Void>(){
                Button edit = new Button(translate("btn.edit"));
                Button delete = new Button(translate("btn.delete"));

                Group btnGroup = new Group();
                {
                    edit.setLayoutX(5);
                    delete.setLayoutX(105);

                    edit.getStyleClass().addAll("btn", "btn-info");
                    delete.getStyleClass().addAll("btn", "btn-danger");

                    btnGroup.getChildren().addAll(edit, delete);
//                    Icon icon = new Icon(IconSVG.edit, Color.white);
//                    edit.setGraphic(icon);
                    edit.setOnAction(event -> {
                        model.setSelectedVideo(videoTable.getItems().get(getIndex()));
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

    public void showDetails() {
        // Display video information
//        titleValue.setText(model.getSelectedVideo().getName());
//        durationValue.setText(model.getSelectedVideo().getDurationFormatted());
//        totalValue.setText(String.format("%d", model.getSelectedVideo().totalPoints()));

//        IconCounterMatrice matrix = new IconCounterMatrice(this.model.getSelectedVideo().getCollection().getCategorySet());
//        categoryGroupMap.clear();
//        categoryGroupMap.putAll(matrix.getIconMap());
//
//        categoryGroup.getChildren().clear();
//        categoryGroup.getChildren().addAll(matrix.getIconMap().values());
//
//        Map<Category, Long> pointsByCategory = this.model.getTotalByCategory();
//
//        categoryGroupMap.forEach((category, categoryIcon) -> {
//            categoryIcon.reset();
//
//            if(this.model.getSelectedVideo().getCollection().getCategorySet().contains(category)){
//                categoryIcon.setText("0");
//                categoryIcon.setOpacity(1);
//            }
//
//            if(pointsByCategory.containsKey(category)){
//                categoryIcon.setText(pointsByCategory.get(category).toString());
//            }
//        });
    }

}
