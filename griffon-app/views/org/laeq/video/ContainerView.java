package org.laeq.video;

import griffon.core.artifact.GriffonView;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.util.Callback;
import org.kordamp.ikonli.fontawesome.FontAwesome;
import org.kordamp.ikonli.javafx.FontIcon;
import org.laeq.TranslatedView;
import org.laeq.model.Category;
import org.laeq.model.Collection;
import org.laeq.model.User;
import org.laeq.model.Video;
import org.laeq.model.icon.IconCounter;
import org.laeq.model.icon.IconCounterMatrice;
import org.laeq.template.MiddlePaneView;
import org.laeq.ui.DialogService;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

@ArtifactProviderFor(GriffonView.class)
public class ContainerView extends TranslatedView {
    @MVCMember @Nonnull private ContainerController controller;
    @MVCMember @Nonnull private ContainerModel model;
    @MVCMember @Nonnull private MiddlePaneView parentView;

    @FXML private TableView<Video> videoTable;

    @FXML private Label titleTxt;
    @FXML private Label videoTitleTxt;
    @FXML private Label durationTxt;
    @FXML private Label totalTxt;
    @FXML private Label lastPointTxt;
    @FXML private Label titleValue;
    @FXML private Label durationValue;
    @FXML private Label totalValue;
    @FXML private Label lastPointValue;
    @FXML private Group categoryGroup;
    @FXML private TextField filterNameField;
    @FXML private Label filterLabel;

    @FXML private Button exportActionTarget;
    @FXML private Button clearActionTarget;
    @FXML private Button deleteActionTarget;
    @FXML private Button editActionTarget;

    Map<Category, IconCounter> categoryGroupMap;

    private TableColumn<Video, User> userColumn;
    private TableColumn<Video, Collection> collectionColumn;

    @Inject private DialogService dialogService;

    @Override
    public void initUI() {
        Node node = loadFromFXML();
        parentView.addMVCGroup(getMvcGroup().getMvcId(), node);
        connectActions(node, controller);
        init();

        textFields.put(titleTxt, "org.laeq.video.title_text");
        textFields.put(videoTitleTxt, "org.laeq.video.video_title_text");
        textFields.put(durationTxt, "org.laeq.video.duration_text");
        textFields.put(totalTxt, "org.laeq.video.total_text");
        textFields.put(lastPointTxt, "org.laeq.video.last_point_text");
        textFields.put(filterLabel, "org.laeq.video.filter_label");
        textFields.put(exportActionTarget, "org.laeq.video.export_btn");
        textFields.put(clearActionTarget, "org.laeq.video.clear_btn");
        textFields.put(deleteActionTarget, "org.laeq.video.delete_btn");
        textFields.put(editActionTarget, "org.laeq.video.edit_btn");

        translate();
    }

