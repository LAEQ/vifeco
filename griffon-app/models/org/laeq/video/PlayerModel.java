package org.laeq.video;

import griffon.core.artifact.GriffonModel;
import griffon.metadata.ArtifactProviderFor;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonModel;
import org.laeq.model.Point;
import org.laeq.model.VideoUser;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.util.SortedSet;

@ArtifactProviderFor(GriffonModel.class)
public class PlayerModel extends AbstractGriffonModel {
    private BooleanProperty isPlaying;
    private StringProperty videoPath;
    private Duration duration;
    private VideoUser item;

    public PlayerModel(){
        this.videoPath = new SimpleStringProperty(this, "videoPath", "(Ctrl+0) to open a video.");
        this.isPlaying = new SimpleBooleanProperty(this, "isPlaying", false);
    }

    @Nonnull
    public StringProperty videoPathProperty() {
        return videoPath;
    }
    public String getVideoPath() { return videoPath.get(); }
    public void setVideoPath(String videoPath) { this.videoPath.set(videoPath); }

    @Nonnull
    public BooleanProperty isPlayingProperty() { return isPlaying;}
    public boolean isIsPlaying() { return isPlaying.get();}
    public void setIsPlaying(boolean isPlaying) { this.isPlaying.set(isPlaying);}

    public void setItem(VideoUser videoUser) {
        item = videoUser;
        this.setVideoPath(item.getVideo().getPath());
        this.setIsPlaying(false);
    }

    public void setItems(SortedSet<Point> listPoint) {

    }
}