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
import org.laeq.TranslatedView;
import org.laeq.TranslationService;
import org.laeq.model.Category;
import org.laeq.model.Collection;
import org.laeq.model.User;
import org.laeq.model.Video;
import org.laeq.model.icon.IconCounter;
import org.laeq.model.icon.IconCounterMatrice;
import org.laeq.model.icon.IconSVG;
import org.laeq.model.icon.IconSquare;
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

    Map<Category, IconCounter> categoryGroupMap;

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

//        IconSquare exportIcon = new IconSquare(new Category("export", IconSVG.export, "#FFFFFFFF", "A"));
//        exportIcon.decorate();
//        exportActionTarget.setGraphic(exportIcon);

//        IconSquare deleteIcon = new IconSquare(new Category("delete", IconSVG.trash, "#FFFFFFFF", "A"));
//        deleteIcon.decorate();
//        deleteActionTarget.setGraphic(deleteIcon);

//        IconSquare editIcon = new IconSquare(new Category("delete", IconSVG.edit, "#FFFFFFFF", "A"));
//        editIcon.decorate();
//        editActionTarget.setGraphic(editIcon);

//        IconSquare clearIcon = new IconSquare(new Category("clear", IconSVG.clear, "#000000FF", "A"));
//        clearIcon.decorate();
//        clearActionTarget.setGraphic(clearIcon);

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
            textFields.entrySet().forEach(t -> {
                System.out.println(t.getKey() + " : " + t.getValue());
                t.getKey().setText(translationService.getMessage(t.getValue()));
            });

            columnsMap.entrySet().forEach( t -> {
                t.getKey().setText(translationService.getMessage(t.getValue()));
            });
        } catch (Exception e){
            getLog().error("icit: " + e.getMessage());
        }
    }

    private void init(){
        categoryGroupMap = new HashMap<>();
        videoTable.setEditable(true);

        TableColumn<Video, Number> idColumn = new TableColumn<>("#");
        TableColumn<Video, String> dateColumn = new TableColumn<>("Created At");
        TableColumn<Video, String> pathColumn = new TableColumn("Name");
        userColumn = new TableColumn<>("User");
        TableColumn<Video, String> durationColumn = new TableColumn("Duration");
        collectionColumn = new TableColumn("Collection");
        TableColumn<Video, Number> totalColumn = new TableColumn<>("Total");


        columnsMap.put(dateColumn, "org.laeq.video.column.created_at");
        columnsMap.put(pathColumn, "org.laeq.video.column.name");
        columnsMap.put(userColumn, "org.laeq.video.column.user");
        columnsMap.put(durationColumn, "org.laeq.video.column.duration");
        columnsMap.put(collectionColumn, "org.laeq.video.column.collection");
        columnsMap.put(totalColumn, "org.laeq.video.column.total");

        videoTable.getColumns().addAll(idColumn, dateColumn, pathColumn, userColumn, durationColumn, collectionColumn, totalColumn);

        idColumn.setCellValueFactory(param -> Bindings.createIntegerBinding(()-> new Integer(param.getValue().getId())));
        dateColumn.setCellValueFactory(param -> Bindings.createStringBinding(() -> param.getValue().getCreatedFormatted()));
        pathColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        durationColumn.setCellValueFactory(cellData -> Bindings.createStringBinding(() -> cellData.getValue().getDurationFormatted()));
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
                    alert("org.laeq.title.error", "org.laeq.video.duration.error");
                }
            }
        });
    }

    private ChangeListener<String> filtering(){
        return (observable, oldValue, newValue) -> {
            model.getFilteredList().setPredicate(video -> {
                if((newValue == null || newValue.isEmpty()) && video.isEditable()){
                    return true;
                }

                String filter = newValue.toLowerCase();
                if(video.getName().toLowerCase().contains(filter) && video.isEditable()){
                    return true;
                }

                return false;
            });
        };
    }

    public void showDetails() {
        setCategoryGroup();
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

    private void setCategoryGroup(){
        if(this.model.getSelectedVideo() instanceof Video){
            IconCounterMatrice matrix = new IconCounterMatrice(this.model.getSelectedVideo().getCollection().getCategorySet());
            categoryGroupMap = matrix.getIconMap();

            categoryGroup.getChildren().addAll(matrix.getIconMap().values());
        }
    }

    public void reset() {
        titleValue.setText("");
        durationValue.setText("");
        totalValue.setText("");

        categoryGroupMap.clear();

        videoTable.refresh();
        videoTable.getSelectionModel().clearSelection();
        categoryGroup.getChildren().clear();
    }

    public void refresh() {
        videoTable.refresh();
    }
}
