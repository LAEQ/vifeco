package org.laeq.model;

import griffon.core.artifact.GriffonModel;
import griffon.metadata.ArtifactProviderFor;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Bounds;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonModel;

import javax.annotation.Nonnull;

@ArtifactProviderFor(GriffonModel.class)
public class VideoPointList extends AbstractGriffonModel {
    private final ObservableList<VideoPoint> pointList;
    private final ObservableList<VideoPoint> displayPoint;
    private Pane iconPane;

    private SimpleIntegerProperty count categor

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
                    c.getAddedSubList().forEach(e -> iconPane.getChildren().add(getPositionedIcon(e)));
                }

                if(c.wasRemoved()){
                    c.getRemoved().forEach(e -> iconPane.getChildren().remove(e.getIcon()));
                }
            }
        });
    }

    public PointIcon getPositionedIcon(VideoPoint point){
        PointIcon icon = (PointIcon)point.getIcon();

        icon.setLayoutX(point.getIconX(iconPane.getBoundsInLocal()));
        icon.setLayoutY(point.getIconY(iconPane.getBoundsInLocal()));

        return icon;
    }

    public void setPositionedIcon(VideoPoint point){
        PointIcon icon = (PointIcon) point.getIcon();

        icon.setLayoutX(point.getIconX(iconPane.getBoundsInLocal()));
        icon.setLayoutY(point.getIconY(iconPane.getBoundsInLocal()));
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

    public void init(@Nonnull Pane iconPane) {
        this.iconPane = iconPane;

        iconPane.boundsInLocalProperty().addListener((observable, oldValue, newValue) -> {
            pointList.filtered(videoPoint -> iconPane.getChildren().contains(videoPoint.getIcon())).forEach(videoPoint -> setPositionedIcon(videoPoint));
        });
    }

    public void addVideoPointTest(VideoPoint vp) {
        pointList.add(vp);
    }
}
