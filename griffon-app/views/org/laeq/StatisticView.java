package org.laeq;

import griffon.core.artifact.GriffonView;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.util.Callback;
import org.laeq.StatisticController;
import org.laeq.StatisticModel;
import org.laeq.TranslatedView;
import org.laeq.TranslationService;
import org.laeq.model.Category;
import org.laeq.model.Point;
import org.laeq.model.Video;
import org.laeq.model.statistic.Graph;
import org.laeq.service.statistic.StatisticService;
import org.laeq.statistic.StatisticTimeline;
import org.laeq.template.MiddlePaneView;
import org.laeq.ui.DialogService;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@ArtifactProviderFor(GriffonView.class)
public class StatisticView extends TranslatedView {
    @MVCMember @Nonnull private StatisticModel model;
    @MVCMember @Nonnull private StatisticController controller;
    @MVCMember @Nonnull private MiddlePaneView parentView;
    @Inject private StatisticService statService;
    @Inject private DialogService dialogService;

    @FXML private TableView<Video> videoTable;
    @FXML private TableColumn<Video, CheckBox> select;
    @FXML private TableColumn<Video, String> name;
    @FXML private TableColumn<Video, String> user;
    @FXML private TableColumn<Video, String> collection;
    @FXML private TableColumn<Video, Number> total;


    @FXML private Button compareActionTarget;
    @FXML private GridPane gridResult;
    @FXML private AnchorPane visualTab;
    @FXML private Spinner<Integer> durationSpinner;
    @FXML private Label durationStepLabel;


    @Override
    public void initUI() {
        Node node = loadFromFXML();
        parentView.addMVCGroup(getMvcGroup().getMvcId(), node);
        connectActions(node, controller);
        connectMessageSource(node);

        init();
    }

    public void init(){
//        select.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Video, Boolean>, ObservableValue<Boolean>>() {
//            @Override
//            public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Video, Boolean> param) {
//                Video video = param.getValue();
//                CheckBox checkBox = new CheckBox();
//                checkBox.selectedProperty().setValue(video.getSelected());
//
//                checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
//                    @Override
//                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
//                        video.setSelected(newValue);
//                    }
//                });
//
//                return new SimpleObjectProperty<CheckBox>(checkBox);
//            }
//        });

        select.setCellValueFactory(c -> {
            Video video = c.getValue();
            CheckBox checkBox = new CheckBox();
            checkBox.selectedProperty().setValue(video.getSelected());
            checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                video.setSelected(newValue);
            });

            return new SimpleObjectProperty<>(checkBox);
        });
        name.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().pathToName()));
        user.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getUser().toString()));
        collection.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getCollection().getName()));
        total.setCellValueFactory(cellData -> new ReadOnlyIntegerWrapper(cellData.getValue().getPoints().size()));


        videoTable.setItems(this.model.videos);
        videoTable.setEditable(true);

        durationSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 3600));
        durationSpinner.getValueFactory().setValue(model.durationStep.getValue());
        durationSpinner.getValueFactory().valueProperty().bindBidirectional(model.durationStep.asObject());


        videoTable.setOnMouseClicked(event -> {
//            String setLanguage = String.format("setLanguage('%s')", model.getPrefs().locale.getLanguage());
//            webEngine.executeScript(setLanguage);
//            webEngine.executeScript("reset()");
//
//            Point video = videoTable.getSelectionModel().getSelectedItem();
//
//            Path path = Paths.get(Settings.statisticPath);
//
//            try {
//                Stream<Path> list = Files.list(path);
//
//                list.filter(p -> p.getFileName().toString().contains(video.getName())).forEach(p -> {
//                    try {
//                        String content = FileUtils.readFileToString(p.toFile(), "UTF-8");
//                        String addFunction = String.format("addJsonContent(%s)", content);
//                        webEngine.executeScript(addFunction);
//                    } catch (IOException e) {
//                        getLog().error(e.getMessage());
//                    }
//                });
//            } catch (IOException e) {
//                getLog().error(e.getMessage());
//            }
//
//            String jsonFunction = String.format("render()");
//            webEngine.executeScript(jsonFunction);
        });

    }


    public void loadStatisticPage(){
//        webEngine = statView.getEngine();
//        String aboutPath = String.format("html/statistic_en.html", model.getPrefs().locale.getLanguage());
//        webEngine.load(getClass().getClassLoader().getResource(aboutPath).toExternalForm());
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

//        Point video1 = this.model.getVideos().get(0);
//        Point video2 = this.model.getVideos().get(1);

//        try {
//            statService.setVideos(video1, video2);
//            statService.setDurationStep(Duration.seconds(step));
//            statService.execute();
//
//            final int[] rowIndex = new int[]{1};
//
//            statService.getTarjanDiffs().entrySet().forEach(e -> {
//                gridResult.add(new Label(e.getKey().getName()), 0, rowIndex[0]);
//
//                long totalA = statService.getTotalVideoAByCategory(e.getKey());
//
//                gridResult.add(new Label(String.valueOf(totalA)), 1, rowIndex[0]);
//                gridResult.add(new Label(e.getValue().get(video1).toString()), 2, rowIndex[0]);
//
//                double percent = (totalA != 0)?  e.getValue().get(video1) / ((double)totalA) : 0;
//
//                gridResult.add(new Label(rounder(percent * 100) + "%"), 3, rowIndex[0]);
//
//                long totalB = statService.getTotalVideoBByCategory(e.getKey());
//
//                gridResult.add(new Label(String.valueOf(totalB)), 4, rowIndex[0]);
//                gridResult.add(new Label(e.getValue().get(video2).toString()), 5, rowIndex[0]);
//
//                percent = (totalB != 0)?  e.getValue().get(video2) / ((double)totalB) : 0;
//
//                gridResult.add(new Label(rounder(percent * 100) + "%"), 6, rowIndex[0]);
//                rowIndex[0]++;
//            });
//
//        }catch (StatisticException e){
//            dialogService.simpleAlert("key.title.error", e.getMessage());
//        }

        visualTab.getChildren().clear();

        Map<Category, Graph> graphMap = statService.getGraphs();

        final int[] y = new int[]{0};

        graphMap.keySet().forEach(category -> {
            StatisticTimeline timeline = statService.getStatisticTimeline(category);
            timeline.setLayoutY(y[0]);

            y[0] += 110;

            visualTab.getChildren().add(timeline);
        });
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
}
