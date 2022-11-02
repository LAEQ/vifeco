package org.laeq.editor;

import griffon.core.artifact.GriffonModel;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonModel;
import org.laeq.model.Video;
import javax.annotation.Nonnull;

@ArtifactProviderFor(GriffonModel.class)
public final class PlayerModel extends AbstractGriffonModel {
    @MVCMember @Nonnull private Video video;

    public void setVideo(@Nonnull Video video){
        this.video = video;
    }
}