package org.laeq.editor;

import griffon.core.artifact.GriffonModel;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonModel;
import org.laeq.model.Point;
import org.laeq.model.Video;
import javax.annotation.Nonnull;

@ArtifactProviderFor(GriffonModel.class)
final public class IconPaneModel extends AbstractGriffonModel {
    @MVCMember @Nonnull private Video video;

    public void setVideo(@Nonnull Video video){
        this.video = video;

    }

    public void addPoint(Point point) {

    }

    public void removePoint(Point point) {

    }
}