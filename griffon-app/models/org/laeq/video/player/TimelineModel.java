package org.laeq.video.player;

import griffon.core.artifact.GriffonModel;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.collections.SetChangeListener;
import javafx.util.Duration;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonModel;
import org.laeq.model.Point;
import org.laeq.model.Video;

import javax.annotation.Nonnull;

@ArtifactProviderFor(GriffonModel.class)
public class TimelineModel extends AbstractGriffonModel {
    @MVCMember @Nonnull private Video video;
    @MVCMember @Nonnull private TimelineView view;

    private Duration duration;
    private double lineDuration;

    private SetChangeListener<Point> listener;

    public Duration getDuration() {
        return duration;
    }

    public void init(Duration duration) {
        video.setDuration(duration.toMillis());
        this.duration = duration;
    }

    public double getLineDuration() {
        return lineDuration;
    }

    public void setLineDuration(double lineDuration) {
        this.lineDuration = lineDuration;
    }


}