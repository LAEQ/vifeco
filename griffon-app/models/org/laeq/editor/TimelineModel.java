package org.laeq.editor;

import griffon.core.artifact.GriffonModel;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonModel;
import org.laeq.model.Point;
import org.laeq.model.Video;
import javax.annotation.Nonnull;

@ArtifactProviderFor(GriffonModel.class)
final public class TimelineModel extends AbstractGriffonModel {
    @MVCMember @Nonnull private Video video;

    //List for summary table
    final public ObservableList<Point> points = FXCollections.observableArrayList();

    public void setVideo(@Nonnull Video video){
        this.video = video;

        points.addAll(video.getPoints());
    }
}