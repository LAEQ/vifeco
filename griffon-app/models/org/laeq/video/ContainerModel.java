package org.laeq.video;

import griffon.core.artifact.GriffonModel;
import griffon.metadata.ArtifactProviderFor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonModel;
import org.laeq.model.Video;

@ArtifactProviderFor(GriffonModel.class)
public class ContainerModel extends AbstractGriffonModel {
    private ObservableList<Video> videoList = FXCollections.observableArrayList();

    public ObservableList<Video> getVideoList() {
        return videoList;
    }

    public void setVideoList(ObservableList<Video> videoList) {
        this.videoList = videoList;
    }
}