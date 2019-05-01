package org.laeq.video;

import griffon.core.artifact.GriffonModel;
import griffon.metadata.ArtifactProviderFor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonModel;
import org.laeq.model.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@ArtifactProviderFor(GriffonModel.class)
public class ContainerModel extends AbstractGriffonModel {
    private ObservableList<Video> videoList = FXCollections.observableArrayList();
    private FilteredList<Video> filteredList = new FilteredList<>(videoList, p -> true);

    private Set<User> userSet = new HashSet<>();
    private Set<Collection> collectionSet = new HashSet<>();
    private Video selectedVideo;
    private String errors = "";
    private Preferences prefs;

    public FilteredList<Video> getFilteredList() {
        return filteredList;
    }

    private Set<Category> categorySet = new HashSet<>();

    public Video getSelectedVideo() {
        return selectedVideo;
    }

    public ObservableList<Video> getVideoList() {
        return videoList;
    }
    public Set<Collection> getCollectionSet() {
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
        this.selectedVideo.setUser(video.getUser());
        this.selectedVideo.setCollection(video.getCollection());
    }

    public void addCategories(Set<Category> categories) {
        this.categorySet.addAll(categories);
    }

    public Set<Category> getCategorySet() {
        return categorySet;
    }

    public Map<Category, Long> getTotalByCategory(){
        if(this.selectedVideo != null){
            return this.selectedVideo.getTotalByCategory();
        } else {
            return new HashMap<>();
        }
    }

    public void deleteVideo() {
        videoList.remove(this.selectedVideo);
        this.selectedVideo = null;
    }

    public void setPrefs(Preferences preferences) {
        this.prefs = preferences;
    }

    public Preferences getPrefs() {
        return prefs;
    }
}