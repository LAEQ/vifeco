package org.laeq.video;

import griffon.core.artifact.GriffonModel;
import griffon.metadata.ArtifactProviderFor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonModel;
import org.laeq.model.Category;
import org.laeq.model.CategoryCollection;
import org.laeq.model.User;
import org.laeq.model.Video;

import java.util.HashSet;
import java.util.Set;

@ArtifactProviderFor(GriffonModel.class)
public class ContainerModel extends AbstractGriffonModel {
    private ObservableList<Video> videoList = FXCollections.observableArrayList();
    private Set<User> userSet = new HashSet<>();
    private Set<CategoryCollection> collectionSet = new HashSet<>();
    private Video selectedVideo;
    private String errors = "";

    private Set<Category> categorySet = new HashSet<>();

    public Video getSelectedVideo() {
        return selectedVideo;
    }

    public ObservableList<Video> getVideoList() {
        return videoList;
    }

    public void setVideoList(ObservableList<Video> videoList) {
        this.videoList = videoList;
    }

    public Set<CategoryCollection> getCollectionSet() {
        return collectionSet;
    }

    public Set<User> getUserSet() {
        return userSet;
    }

    public void setSelectedVideo(Video selectedItem) {
        this.selectedVideo = selectedItem;
    }

    public void clear() {
        this.selectedVideo = null;
        this.errors = "";
    }

    public void update(Video video) {
        System.out.println(video);
        this.selectedVideo.setUser(video.getUser());
        this.selectedVideo.setCategoryCollection(video.getCategoryCollection());
    }

    public void addCategories(Set<Category> categories) {
        this.categorySet.addAll(categories);
    }

    public Set<Category> getCategorySet() {
        return categorySet;
    }
}