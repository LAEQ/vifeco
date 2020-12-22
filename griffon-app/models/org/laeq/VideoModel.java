package org.laeq;

import griffon.core.artifact.GriffonModel;
import griffon.metadata.ArtifactProviderFor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonModel;
import org.laeq.model.*;
import org.laeq.model.Collection;

import java.util.*;

@ArtifactProviderFor(GriffonModel.class)
public class VideoModel extends AbstractGriffonModel {
    public ObservableList<Video> videoList = FXCollections.observableArrayList();

    public Set<User> userSet = new HashSet<>();
    public Set<Collection> collectionSet = new HashSet<>();
    public Video selectedVideo;
    private Preferences prefs;

    public List<Category> categorySet = new ArrayList<>();

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
    }

    public void update(Video video) {
//        this.selectedVideo.setUser(video.getUser());
//        this.selectedVideo.setCollection(video.getCollection());
    }

    public Map<Category, Long> getTotalByCategory(){
//        if(this.selectedVideo != null){
//            return this.selectedVideo.getTotalByCategory();
//        } else {
            return new HashMap<>();
//        }
    }

    public void setPrefs(Preferences preferences) {
        this.prefs = preferences;
    }

    public Preferences getPrefs() {
        return prefs;
    }
}