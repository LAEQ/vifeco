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
import javafx.scene.paint.Color;
import org.laeq.TranslatedView;
import org.laeq.TranslationService;
import org.laeq.model.*;
import org.laeq.model.icon.IconCounter;
import org.laeq.model.icon.IconCounterMatrice;
import org.laeq.model.icon.IconSVG;
import org.laeq.template.MiddlePaneView;
import org.laeq.ui.DialogService;
import org.laeq.user.PreferencesService;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
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
    @FXML private Label titleValue;
    @FXML private Label durationValue;
    @FXML private Label totalValue;
    @FXML private Group categoryGroup;
    @FXML private TextField filterNameField;
    @FXML private Label filterLabel;

    @FXML private Button exportActionTarget;
    @FXML private Button clearActionTarget;
    @FXML private Button deleteActionTarget;
    @FXML private Button editActionTarget;

    private final Map<Category, IconCounter> categoryGroupMap = new HashMap<>();

    private TableColumn<Video, User> userColumn;
    private TableColumn<Video, Collection> collectionColumn;

    @Inject private DialogService dialogService;
    @Inject private PreferencesService prefService;

    private TranslationService translationService;

    @Override
    public void initUI() {
        model.setPrefs(prefService.getPreferences());

        Node node = loadFromFXML();
        parentView.addMVCGroup(getMvcGroup().getMvcId(), node);
        connectActions(node, controller);
        init();

        textFields.put(titleTxt, "org.laeq.video.title_text");
        textFields.put(videoTitleTxt, "org.laeq.video.video_title_text");
        textFields.put(durationTxt, "org.laeq.video.duration_text");
        textFields.put(totalTxt, "org.laeq.video.total_text");
        textFields.put(filterLabel, "org.laeq.video.filter_label");
        textFields.put(exportActionTarget, "org.laeq.video.export_btn");
        textFields.put(clearActionTarget, "org.laeq.video.clear_btn");
        textFields.put(deleteActionTarget, "org.laeq.video.delete_btn");
        textFields.put(editActionTarget, "org.laeq.video.edit_btn");

        changeLocale(model.getPrefs().locale);
    }

    public void changeLocale(Locale locale) {
        try {
            translationService = new TranslationService(getClass().getClassLoader().getResourceAsStream("messages/messages.json"), model.getPrefs().locale);
        } catch (IOException e) {
            getLog().error("Cannot load file messages.json");
        }

        setTranslatedText();
    }

    private void setTranslatedText(){
        try{
            textFields.entrySet().forEach(t -> t.getKey().setText(translationService.getMessage(t.getValue())));
            columnsMap.entrySet().forEach( t -> t.getKey().setText(translationService.getMessage(t.getValue())));
        } catch (Exception e){
            getLog().error(e.getMessage());
        }
    }

    private void init(){
        videoTable.setEditable(true);

        TableColumn<Video, Number> idColumn = new TableColumn<>("#");
        TableColumn<Video, String> dateColumn = new TableColumn<>("");
        TableColumn<Video, String> pathColumn = new TableColumn("");
        userColumn = new TableColumn<>("");
        TableColumn<Video, String> durationColumn = new TableColumn("");
        collectionColumn = new TableColumn("");
        TableColumn<Video, Number> totalColumn = new TableColumn<>("");
        TableColumn<Video, Icon> editColumn = new TableColumn("");

        columnsMap.put(dateColumn, "org.laeq.video.column.created_at");
        columnsMap.put(pathColumn, "org.laeq.video.column.name");
        columnsMap.put(userColumn, "org.laeq.video.column.user");
        columnsMap.put(durationColumn, "org.laeq.video.column.duration");
        columnsMap.put(collectionColumn, "org.laeq.video.column.collection");
        columnsMap.put(totalColumn, "org.laeq.video.column.total");
        columnsMap.put(editColumn, "org.laeq.video.column.not_editable");


        videoTable.getColumns().addAll(idColumn, dateColumn, pathColumn, userColumn, durationColumn, collectionColumn, totalColumn, editColumn);

        idColumn.setCellValueFactory(param -> Bindings.createIntegerBinding(()-> new Integer(param.getValue().getId())));
        dateColumn.setCellValueFactory(param -> Bindings.createStringBinding(() -> param.getValue().getCreatedFormatted()));
        pathColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        durationColumn.setCellValueFactory(cellData -> Bindings.createStringBinding(() -> cellData.getValue().getDurationFormatted()));
        totalColumn.setCellValueFactory(cellData -> cellData.getValue().totalProperty());
        editColumn.setCellValueFactory(cellData -> cellData.getValue().isEditable() ? null : new SimpleObjectProperty<>(new Icon(IconSVG.error, Color.DARKORANGE.toString())));

        videoTable.setItems(this.model.getFilteredList());
        videoTable.getSelectionModel().selectedItemProperty().addListener(observable -> {
            if(videoTable.getSelectionModel().getSelectedItem() != null){
                model.setSelectedVideo(videoTable.getSelectionModel().getSelectedItem());
                controller.showDetail();
            }
        });

        filterNameField.textProperty().addListener(filtering());

        videoTable.setOnMouseClicked(event -> {
            if(event.getClickCount() == 2){
                Video video = videoTable.getSelectionModel().getSelectedItem();
                if(video.getDuration() != 0){
                    controller.editVideo(video);
                }else{
                    runOutsideUIAsync(() -> {
                       controller.getVideoDuration(video);
                    });

                    alert(translationService.getMessage("org.laeq.title.error"), translationService.getMessage("org.laeq.video.duration.error"));
                }
            }
        });
    }

    private ChangeListener<String> filtering(){
        return (observable, oldValue, newValue) -> {
            model.getFilteredList().setPredicate(video -> {
                if((newValue == null || newValue.isEmpty())){
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
        // Display video information
        titleValue.setText(model.getSelectedVideo().getName());
        durationValue.setText(model.getSelectedVideo().getDurationFormatted());
        totalValue.setText(String.format("%d", model.getSelectedVideo().totalPoints()));

        IconCounterMatrice matrix = new IconCounterMatrice(this.model.getSelectedVideo().getCollection().getCategorySet());
        categoryGroupMap.clear();
        categoryGroupMap.putAll(matrix.getIconMap());

        categoryGroup.getChildren().clear();
        categoryGroup.getChildren().addAll(matrix.getIconMap().values());

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
    }

    public void reset() {
        titleValue.setText("");
        durationValue.setText("");
        totalValue.setText("");
        categoryGroupMap.clear();

        videoTable.refresh();
        videoTable.getSelectionModel().clearSelection();
        categoryGroup.getChildren().clear();
        categoryGroupMap.clear();
        model.setSelectedVideo(null);
    }

    public void refresh() {
        runInsideUISync(() -> {
            videoTable.refresh();
        });
    }
}
