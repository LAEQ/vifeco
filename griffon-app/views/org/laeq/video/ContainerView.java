package org.laeq.video;

import griffon.core.artifact.GriffonView;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.text.Text;
import javafx.util.Callback;
import javafx.util.Duration;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;
import org.kordamp.ikonli.fontawesome.FontAwesome;
import org.kordamp.ikonli.javafx.FontIcon;
import org.laeq.graphic.Color;
import org.laeq.graphic.icon.CategoryMatrice;
import org.laeq.graphic.icon.IconAbstract;
import org.laeq.graphic.icon.IconType;
import org.laeq.model.*;
import org.laeq.template.MiddlePaneView;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ArtifactProviderFor(GriffonView.class)
public class ContainerView extends AbstractJavaFXGriffonView {
    @MVCMember @Nonnull private ContainerController controller;
    @MVCMember @Nonnull private ContainerModel model;
    @MVCMember @Nonnull private MiddlePaneView parentView;

    @FXML private TableView<Video> videoTable;
    @FXML private Text titleTxt;
    @FXML private Text durationTxt;
    @FXML private Text totalTxt;
    @FXML private Text lastPointTxt;
    @FXML private Text titleValue;
    @FXML private Text durationValue;
    @FXML private Text totalValue;
    @FXML private Text lastPointValue;
    @FXML private Group categoryGroup;

    Map<Category, IconAbstract> categoryGroupMap;

    private TableColumn<Video, User> userColumn;
    private TableColumn<Video, Collection> collectionColumn;

    @Override
    public void initUI() {
        Node node = loadFromFXML();
        parentView.addMVCGroup(getMvcGroup().getMvcId(), node);
        connectActions(node, controller);
        init();
    }

    private void init(){
        reset();

        categoryGroupMap = new HashMap<>();
        videoTable.setEditable(true);

        TableColumn<Video, Void> selectBox = new TableColumn<>("#");
        selectBox.setPrefWidth(10);
        TableColumn<Video, String> dateColumn = new TableColumn<>("Created At");
        TableColumn<Video, String> pathColumn = new TableColumn("Name");
        userColumn = new TableColumn<>("User");
        TableColumn<Video, String> durationColumn = new TableColumn("Duration");
        collectionColumn = new TableColumn("Collection");
        TableColumn<Video, Number> totalColumn = new TableColumn<>("Total");
        TableColumn<Video, Number> lastPointColumn = new TableColumn<>("Last point");

        videoTable.getColumns().addAll(selectBox, dateColumn, pathColumn, userColumn, durationColumn, collectionColumn, totalColumn, lastPointColumn);

        dateColumn.setCellValueFactory(param -> Bindings.createStringBinding(() -> param.getValue().getCreatedFormatted()));
        pathColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        durationColumn.setCellValueFactory(cellData -> Bindings.createStringBinding(() -> cellData.getValue().getDurationFormatted()));
        totalColumn.setCellValueFactory(cellData -> cellData.getValue().totalProperty());

        videoTable.setItems(this.model.getVideoList());
        videoTable.getSelectionModel().selectedItemProperty().addListener(observable -> {
            model.setSelectedVideo(videoTable.getSelectionModel().getSelectedItem());
            controller.showDetail();
        });
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

        userColumn.setCellValueFactory(param -> new SimpleObjectProperty<User>(param.getValue().getUser()));
        userColumn.setMinWidth(140);
        userColumn.setCellFactory(ComboBoxTableCell.forTableColumn(users));
        userColumn.setOnEditCommit(event -> controller.updateUser(event));

        ObservableList<Collection> collections = FXCollections.observableArrayList(model.getCollectionSet());
        collectionColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getCollection()));
        collectionColumn.setMinWidth(140);
        collectionColumn.setCellFactory(ComboBoxTableCell.forTableColumn(collections));
        collectionColumn.setOnEditCommit(event -> controller.updateCollection(event));

        CategoryMatrice matrice = new CategoryMatrice(model.getCategorySet(), IconType.COUNT);
        categoryGroupMap = matrice.getIconMap();

        categoryGroup.getChildren().addAll(matrice.getIconMap().values());
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
//                        controller.export(video);
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

    public void reset() {
        titleValue.setText("");
        durationValue.setText("");
        totalValue.setText("");
        lastPointValue.setText("");

        model.getCategorySet().forEach(c -> {
            categoryGroupMap.get(c).setOpacity(0.4);
            categoryGroupMap.get(c).colorize(Color.white, Color.white);
            categoryGroupMap.get(c).setText("-");
        });
    }
}
