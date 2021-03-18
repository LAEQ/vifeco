package org.laeq.editor;

import griffon.core.artifact.GriffonView;
import griffon.core.i18n.MessageSource;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.util.Callback;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;
import org.laeq.HelperService;
import org.laeq.model.Icon;
import org.laeq.model.Point;
import org.laeq.model.Video;
import org.laeq.model.comparator.DurationComparator;

import javax.annotation.Nonnull;
import javax.inject.Inject;

@ArtifactProviderFor(GriffonView.class)
public class TimelineView extends AbstractJavaFXGriffonView {
    @MVCMember @Nonnull private TimelineController controller;
    @MVCMember @Nonnull private TimelineModel model;
    @MVCMember @Nonnull private Video video;

    @MVCMember @Nonnull private PlayerView parentView;

    @Inject private HelperService helperService;

    @FXML public TableView<Point> timeline;
    @FXML public TableColumn<Point, Icon> iconTD;
    @FXML public TableColumn<Point, String> startTD;
    @FXML public TableColumn<Point, Number> xTD;
    @FXML public TableColumn<Point, Number> yTD;
    @FXML public TableColumn<Point, Void>  delete;

    private MessageSource messageSource;

    @Override
    public void initUI() {
        Node node = loadFromFXML();
        parentView.timeline.getChildren().add(node);
        connectMessageSource(node);
        connectActions(node, controller);

        messageSource = getApplication().getMessageSource();

        init();
    }

    private void init() {
        iconTD.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCategory().getIcon2()));
        startTD.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStartFormatted()));
        xTD.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getX()));
        yTD.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getY()));
        delete.setCellFactory(deleteActions());

//        SortedList<Point> points = model.points.sorted(new DurationComparator());
        FXCollections.sort(model.points);
        timeline.setItems(model.points);
        timeline.setPlaceholder(new Label(""));
        timeline.getSelectionModel().selectedItemProperty().addListener(rowlistener());
//        points.comparatorProperty().bind(timeline.comparatorProperty());
    }

    private String translate(String key) {
        return messageSource.getMessage(key);
    }

    private ChangeListener<Point> rowlistener(){
        return (observable, oldValue, newValue) -> {
            controller.updateCurrentTime(newValue.getStart());
        };
    }

    private Callback<TableColumn<Point, Void>, TableCell<Point, Void>> deleteActions() {
        return param -> {
            final  TableCell<Point, Void> cell = new TableCell<Point, Void>(){
                Button delete = new Button(translate("btn.delete"));
                {
                    delete.setLayoutX(5);
                    delete.getStyleClass().addAll("btn", "btn-danger", "btn-sm");
                    delete.setOnAction(event -> controller.deletePoint(timeline.getItems().get(getIndex())));
                }

                @Override
                public void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(delete);
                    }
                }
            };

            return cell;
        };
    }
}
