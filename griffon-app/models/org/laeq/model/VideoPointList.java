package org.laeq.model;

import griffon.core.artifact.GriffonModel;
import griffon.metadata.ArtifactProviderFor;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.transformation.FilteredList;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonModel;

@ArtifactProviderFor(GriffonModel.class)
public class VideoPointList extends AbstractGriffonModel {
    private final ObservableList<VideoPoint> pointList;
    private final ObservableMap<VideoPoint, Group> displayPointMap;
    private Pane iconPane;

    public VideoPointList() {
        pointList = FXCollections.observableArrayList();
        displayPointMap = FXCollections.observableHashMap();

        displayPointMap.addListener((MapChangeListener<VideoPoint, Group>) change -> {
            if(change.wasRemoved()){
                iconPane.getChildren().remove(change.getValueRemoved());
            } else if (change.wasAdded()){
                iconPane.getChildren().add(change.getValueAdded());
            }
        });
    }

    public void addVideoPoint(VideoPoint videoPoint){
        pointList.add(videoPoint);
        displayPointMap.put(videoPoint, new Group());
    }

    public ObservableList<VideoPoint> getPointList() {
        return pointList;
    }

    public ObservableMap<VideoPoint, Group> getDisplayPointMap() {
        return displayPointMap;
    }

    public void updatePane(Duration now) {
        FilteredList<VideoPoint> filteredList = pointList.filtered(videoPoint -> videoPoint.isValid(now) == false);

        displayPointMap.keySet().removeAll(filteredList);
    }

    public void init(Pane iconPane){
        this.iconPane = iconPane;
    }
}