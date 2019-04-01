package org.laeq.statistic;

import griffon.core.artifact.GriffonView;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import javafx.util.Duration;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;
import org.laeq.model.Category;
import org.laeq.model.Video;
import org.laeq.service.statistic.StatisticException;
import org.laeq.service.statistic.StatisticService;
import org.laeq.template.MiddlePaneView;
import org.laeq.ui.DialogService;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
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
        durationSlider.setMin(0);
        durationSlider.setMax(60);
        durationSlider.setMajorTickUnit(10);
        durationSlider.setShowTickMarks(true);
        durationSlider.setShowTickLabels(true);

        durationSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            try{
                BigDecimal bd = new BigDecimal(newValue.toString());
                bd = bd.setScale(0, RoundingMode.HALF_EVEN);
                durationSlider.setValue(bd.intValueExact());

                compare(bd.intValueExact());

            } catch (Exception e){
                getLog().error(e.getMessage());
            }

        });

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
            compare((int) durationSlider.getValue());
        });

        gridResult.setPadding(new Insets(10,10,10,10));
        gridResult.setVgap(5);
        gridResult.setHgap(5);

    }

    private void compare(int step){
        gridResult.getChildren().clear();
        gridResult.getColumnConstraints().clear();

        gridResult.add(new Label("Category"), 0,0);
        gridResult.add(new Label("Video A - Total"), 1,0);
        gridResult.add(new Label("Delta A"), 2,0);
        gridResult.add(new Label("% "), 3,0);
        gridResult.add(new Label("Video B - Total"), 4,0);
        gridResult.add(new Label("Delta B"), 5,0);
        gridResult.add(new Label("%"), 6,0);

        gridResult.getColumnConstraints().addAll(getColumnConstraints());



        Video video1 = this.model.getVideos().get(0);
        Video video2 = this.model.getVideos().get(1);

        try {
            statService.setVideos(video1, video2);
            statService.setDurationStep(Duration.seconds(step));
            Map<Category, Map<Video, Long>> result = statService.analyse();


            final int[] rowIndex = new int[]{1};

            result.entrySet().forEach(e -> {
                gridResult.add(new Label(e.getKey().getName()), 0, rowIndex[0]);

                long totalA = video1.getPointSet().stream().filter(point -> point.getCategory().equals(e.getKey())).count();

                gridResult.add(new Label(String.valueOf(totalA)), 1, rowIndex[0]);
                gridResult.add(new Label(e.getValue().get(video1).toString()), 2, rowIndex[0]);
                gridResult.add(new Label(rounder((e.getValue().get(video1) / ((double)totalA) * 100)) + "%"), 3, rowIndex[0]);

                long totalB = video2.getPointSet().stream().filter(point -> point.getCategory().equals(e.getKey())).count();

                gridResult.add(new Label(String.valueOf(totalB)), 4, rowIndex[0]);
                gridResult.add(new Label(e.getValue().get(video2).toString()), 5, rowIndex[0]);
                gridResult.add(new Label(rounder(e.getValue().get(video2) / ((double)totalB) * 100) + "%"), 6, rowIndex[0]);
                rowIndex[0]++;
            });



        }catch (StatisticException e){
            dialogService.simpleAlert("key.title.error", e.getMessage());
        }
    }

    private String rounder(double value){
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(0, RoundingMode.HALF_EVEN);

        return bd.toString();
    }

    private List<ColumnConstraints> getColumnConstraints(){

        List<ColumnConstraints> colConstList = new ArrayList<>();

        ColumnConstraints colConst1 = new ColumnConstraints();
        colConst1.setMinWidth(200);
        colConst1.setPrefWidth(250);
        colConst1.setMaxWidth(300);
        colConstList.add(colConst1);

        ColumnConstraints colConst2 = new ColumnConstraints();
        colConst2.setMinWidth(50);
        colConst2.setPrefWidth(100);
        colConst2.setMaxWidth(200);

        colConstList.add(colConst2);
        colConstList.add(colConst2);
        colConstList.add(colConst2);
        colConstList.add(colConst2);
        colConstList.add(colConst2);
        colConstList.add(colConst2);

        return colConstList;
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
