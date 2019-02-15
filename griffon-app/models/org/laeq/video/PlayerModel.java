package org.laeq.video;

import griffon.core.artifact.GriffonModel;
import griffon.metadata.ArtifactProviderFor;
import javafx.beans.property.*;
import javafx.util.Duration;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonModel;
import org.laeq.model.*;

import javax.annotation.Nonnull;
import java.util.Iterator;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;

@ArtifactProviderFor(GriffonModel.class)
public class PlayerModel extends AbstractGriffonModel {
    private BooleanProperty isPlaying;
    private StringProperty videoPath;
    private VideoUser videoUser;
    private SortedSet<VideoPoint> videoPoints;

    private SimpleDoubleProperty rate;
    private SimpleDoubleProperty volume;
    private SimpleIntegerProperty size;
    private SimpleIntegerProperty duration;
    private SimpleDoubleProperty opacity;

    public PlayerModel(){
        this.videoPath = new SimpleStringProperty(this, "videoPath", "(Ctrl+0) to open a video.");
        this.isPlaying = new SimpleBooleanProperty(this, "isPlaying", false);
        this.videoPoints = new TreeSet<>();

        this.rate = new SimpleDoubleProperty(1.0);
        this.volume = new SimpleDoubleProperty(1.0);
        this.size = new SimpleIntegerProperty(100);
        this.duration = new SimpleIntegerProperty(10);
        this.opacity = new SimpleDoubleProperty(0.5);
    }


    public void setVideoUser(VideoUser videoUser) {
        this.videoUser = videoUser;
        this.setVideoPath(this.videoUser.getVideo().getPath());
        this.setIsPlaying(false);

        videoPoints.clear();

        videoUser.getPoints().forEach(point -> {
            videoPoints.add(new VideoPoint(this.size.getValue(), generateIcon(point), point));
        });
    }

    public void addPoint(Point point) {
        videoPoints.add(new VideoPoint(getSize(), generateIcon(point), point));
    }

    public Optional<Category> getCategory(String shortcut) {
        return videoUser.getVideo().getCategoryCollection().getCategorySet().stream().filter(category -> category.getShortcut().equals(shortcut)).findFirst();
    }

    public Video getVideo() {
        return videoUser.getVideo();
    }
    public User getUser() {
        return videoUser.getUser();
    }

    public void removePoint(Point point) {
        videoPoints.removeIf(videoPoint -> videoPoint.getPoint().equals(point));
    }

    public void addVideoPoint(VideoPoint newPoint) {
        videoPoints.add(newPoint);
    }

    @Nonnull
    public StringProperty videoPathProperty() {
        return videoPath;
    }
    public void setVideoPath(String videoPath) { this.videoPath.set(videoPath); }

    @Nonnull
    public BooleanProperty isPlayingProperty() { return isPlaying;}
    public boolean isIsPlaying() { return isPlaying.get();}
    public void setIsPlaying(boolean isPlaying) { this.isPlaying.set(isPlaying);}

    public double getRate() {
        return rate.get();
    }
    public SimpleDoubleProperty rateProperty() {
        return rate;
    }
    public void setRate(double rate) {
        this.rate.set(rate);
    }

    public double getVolume() {
        return volume.get();
    }
    public SimpleDoubleProperty volumeProperty() {
        return volume;
    }
    public void setVolume(double volume) {
        this.volume.set(volume);
    }

    public int getSize() {
        return size.get();
    }
    public SimpleIntegerProperty sizeProperty() {
        return size;
    }
    public void setSize(int size) {
        this.size.set(size);
    }

    public int getDuration() {
        return duration.get();
    }
    public SimpleIntegerProperty durationProperty() {
        return duration;
    }
    public void setDuration(int duration) {
        this.duration.set(duration);
    }

    public double getOpacity() {
        return opacity.get();
    }
    public SimpleDoubleProperty opacityProperty() {
        return opacity;
    }
    public void setOpacity(double opacity) {
        this.opacity.set(opacity);
    }

    private Icon generateIcon(Point point){
        Icon icon = null;
        try {
            String path = getApplication().getResourceHandler().getResourceAsURL(point.getCategory().getIcon()).getPath();
            icon = new Icon(size.getValue(), opacity.getValue(), path);
            icon.setX(point.getX());
            icon.setY(point.getY());
        } catch (Exception e) {
            //@todo for spock test only (category should not be null)
            if(point.getCategory() != null){
                getLog().error(String.format("Icon path not found: %s", point.getCategory().getIcon()));
                icon = new Icon(size.getValue(), opacity.getValue());
                icon.setX(point.getX());
                icon.setY(point.getY());
            }
        }

        return icon;
    }

    public Iterator<VideoPoint> displayIter(Duration currentTime) {
        return display(currentTime).iterator();
    }

    public SortedSet<VideoPoint> display(Duration currentTime){
        VideoPoint start = new VideoPoint(this.size.getValue(), null, new Point(0, 0, currentTime.subtract(Duration.seconds(this.duration.getValue())), null, null, null));
        VideoPoint end = new VideoPoint(this.size.getValue(), null, new Point(0, 0, currentTime, null, null, null));
        return videoPoints.subSet(start, end);
    }

    public SortedSet<VideoPoint> getVideoPoints() {
        return videoPoints;
    }
}