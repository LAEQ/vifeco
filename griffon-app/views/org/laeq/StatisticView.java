package org.laeq;

import griffon.core.artifact.GriffonView;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.Callback;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;
import org.laeq.model.Video;
import org.laeq.model.statistic.MatchedPoint;
import org.laeq.model.statistic.Tarjan;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@ArtifactProviderFor(GriffonView.class)
public class StatisticView extends AbstractJavaFXGriffonView {
    @MVCMember @Nonnull private StatisticModel model;
    @MVCMember @Nonnull private StatisticController controller;
    @MVCMember @Nonnull private VifecoView parentView;
    @Inject private StatisticService statService;

    @FXML private TableView<Video> videoTable;
    @FXML private TableColumn<Video, CheckBox> select;
    @FXML private TableColumn<Video, String> name;
    @FXML private TableColumn<Video, String> user;
    @FXML private TableColumn<Video, String> collection;
    @FXML private TableColumn<Video, Number> total;

    @FXML private Label videoNameLabel;
    @FXML private Label user1Label;
    @FXML private Label user2Label;
    @FXML private Label collectionLabel;
    @FXML private Label durationLabel;

    @FXML private Button compareActionTarget;
    @FXML private AnchorPane visualTab;
    @FXML private TextField duration;
    @FXML private Label durationStepLabel;

    @FXML private TableView<Tarjan> table;
    @FXML private TableColumn<Tarjan, String> category;
    @FXML private TableColumn<Tarjan, String> video1Col;
    @FXML private TableColumn<Tarjan, String> video2Col;
    @FXML private TableColumn<Tarjan, String> videoOverallCol;

    @FXML private Accordion accordion;
    @FXML private TitledPane chartTitled;
    @FXML private ScrollPane chartAccordion;
    @FXML private Pane tableAccordion;
    @FXML private Pane concordanceIndexAccordion;

    @FXML private TableView<MatchedPoint> tableAcc;
    @FXML private TableColumn<MatchedPoint, String> tableAccPt1;
    @FXML private TableColumn<MatchedPoint, String> tableAccPt2;
    @FXML private TableColumn<MatchedPoint, Void> tableAccAction;

    TableColumn<Tarjan, Number> v1Total = new TableColumn<>();
    TableColumn<Tarjan, Number> v1Lonely = new TableColumn<>();
    TableColumn<Tarjan, String> v1Percent = new TableColumn<>();

    TableColumn<Tarjan, Number> v2Total = new TableColumn<>();
    TableColumn<Tarjan, Number> v2Lonely = new TableColumn<>();
    TableColumn<Tarjan, String> v2Percent = new TableColumn<>();

    TableColumn<Tarjan, Number> vOverallMatched = new TableColumn<>();
    TableColumn<Tarjan, Number> vOverallUnMatched = new TableColumn<>();
    TableColumn<Tarjan, String> vOverallConcordanceIndex = new TableColumn<>();

