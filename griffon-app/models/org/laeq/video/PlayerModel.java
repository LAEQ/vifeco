package org.laeq.video;

import griffon.core.artifact.GriffonModel;
import griffon.metadata.ArtifactProviderFor;
import javafx.util.Duration;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonModel;
import org.laeq.model.*;

import java.util.Optional;
import java.util.SortedSet;

@ArtifactProviderFor(GriffonModel.class)
public class PlayerModel extends AbstractGriffonModel {
    private Boolean isPlaying = false;
    private Video video;

    private Double rate;
    private Double volume;
    private Double size;
    private Double opacity;
    private Double duration;

    public PlayerModel(){
        rate = ControlsDefault.rate;
        volume = ControlsDefault.volume;
        size = ControlsDefault.size;
        duration = ControlsDefault.duration;
        opacity = ControlsDefault.opacity;
    }

    public Double getDuration() {
        return duration;
    }
    public void setDuration(Double duration) {
        this.duration = duration;
    }
    public Double getRate() {
        return rate;
    }
    public void setRate(Double rate) {
        this.rate = rate;
    }
    public Double getVolume() {
        return volume;
    }
    public void setVolume(Double volume) {
        this.volume = volume;
    }
    public double getSize() {
        return size;
    }
    public void setSize(Double size) {
        this.size = size;
    }
    public Double getOpacity() {
        return opacity;
    }
    public void setOpacity(Double opacity) {
        this.opacity = opacity;
    }
    public void setVideo(Video video) {
        this.video = video;
    }
    public void addPoint(Point point) {
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

    public SortedSet<Point> displayPoints(Duration currentTime){
        Point start = new Point();
        start.setStart(currentTime.subtract(Duration.millis( duration * 1000)));
        Point end = new Point();
        end.setStart(currentTime);

        return video.getPointSet().subSet(start, end);
    }

    private Icon generateIcon(Point point){
        return new Icon(point.getCategory(), 100);
    }
}