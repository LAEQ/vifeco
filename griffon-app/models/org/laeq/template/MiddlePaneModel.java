package org.laeq.template;

import griffon.core.artifact.GriffonModel;
import griffon.metadata.ArtifactProviderFor;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonModel;
import org.laeq.model.Preferences;
import org.laeq.model.Point;

@ArtifactProviderFor(GriffonModel.class)
public class MiddlePaneModel extends AbstractGriffonModel {
    private Point video;
    private Preferences preferences;

    public Point getVideo() {
        return video;
    }

    public void setVideo(Point video) {
        this.video = video;
    }

    public void setPrefs(Preferences preferences) {
        this.preferences = preferences;
    }

    public Preferences getPrefs() {
        return preferences;
    }
}