    @Override
    public void initUI() {
        Node node = loadFromFXML();
        parentView.middle.getChildren().add(node);
        connectActions(node, controller);
        connectMessageSource(node);

        v1Total.setText(getApplication().getMessageSource().getMessage("statistic.column.matched"));
        v2Total.setText(getApplication().getMessageSource().getMessage("statistic.column.matched"));
        v1Lonely.setText(getApplication().getMessageSource().getMessage("statistic.column.unmatched"));
        v2Lonely.setText(getApplication().getMessageSource().getMessage("statistic.column.unmatched"));
        v1Percent.setText(getApplication().getMessageSource().getMessage("statistic.column.percent"));
        v2Percent.setText(getApplication().getMessageSource().getMessage("statistic.column.percent"));
        vOverallMatched.setText(getApplication().getMessageSource().getMessage("statistic.column.matched"));
        vOverallUnMatched.setText(getApplication().getMessageSource().getMessage("statistic.column.unmatched"));
        vOverallConcordanceIndex.setText(getApplication().getMessageSource().getMessage("statistic.column.concordance_index"));

        category.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().category.getName()));
        video1Col.getColumns().addAll(v1Total, v1Lonely, v1Percent);
        video2Col.getColumns().addAll(v2Total, v2Lonely, v2Percent);
        videoOverallCol.getColumns().addAll(vOverallMatched, vOverallUnMatched, vOverallConcordanceIndex);

        vOverallConcordanceIndex.setPrefWidth(145);

        v1Total.setCellValueFactory(cellData-> new ReadOnlyIntegerWrapper(cellData.getValue().getSummaryVideo1().getMatched()));
        v1Lonely.setCellValueFactory(cellData-> new ReadOnlyIntegerWrapper(cellData.getValue().getSummaryVideo1().getLonely()));
        v1Percent.setCellValueFactory(cellData-> new ReadOnlyStringWrapper(String.format("%.2f", cellData.getValue().getSummaryVideo1().getPercent())));

        v2Total.setCellValueFactory(cellData-> new ReadOnlyIntegerWrapper(cellData.getValue().getSummaryVideo2().getMatched()));
        v2Lonely.setCellValueFactory(cellData-> new ReadOnlyIntegerWrapper(cellData.getValue().getSummaryVideo2().getLonely()));
        v2Percent.setCellValueFactory(cellData-> new ReadOnlyStringWrapper(String.format("%.2f", cellData.getValue().getSummaryVideo2().getPercent())));

        vOverallMatched.setCellValueFactory(cellData -> new ReadOnlyIntegerWrapper(cellData.getValue().getSummaryOverallMatched()));
        vOverallUnMatched.setCellValueFactory(cellData -> new ReadOnlyIntegerWrapper(cellData.getValue().getSummaryOverallUnMatched()));
        vOverallConcordanceIndex.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(String.format("%.2f", cellData.getValue().getSummaryOverallConcordanceIndex() * 100)));


        tableAccPt1.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getPt1Formatted()));
        tableAccPt2.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getPt2Formatted()));

        init();
    }

    public void init(){
        duration.setText("10");
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

        model.duration.bindBidirectional(durationLabel.textProperty());
        duration.textProperty().addListener((observable, oldValue, newValue) -> {
            try{
                int step = Integer.parseInt(newValue);
                if(step < 0){
                    throw new Exception();
                } else {
                    model.durationStep.set(step);
                }
            }catch (Exception e){
                getApplication().getEventRouter().publishEvent("duration.step.invalid ");
            }
        });

        table.setItems(model.tarjans);

        table.setOnMouseClicked(evt ->{
            displayChart(table.getSelectionModel().getSelectedItem());
        });

        model.videoName.bindBidirectional(videoNameLabel.textProperty());
        model.user1.bindBidirectional(user1Label.textProperty());
        model.user2.bindBidirectional(user2Label.textProperty());
        model.collection.bindBidirectional(collectionLabel.textProperty());
        model.duration.bindBidirectional(durationLabel.textProperty());

        accordion.setExpandedPane(chartTitled);

        tableAccAction.setCellFactory(addActions());
    }

    private Callback<TableColumn<MatchedPoint, Void>, TableCell<MatchedPoint, Void>> addActions() {
        return param -> {
            final  TableCell<MatchedPoint, Void> cell = new TableCell<MatchedPoint, Void>(){
                Button view = new Button("view");

                Group btnGroup = new Group();
                {
                    view.getStyleClass().addAll("btn", "btn-sm", "btn-info");
                    view.setLayoutX(5);
                    btnGroup.getChildren().addAll(view);
                    view.setOnAction(event -> {
                        MatchedPoint mp = tableAcc.getItems().get(getIndex());
                        controller.displayMatchedPoint(mp);
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

    private void displayChart(Tarjan tarjan){
        chartAccordion.setContent(null);

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        StackedBarChart<String, Number> sbc = new StackedBarChart<String, Number>(xAxis, yAxis);

        Map<String, Integer> serie_1 = tarjan.getSerieVideo1(60);
        XYChart.Series<String, Number> chartSerie_1 = new XYChart.Series<String, Number>();
        chartSerie_1.setName("Video 1");
        serie_1.forEach((s, total) -> {
            chartSerie_1.getData().add(new XYChart.Data<>(s, total));
        });

        Map<String, Integer> serie_2 = tarjan.getSerieVideo2(60);
        XYChart.Series<String, Number> chartSerie_2 = new XYChart.Series<String, Number>();
        chartSerie_2.setName("Video 2");
        serie_2.forEach((s, total) -> {
            chartSerie_2.getData().add(new XYChart.Data<>(s, total));
        });

        Map<String, Integer> serie_3 = tarjan.getSerieMatched(60);
        XYChart.Series<String, Number> chartSerie_3 = new XYChart.Series<String, Number>();
        chartSerie_3.setName("Matched");
        serie_3.forEach((s, total) -> {
            chartSerie_3.getData().add(new XYChart.Data<>(s, total));
        });

        xAxis.setLabel("Seconds");
        xAxis.setCategories(FXCollections.observableArrayList(serie_1.keySet()));

        sbc.setMinWidth(serie_1.keySet().size() * 20);
        sbc.getData().addAll(chartSerie_1, chartSerie_2, chartSerie_3);

        chartAccordion.setContent(sbc);

        tableAcc.getItems().clear();

        List<MatchedPoint> sorted = tarjan.matchedPoints.stream()
                .sorted((o1, o2) -> (int) o1.getStarts().get(0).subtract(o2.getStarts().get(0)).toMillis())
                .collect(Collectors.toList());

        tableAcc.getItems().addAll(FXCollections.observableArrayList(sorted));

        concordanceIndexAccordion.getChildren().clear();

        Map<String, Double> serie_concordanceIndex = new LinkedHashMap<>();

        serie_1.forEach((s, integer) -> {
            Double intersec = Double.valueOf(serie_3.get(s));
            Double union = Double.valueOf(serie_1.get(s) + serie_2.get(s)) + intersec;

            if(union == 0){
                serie_concordanceIndex.put(s, 100d);
            } else {
                serie_concordanceIndex.put(s, intersec / union * 100);
            }
        });


        CategoryAxis xAxisCI = new CategoryAxis();
        xAxisCI.setLabel(translate("z.concordance_index"));
        NumberAxis yAxisCI = new NumberAxis(0, 110, 10);

        yAxisCI.setLabel("%");

        XYChart.Series series = new XYChart.Series();
        series.setName(translate("z.concordance_index"));

        LineChart linechart = new LineChart(xAxisCI, yAxisCI);

        linechart.setMinWidth(serie_1.keySet().size() * 20);

        serie_concordanceIndex.forEach((s, value) -> {
            series.getData().add(new XYChart.Data(s, value));
        });

        linechart.getData().add(series);

        concordanceIndexAccordion.getChildren().add(linechart);

    }

    private String translate(String key){
        return getApplication().getMessageSource().getMessage(key);
    }

    public void reset() {
        tableAcc.getItems().clear();
        chartAccordion.setContent(null);
    }
}
