package org.laeq.editor;

import griffon.core.artifact.GriffonModel;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonModel;
import org.laeq.model.Category;
import org.laeq.model.CategoryCount;
import org.laeq.model.Point;
import org.laeq.model.Video;

import javax.annotation.Nonnull;



@ArtifactProviderFor(GriffonModel.class)
public class PlayerModel extends AbstractGriffonModel {
    @MVCMember @Nonnull private Video video;
    public ObservableSet<Point> points = FXCollections.observableSet();
    public ObservableList<CategoryCount> summary = FXCollections.observableArrayList();

    public void setVideo(@Nonnull Video video){
        this.video = video;
        points.addAll(video.getPoints());
       summary.addAll(video.getCategoryCount());
    }
}