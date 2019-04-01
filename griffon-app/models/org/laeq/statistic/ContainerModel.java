package org.laeq.statistic;

import griffon.core.artifact.GriffonModel;
import griffon.metadata.ArtifactProviderFor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonModel;
import org.laeq.model.Video;

import java.util.Collection;

@ArtifactProviderFor(GriffonModel.class)
public class ContainerModel extends AbstractGriffonModel {
   private ObservableList<Video> videos = FXCollections.observableArrayList();

    public ObservableList<Video> getVideos() {
        return videos;
    }

    public void addVideos(Collection<Video> videos) {
        this.videos.addAll(videos);
    }
}