    private void init(){
        reset();

        categoryGroupMap = new HashMap<>();
        videoTable.setEditable(true);

        TableColumn<Video, Void> selectBox = new TableColumn<>("#");
        selectBox.setPrefWidth(30);

        TableColumn<Video, String> dateColumn = new TableColumn<>("Created At");
        TableColumn<Video, String> pathColumn = new TableColumn("Name");
        userColumn = new TableColumn<>("User");
        TableColumn<Video, String> durationColumn = new TableColumn("Duration");
        collectionColumn = new TableColumn("Collection");
        TableColumn<Video, Number> totalColumn = new TableColumn<>("Total");
        TableColumn<Video, Number> lastPointColumn = new TableColumn<>("Last point");

        columnsMap.put(dateColumn, "org.laeq.video.column.created_at");
        columnsMap.put(pathColumn, "org.laeq.video.column.name");
        columnsMap.put(userColumn, "org.laeq.video.column.user");
        columnsMap.put(durationColumn, "org.laeq.video.column.duration");
        columnsMap.put(collectionColumn, "org.laeq.video.column.collection");
        columnsMap.put(totalColumn, "org.laeq.video.column.total");
        columnsMap.put(lastPointColumn, "org.laeq.video.column.last_point");

        videoTable.getColumns().addAll(selectBox, dateColumn, pathColumn, userColumn, durationColumn, collectionColumn, totalColumn, lastPointColumn);

        selectBox.setCellFactory(addSelectBox());
        dateColumn.setCellValueFactory(param -> Bindings.createStringBinding(() -> param.getValue().getCreatedFormatted()));
        pathColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        durationColumn.setCellValueFactory(cellData -> cellData.getValue().durationProperty().asString());
        totalColumn.setCellValueFactory(cellData -> cellData.getValue().totalProperty());

        videoTable.setItems(this.model.getFilteredList());
        videoTable.getSelectionModel().selectedItemProperty().addListener(observable -> {
            model.setSelectedVideo(videoTable.getSelectionModel().getSelectedItem());
            controller.showDetail();
        });

        filterNameField.textProperty().addListener(filtering());

        videoTable.setOnMouseClicked(event -> {
            if(event.getClickCount() == 2){
                Video video = videoTable.getSelectionModel().getSelectedItem();
                if(video.getDuration() != 0){
                    controller.editVideo(video);
                }else{
                    alert("key.to_implement", "org.laeq.video.duration.error");
                }

            }
        });
    }

    private Callback<TableColumn<Video, Void>, TableCell<Video, Void>> addSelectBox() {
        return param -> {
            final  TableCell<Video, Void> cell = new TableCell<Video, Void>(){
                CheckBox selectBox = new CheckBox("");

                Group btnGroup = new Group();
                {
                    btnGroup.getChildren().addAll(selectBox);
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

    private ChangeListener<String> filtering(){
        return (observable, oldValue, newValue) -> {
            model.getFilteredList().setPredicate(video -> {
                if(newValue == null || newValue.isEmpty()){
                    return true;
                }

                String filter = newValue.toLowerCase();
                if(video.getName().toLowerCase().contains(filter)){
                    return true;
                }

                return false;
            });
        };
    }

    public void showDetails() {
        Map<Category, Long> pointsByCategory = this.model.getTotalByCategory();

        categoryGroupMap.forEach((category, categoryIcon) -> {
            categoryIcon.reset();

            if(this.model.getSelectedVideo().getCollection().getCategorySet().contains(category)){
                categoryIcon.setText("0");
                categoryIcon.setOpacity(1);
            }

            if(pointsByCategory.containsKey(category)){
                categoryIcon.setText(pointsByCategory.get(category).toString());
            }
        });

        titleValue.setText(model.getSelectedVideo().getName());
        durationValue.setText(model.getSelectedVideo().getDurationFormatted());
        totalValue.setText(String.format("%d", model.getSelectedVideo().totalPoints()));
        lastPointValue.setText("to do");
    }

    public void initForm(){
        ObservableList<User> users = FXCollections.observableArrayList(model.getUserSet());

        userColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getUser()));
        userColumn.setMinWidth(140);
        userColumn.setCellFactory(ComboBoxTableCell.forTableColumn(users));
        userColumn.setOnEditCommit(event -> controller.updateUser(event));

        ObservableList<Collection> collections = FXCollections.observableArrayList(model.getCollectionSet());
        collectionColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getCollection()));
        collectionColumn.setMinWidth(140);
        collectionColumn.setCellFactory(ComboBoxTableCell.forTableColumn(collections));
        collectionColumn.setOnEditCommit(event -> controller.updateCollection(event));

        IconCounterMatrice matrix = new IconCounterMatrice(model.getCategorySet());
        categoryGroupMap = matrix.getIconMap();

        categoryGroup.getChildren().addAll(matrix.getIconMap().values());
    }

    public void reset() {
        titleValue.setText("");
        durationValue.setText("");
        totalValue.setText("");
        lastPointValue.setText("");

        model.getCategorySet().forEach(c -> {
            categoryGroupMap.get(c).setText("-");
        });

        videoTable.getSelectionModel().clearSelection();
    }
}
