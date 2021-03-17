package org.laeq.editor;

import griffon.core.artifact.GriffonModel;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonModel;
import org.laeq.model.CategoryCount;
import org.laeq.model.Point;
import org.laeq.model.Video;

import javax.annotation.Nonnull;

@ArtifactProviderFor(GriffonModel.class)
final public class CategorySumModel extends AbstractGriffonModel {
    @MVCMember @Nonnull private Video video;

    final public ObservableList<CategoryCount> summary = FXCollections.observableArrayList();

    public void setVideo(@Nonnull Video video){
        this.video = video;
        summary.addAll(video.getCategoryCount());
    }

    public void addPoint(Point point) {
        summary.stream().filter(c -> c.category.equals(point.getCategory())).findFirst().get().increment();
    }

    public void removePoint(Point point) {
        summary.stream().filter(c -> c.category.equals(point.getCategory())).findFirst().get().decrement();
    }
}