package org.laeq.statistic;

import griffon.core.artifact.GriffonView;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.beans.binding.Bindings;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.util.Callback;
import javafx.util.Duration;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.apache.commons.io.FileUtils;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;
import org.laeq.model.Category;
import org.laeq.model.Video;
import org.laeq.model.statistic.Graph;
import org.laeq.service.statistic.StatisticException;
import org.laeq.service.statistic.StatisticService;
import org.laeq.settings.Settings;
import org.laeq.template.MiddlePaneView;
import org.laeq.ui.DialogService;
import org.laeq.user.PreferencesService;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@ArtifactProviderFor(GriffonView.class)
public class ContainerView extends AbstractJavaFXGriffonView {
    @MVCMember @Nonnull private ContainerModel model;
    @MVCMember @Nonnull private ContainerController controller;
    @MVCMember @Nonnull private MiddlePaneView parentView;
    @Inject private StatisticService statService;
    @Inject private DialogService dialogService;
    @Inject private PreferencesService preferenceService;

    @FXML private TableView<Video> videoTable;
    @FXML private Button compareBtn;
    @FXML private GridPane gridResult;
    @FXML private AnchorPane visualTab;
    @FXML private Spinner<Integer> durationSpinner;
    @FXML private WebView statView;

    private WebEngine webEngine;

    @Override
    public void initUI() {
        Node node = loadFromFXML();
        parentView.addMVCGroup(getMvcGroup().getMvcId(), node);
        connectActions(node, controller);
    }

    public void init(){
        TableColumn<Video, Number> idColumn = new TableColumn<>("#");
        TableColumn<Video, String> pathColumn = new TableColumn("Name");
        TableColumn<Video, String> collectionColumn = new TableColumn("Collection");
        TableColumn<Video, Number> filesColumn = new TableColumn<>("Imports");

        videoTable.getColumns().addAll(idColumn, pathColumn, collectionColumn, filesColumn);

        idColumn.setCellValueFactory(param -> Bindings.createIntegerBinding(()-> new Integer(param.getValue().getId())));
        pathColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        collectionColumn.setCellValueFactory(cellData -> cellData.getValue().getCollection().nameProperty());
        filesColumn.setCellValueFactory(param -> {
           return Bindings.createIntegerBinding(() -> getTotalImports(param.getValue()));
        });

        videoTable.setItems(this.model.getVideos());

        loadStatisticPage();

        videoTable.setOnMouseClicked(event -> {
            Video video = videoTable.getSelectionModel().getSelectedItem();
//            String myFunction = String.format("setVideoFile('%s, %s')", Settings.statisticPath, video.getName());
//            webEngine.executeScript(myFunction);

            Path path = Paths.get(Settings.statisticPath);

            try {
                Stream<Path> list = Files.list(path);

                list.filter(p ->{
                    if(p.getFileName().toString().contains(video.getName())){
                        return true;
                    } else {
                        return false;
                    }
                }).forEach(path1 -> {
                    try {
                        String content = FileUtils.readFileToString(path1.toFile(), "UTF-8");
                        String jsonFunction = String.format("addJsonContent(%s)", content);
                        webEngine.executeScript(jsonFunction);

                    } catch (IOException e) {
                        getLog().error(e.getMessage());
                    }
                });


            } catch (IOException e) {
                getLog().error(e.getMessage());
            }

        });
    }

    public void loadStatisticPage(){
        webEngine = statView.getEngine();
//        String aboutPath = String.format("html/statistic_%s.html", preferenceService.getPreferences().locale.getLanguage());
        String aboutPath = String.format("html/statistic_en.html", preferenceService.getPreferences().locale.getLanguage());
        webEngine.load(getClass().getClassLoader().getResource(aboutPath).toExternalForm());

//        String myFunction = String.format("setStatFolder('%s')", Settings.statisticPath);
//        webEngine.executeScript(myFunction);

    }

    private int getTotalImports(Video video){
        int total = 0;
        Iterator it = FileUtils.iterateFiles(new File(Settings.imporPath), null, false);

        while (it.hasNext()){
            File file = (File) it.next();

            if(file.getName().contains(video.getName())){
                total++;
            }
        }

        return new Integer(total);
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
            statService.execute();

            final int[] rowIndex = new int[]{1};

            statService.getTarjanDiffs().entrySet().forEach(e -> {
                gridResult.add(new Label(e.getKey().getName()), 0, rowIndex[0]);

                long totalA = statService.getTotalVideoAByCategory(e.getKey());

                gridResult.add(new Label(String.valueOf(totalA)), 1, rowIndex[0]);
                gridResult.add(new Label(e.getValue().get(video1).toString()), 2, rowIndex[0]);

                double percent = (totalA != 0)?  e.getValue().get(video1) / ((double)totalA) : 0;

                gridResult.add(new Label(rounder(percent * 100) + "%"), 3, rowIndex[0]);

                long totalB = statService.getTotalVideoBByCategory(e.getKey());

                gridResult.add(new Label(String.valueOf(totalB)), 4, rowIndex[0]);
                gridResult.add(new Label(e.getValue().get(video2).toString()), 5, rowIndex[0]);

                percent = (totalB != 0)?  e.getValue().get(video2) / ((double)totalB) : 0;

                gridResult.add(new Label(rounder(percent * 100) + "%"), 6, rowIndex[0]);
                rowIndex[0]++;
            });

        }catch (StatisticException e){
            dialogService.simpleAlert("key.title.error", e.getMessage());
        }

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
