package org.laeq.video;

import griffon.core.artifact.GriffonModel;
import griffon.metadata.ArtifactProviderFor;
import javafx.util.Duration;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonModel;
import org.laeq.model.*;

import java.util.*;

@ArtifactProviderFor(GriffonModel.class)
public class PlayerModel extends AbstractGriffonModel {
    private Boolean isPlaying = false;
    private Video video;
//    private SortedMap<Point, Icon> pointToIcons;
    private Duration duration;

    public PlayerModel(){
//        pointToIcons = new TreeMap<>();
        duration = Duration.millis(10000);
    }

    public void setDuration(double duree) {
        this.duration = Duration.millis(duree);
    }

//    public SortedMap<Point, Icon> getPointToIcons() {
//        return pointToIcons;
//    }

    public void setVideo(Video video) {
        this.video = video;
//        this.video.getPointSet().forEach(point -> {
//            pointToIcons.put(point, generateIcon(point));
//        });
    }

    private Icon generateIcon(Point point){
        return new Icon(point.getCategory(), 100);
    }

    public void addPoint(Point point) {
//        pointToIcons.put(point,generateIcon(point));
        video.addPoint(point);
    }

    public Optional<Category> getCategory(String shortcut) {
        return video.getCategoryCollection().getCategorySet().stream().filter(category -> category.getShortcut().equals(shortcut)).findFirst();
    }

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

//    public Iterator<Icon> displayIter(Duration currentTime) {
//        return displayPoints(currentTime).iterator();
//    }
    public SortedSet<Point> displayPoints(Duration currentTime){
        Point start = new Point();
        start.setStart(currentTime.subtract(duration));
        Point end = new Point();
        end.setStart(currentTime);

        return video.getPointSet().subSet(start, end);
    }
}