package org.laeq.video.player;

import griffon.core.artifact.GriffonModel;
import griffon.metadata.ArtifactProviderFor;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonModel;

@ArtifactProviderFor(GriffonModel.class)
public class TimelineModel extends AbstractGriffonModel {
    private double lineDuration;

    public double getLineDuration() {
        return lineDuration;
    }

    public void setLineDuration(double lineDuration) {
        this.lineDuration = lineDuration;
    }


}