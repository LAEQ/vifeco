package org.laeq;

import griffon.core.artifact.GriffonView;
import griffon.inject.MVCMember;
import griffon.javafx.support.JavaFXUtils;
import griffon.metadata.ArtifactProviderFor;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;
import org.laeq.model.Category;
import org.laeq.model.Video;
import org.laeq.model.statistic.Graph;
import org.laeq.model.statistic.Tarjan;
import org.laeq.statistic.StatisticTimeline;
import org.laeq.template.MiddlePaneView;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ArtifactProviderFor(GriffonView.class)
public class StatisticView extends AbstractJavaFXGriffonView {
    @MVCMember @Nonnull private StatisticModel model;
    @MVCMember @Nonnull private StatisticController controller;
    @MVCMember @Nonnull private MiddlePaneView parentView;
    @Inject private StatisticService statService;

    @FXML private TableView<Video> videoTable;
    @FXML private TableColumn<Video, CheckBox> select;
    @FXML private TableColumn<Video, String> name;
    @FXML private TableColumn<Video, String> user;
    @FXML private TableColumn<Video, String> collection;
    @FXML private TableColumn<Video, Number> total;


    @FXML private Button compareActionTarget;
    @FXML private AnchorPane visualTab;
    @FXML private Spinner<Integer> durationSpinner;
    @FXML private Label durationStepLabel;

    @FXML private TableView<Tarjan> table;
    @FXML private TableColumn<Tarjan, String> category;
    @FXML private TableColumn<Tarjan, String> video1Col;
    @FXML private TableColumn<Tarjan, String> video2Col;

    TableColumn<Tarjan, Number> v1Total = new TableColumn<>();
    TableColumn<Tarjan, Number> v1Lonely = new TableColumn<>();
    TableColumn<Tarjan, String> v1Percent = new TableColumn<>();

    TableColumn<Tarjan, Number> v2Total = new TableColumn<>();
    TableColumn<Tarjan, Number> v2Lonely = new TableColumn<>();
    TableColumn<Tarjan, String> v2Percent = new TableColumn<>();


    @Override
    public void initUI() {
        Node node = loadFromFXML();
        parentView.addMVCGroup(getMvcGroup().getMvcId(), node);
        connectActions(node, controller);
        connectMessageSource(node);


        v1Total.setText(getApplication().getMessageSource().getMessage("statistic.column.total"));
        v2Total.setText(getApplication().getMessageSource().getMessage("statistic.column.total"));
        v1Lonely.setText(getApplication().getMessageSource().getMessage("statistic.column.unmatched"));
        v2Lonely.setText(getApplication().getMessageSource().getMessage("statistic.column.unmatched"));
        v1Percent.setText(getApplication().getMessageSource().getMessage("statistic.column.percent"));
        v2Percent.setText(getApplication().getMessageSource().getMessage("statistic.column.percent"));


        category.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().category.getName()));
        video1Col.getColumns().addAll(v1Total, v1Lonely, v1Percent);
        video2Col.getColumns().addAll(v2Total, v2Lonely, v2Percent);

        v1Total.setCellValueFactory(cellData-> new ReadOnlyIntegerWrapper(cellData.getValue().getSummaryVideo1().getMatched()));
        v1Lonely.setCellValueFactory(cellData-> new ReadOnlyIntegerWrapper(cellData.getValue().getSummaryVideo1().getLonely()));
        v1Percent.setCellValueFactory(cellData-> new ReadOnlyStringWrapper(String.format("%.2f", cellData.getValue().getSummaryVideo1().getPercent())));

        v2Total.setCellValueFactory(cellData-> new ReadOnlyIntegerWrapper(cellData.getValue().getSummaryVideo2().getMatched()));
        v2Lonely.setCellValueFactory(cellData-> new ReadOnlyIntegerWrapper(cellData.getValue().getSummaryVideo2().getMatched()));
        v2Percent.setCellValueFactory(cellData-> new ReadOnlyStringWrapper(String.format("%.2f", cellData.getValue().getSummaryVideo2().getPercent())));

        init();
    }


    public void init(){
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

    }

    public void display(StatisticService statService){
        table.setItems(FXCollections.observableArrayList(statService.getTarjanDiff()));
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
