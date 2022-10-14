package org.laeq.editor;

import griffon.core.event.EventRouter;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;
import org.laeq.model.Drawing;
import java.util.List;
import java.util.stream.Collectors;

public class DrawingPane extends Pane {
    EventRouter router;

    public ObservableList<Drawing> drawings = FXCollections.observableArrayList();

    public DrawingPane() {
        drawings.addListener((ListChangeListener<Drawing>) c -> {
            List<Canvas> canvasList = drawings.stream()
                    .map(drawing -> drawing.getCanvas(widthProperty().doubleValue(), heightProperty().doubleValue(), 0, 0))
                    .collect(Collectors.toList());

            getChildren().clear();
            getChildren().addAll(canvasList);
        });
    }

    public DrawingPane(Node... children) {
        super(children);
    }

    void dispose() {

    }

    public void setEventRouter(EventRouter eventRouter) {
        this.router = eventRouter;
    }
}