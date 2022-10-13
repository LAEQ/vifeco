package org.laeq.editor;

import griffon.core.artifact.GriffonModel;
import griffon.metadata.ArtifactProviderFor;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonModel;
import org.laeq.model.Drawing;

import java.util.List;
import java.util.stream.Collectors;

@ArtifactProviderFor(GriffonModel.class)
public class DrawingModel extends AbstractGriffonModel {
    public ObservableList<Drawing> drawingList = FXCollections.observableArrayList();

    public SimpleStringProperty name = new SimpleStringProperty(this, "name", "line");
    public SimpleStringProperty color = new SimpleStringProperty(this, "color", "#00FF00");
    public Point2D start;
    public Point2D end;

    public void reset(){
        start = null;
        end = null;
    }

    public List<Drawing> getVisibleDrawing() {
        return drawingList.stream().filter(drawing -> drawing.activeProperty().getValue()).collect(Collectors.toList());
    }
}
