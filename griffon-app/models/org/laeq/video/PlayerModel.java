package org.laeq.video;

import griffon.core.artifact.GriffonModel;
import griffon.metadata.ArtifactProviderFor;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonModel;
import org.laeq.model.*;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;

@ArtifactProviderFor(GriffonModel.class)
public class PlayerModel extends AbstractGriffonModel {
    private Boolean isPlaying = false;
    private Video video;
    private ObservableMap<Point, Icon> pointIconObservableMap;

    public PlayerModel(){
        pointIconObservableMap = FXCollections.observableHashMap();
    }

    public void setVideo(Video video) {
        this.video = video;
    }

    public void addPoint(Point point) {
        video.addPoint(point);
    }

//    public Optional<Category> getCategory(String shortcut) {
//        return video.getVideo().getCategoryCollection().getCategorySet().stream().filter(category -> category.getShortcut().equals(shortcut)).findFirst();
//    }

    public User getUser() {
        return video.getUser();
    }

    public void setIsPlaying(Boolean value){
        isPlaying = value;
    }

    public boolean isIsPlaying() {
        return isPlaying;
    }

    public Category debugCategory() {
        return video.getCategoryCollection().getCategorySet().iterator().next();
    }

    public ObservableMap<Point, Icon> getObservableMap() {
        return pointIconObservableMap;
    }

//    public void removePoint(Point point) {
//        videoPoints.removeIf(videoPoint -> videoPoint.getPoint().equals(point));
//    }
//    public void addVideoPoint(VideoPoint newPoint) {
//        videoPoints.add(newPoint);
//    }


//    public Iterator<VideoPoint> displayIter(Duration currentTime) {
//        return display(currentTime).iterator();
//    }

//    public SortedSet<VideoPoint> display(Duration currentTime){
//        VideoPoint start = new VideoPoint(this.size.getValue(), null, new Point(0, 0, currentTime.subtract(Duration.seconds(this.duration.getValue())), null, null, null));
//        VideoPoint end = new VideoPoint(this.size.getValue(), null, new Point(0, 0, currentTime, null, null, null));
//        return videoPoints.subSet(start, end);
//    }

}