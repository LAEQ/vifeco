package org.laeq.video.player;

import griffon.core.artifact.GriffonModel;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.util.Duration;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonModel;
import org.laeq.model.Category;
import org.laeq.model.Point;
import org.laeq.model.User;
import org.laeq.model.Video;
import org.laeq.video.ControlsDefault;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.SortedSet;

@ArtifactProviderFor(GriffonModel.class)
public class PlayerModel extends AbstractGriffonModel {
    @MVCMember @Nonnull VideoEditor editor;
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
}