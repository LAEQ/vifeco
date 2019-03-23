package org.laeq.statistic;

import griffon.core.artifact.GriffonView;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import javafx.util.Duration;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;
import org.laeq.model.Category;
import org.laeq.model.Video;
import org.laeq.model.statistic.Vertex;
import org.laeq.service.statistic.StatisticException;
import org.laeq.service.statistic.StatisticService;
import org.laeq.template.MiddlePaneView;
import org.laeq.ui.DialogService;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;
import java.util.Map;

@ArtifactProviderFor(GriffonView.class)
public class ContainerView extends AbstractJavaFXGriffonView {
    @MVCMember @Nonnull private ContainerModel model;
    @MVCMember @Nonnull private ContainerController controller;
    @MVCMember @Nonnull private MiddlePaneView parentView;
    @Inject private StatisticService statService;
    @Inject private DialogService dialogService;

    @FXML private TableView<Video> videoTable;
    @FXML private Button compareBtn;
    @FXML private Slider durationSlider;
    @FXML private GridPane gridResult;


    private BidiMap<CheckBox, Video> selectBoxes;



    @Override
    public void initUI() {
        Node node = loadFromFXML();
        parentView.addMVCGroup(getMvcGroup().getMvcId(), node);
        connectActions(node, controller);
    }

    public void init(){
        selectBoxes = new DualHashBidiMap<>();

        TableColumn<Video, Void> selectBox = new TableColumn<>("#");
        selectBox.setMinWidth(12);
        TableColumn<Video, String> pathColumn = new TableColumn("Name");
        TableColumn<Video, String> userColumn = new TableColumn<>("User");
        TableColumn<Video, String> durationColumn = new TableColumn("Duration");
        TableColumn<Video, String> collectionColumn = new TableColumn("Collection");
        TableColumn<Video, Number> totalColumn = new TableColumn<>("Total");

        videoTable.getColumns().addAll(selectBox, pathColumn, durationColumn, userColumn, collectionColumn, totalColumn);

        selectBox.setCellFactory(addSelectBox());
        pathColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        userColumn.setCellValueFactory(cellData -> cellData.getValue().getUser().getName());
        collectionColumn.setCellValueFactory(cellData -> cellData.getValue().getCollection().nameProperty());
        durationColumn.setCellValueFactory(cellData -> cellData.getValue().durationProperty().asString());
        totalColumn.setCellValueFactory(cellData -> cellData.getValue().totalProperty());

        videoTable.setItems(this.model.getVideos());

        compareBtn.setOnMouseClicked(event -> {
            //Validate video selection
            compare();
        });

        gridResult.setPadding(new Insets(10,10,10,10));
        gridResult.setVgap(5);
        gridResult.setHgap(5);

    }

    private void compare(){
        Video video1 = this.model.getVideos().get(0);
        Video video2 = this.model.getVideos().get(1);

        try {
            statService.setVideos(video1, video2);
            statService.setDurationStep(Duration.seconds(10));
            Map<Category, List<List<Vertex>>> result = statService.execute();

            System.out.println(result);

            int rowIndex = 1;
            int columnIndex = 0;

            for(Map.Entry<Category, List<List<Vertex>>> entry: result.entrySet()){
                gridResult.add(new Label(entry.getKey().getName()), columnIndex, rowIndex);

                List<List<Vertex>> vertices = entry.getValue();

                int[] totalA = new int[]{0};
                int[] totalB = new int[]{0};








                columnIndex = 0;
                rowIndex++;
            }

        }catch (StatisticException e){
            dialogService.simpleAlert("key.title.error", e.getMessage());
        }
    }

    private Callback<TableColumn<Video, Void>, TableCell<Video, Void>> addSelectBox() {
        return param -> {
            final  TableCell<Video, Void> cell = new TableCell<Video, Void>(){
                CheckBox selectBox = new CheckBox("");

                Group btnGroup = new Group();
                {
                    Video video = videoTable.getSelectionModel().getSelectedItems().get(getIndex());
                    if(video != null){
                        btnGroup.getChildren().addAll(selectBox);
                        selectBoxes.put(selectBox, video);
                    }
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
