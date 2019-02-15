package org.laeq.video;

import griffon.core.artifact.GriffonModel;
import griffon.metadata.ArtifactProviderFor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonModel;
import org.laeq.model.VideoUser;

import java.util.List;

@ArtifactProviderFor(GriffonModel.class)
public class VideoListModel extends AbstractGriffonModel {
    private ObservableList<VideoUser> videoList = FXCollections.observableArrayList();

    public void addVideos(List<VideoUser> list){
        videoList.addAll(list);
    }

    public void addVideo(VideoUser video){
        videoList.add(video);
    }

    public ObservableList<VideoUser> getVideoList() {
        return videoList;
    }
}