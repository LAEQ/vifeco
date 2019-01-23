package org.laeq.model;

import griffon.core.artifact.GriffonModel;
import griffon.metadata.ArtifactProviderFor;
import javafx.collections.*;
import javafx.collections.transformation.FilteredList;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonModel;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.stream.Collectors;

@ArtifactProviderFor(GriffonModel.class)
public class VideoPointList extends AbstractGriffonModel {
    private final ObservableList<VideoPoint> pointList;
    private final ObservableList<VideoPoint> displayPoint;
    private Pane iconPane;

    public VideoPointList() {
        pointList = FXCollections.observableArrayList();
        displayPoint  = FXCollections.observableArrayList();


        pointList.addListener((ListChangeListener<VideoPoint>) c -> {
            while(c.next()){
                if(c.wasAdded()){
                    displayPoint.addAll(c.getAddedSubList());
                }

                if(c.wasRemoved()){
                    displayPoint.removeAll(c.getRemoved());
                }
            }
        });

        displayPoint.addListener((ListChangeListener<VideoPoint>) c -> {
            while(c.next()){
                if(c.wasAdded()){
                    c.getAddedSubList().forEach(e -> iconPane.getChildren().add(e.getIcon()));
                }

                if(c.wasRemoved()){
                    c.getRemoved().forEach(e -> iconPane.getChildren().remove(e.getIcon()));
                }
            }
        });


    }

    public void addVideoPoint(VideoPoint videoPoint) {
        pointList.add(videoPoint);
    }

    public ObservableList<VideoPoint> getDisplayPoint() {
        return displayPoint;
    }

    public ObservableList<VideoPoint> getPointList() {
        return pointList;
    }

    public void update(Duration now) {
        FilteredList<VideoPoint> filteredList = pointList.filtered(videoPoint -> videoPoint.isValid(now));

        displayPoint.retainAll(filteredList);
        filteredList.forEach( e -> {
            if (! displayPoint.contains(e)) {
                displayPoint.add(e);
            }
        });
    }

    public void init(Pane iconPane) {
        this.iconPane = iconPane;
    }
}
