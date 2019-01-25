package org.laeq.video;

import griffon.core.artifact.GriffonModel;
import griffon.metadata.ArtifactProviderFor;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonModel;

import javax.annotation.Nonnull;
import java.time.Duration;

@ArtifactProviderFor(GriffonModel.class)
public class PlayerModel extends AbstractGriffonModel {
    private BooleanProperty isPlaying;
    private StringProperty videoPath;
    private Duration duration;

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
}