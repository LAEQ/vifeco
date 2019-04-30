package org.laeq.statistic;

import griffon.core.artifact.GriffonView;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.util.Duration;
import org.apache.commons.io.FileUtils;
import org.laeq.TranslatedView;
import org.laeq.TranslationService;
import org.laeq.model.Category;
import org.laeq.model.Video;
import org.laeq.model.statistic.Graph;
import org.laeq.service.statistic.StatisticException;
import org.laeq.service.statistic.StatisticService;
import org.laeq.settings.Settings;
import org.laeq.template.MiddlePaneView;
import org.laeq.ui.DialogService;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

@ArtifactProviderFor(GriffonView.class)
public class ContainerView extends TranslatedView {
    @MVCMember @Nonnull private ContainerModel model;
    @MVCMember @Nonnull private ContainerController controller;
    @MVCMember @Nonnull private MiddlePaneView parentView;
    @Inject private StatisticService statService;
    @Inject private DialogService dialogService;

    @FXML private TableView<Video> videoTable;
    @FXML private Button compareActionTarget;
    @FXML private GridPane gridResult;
    @FXML private AnchorPane visualTab;
    @FXML private Spinner<Integer> durationSpinner;
    @FXML private WebView statView;
    @FXML private Label durationStepLabel;

    private WebEngine webEngine;
    private TranslationService translationService;

    @Override
    public void initUI() {
        Node node = loadFromFXML();
        parentView.addMVCGroup(getMvcGroup().getMvcId(), node);
        connectActions(node, controller);
    }

    public void init(){
        TableColumn<Video, Number> idColumn = new TableColumn<>("id");
        TableColumn<Video, String> pathColumn = new TableColumn("");
        TableColumn<Video, String> collectionColumn = new TableColumn("");
        TableColumn<Video, Number> importColumn = new TableColumn<>("");

        videoTable.getColumns().addAll(idColumn, pathColumn, collectionColumn, importColumn);

        idColumn.setCellValueFactory(param -> Bindings.createIntegerBinding(()-> new Integer(param.getValue().getId())));
        pathColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        collectionColumn.setCellValueFactory(cellData -> cellData.getValue().getCollection().nameProperty());
        importColumn.setCellValueFactory(param -> Bindings.createIntegerBinding(() -> getTotalImports(param.getValue())));

        columnsMap.put(pathColumn, "org.laeq.statistic.column.name");
        columnsMap.put(collectionColumn, "org.laeq.statistic.column.collection");
        columnsMap.put(importColumn, "org.laeq.statistic.column.import");
        columnsMap.put(collectionColumn, "org.laeq.video.column.collection");
        textFields.put(compareActionTarget, "org.laeq.statistic.btn.compare");
        textFields.put(durationStepLabel, "org.laeq.statistic.label.duration_step");

        videoTable.setItems(this.model.getVideos());

        durationSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 3600));
        durationSpinner.getValueFactory().setValue(model.getPrefs().durationStep);

        durationSpinner.valueProperty().addListener((observable, oldValue, newValue) -> {
            model.getPrefs().durationStep = newValue;
            controller.savePreferences();
        });

        loadStatisticPage();

        videoTable.setOnMouseClicked(event -> {
            webEngine.executeScript("reset()");

            Video video = videoTable.getSelectionModel().getSelectedItem();

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
                        System.out.println(path1.toFile());
                        String addFunction = String.format("addJsonContent(%s)", content);
                        webEngine.executeScript(addFunction);

                    } catch (IOException e) {
                        getLog().error(e.getMessage());
                    }
                });

            } catch (IOException e) {
                getLog().error(e.getMessage());
            }

            String jsonFunction = String.format("render()");
            webEngine.executeScript(jsonFunction);
        });

        setTranslatedText();

        model.durationStepProperty().bind(durationSpinner.valueProperty());
    }

    public void changeLocale() {
        runInsideUISync(() -> {
            setTranslatedText();
        });
    }

    public void loadStatisticPage(){
        webEngine = statView.getEngine();
//        String aboutPath = String.format("html/statistic_%s.html", preferenceService.getPreferences().locale.getLanguage());
        String aboutPath = String.format("html/statistic_en.html", model.getPrefs().locale.getLanguage());
        webEngine.load(getClass().getClassLoader().getResource(aboutPath).toExternalForm());
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
        try {
            translationService = new TranslationService(getClass().getClassLoader().getResourceAsStream("messages/messages.json"), model.getPrefs().locale);
        } catch (IOException e) {
            getLog().error("Cannot load file messages.json");
        }

        try{
            textFields.entrySet().forEach(t -> {
                t.getKey().setText(translationService.getMessage(t.getValue()));
            });

            columnsMap.entrySet().forEach( t -> {
                t.getKey().setText(translationService.getMessage(t.getValue()));
            });

        } catch (Exception e){
            getLog().error(e.getMessage());
        }
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
