package org.laeq;

import griffon.core.artifact.GriffonModel;
import griffon.metadata.ArtifactProviderFor;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonModel;
import org.laeq.model.Collection;
import org.laeq.model.*;

import java.util.*;

@ArtifactProviderFor(GriffonModel.class)
public class VideoModel extends AbstractGriffonModel {
    public String currentVideo;
    public ObservableList<Video> videoList = FXCollections.observableArrayList();
    public ObservableList<CategoryCount> categoryCounts = FXCollections.observableArrayList();


    // Form Section
    public Video selectedVideo;
    public SimpleStringProperty name = new SimpleStringProperty(this, "name", "");
    public SimpleStringProperty duration = new SimpleStringProperty(this, "duration", "");
    public SimpleStringProperty total = new SimpleStringProperty(this, "total", "");

    public Set<User> userSet = new HashSet<>();
    public Set<Collection> collectionSet = new HashSet<>();
    public List<Category> categorySet = new ArrayList<>();
    public Set<Collection> getCollectionSet() {
        return collectionSet;
    }
    public Set<User> getUserSet() {
        return userSet;
    }

    public void setSelectedVideo(Video video) {
        this.selectedVideo = video;
        name.set(video.pathToName());
        duration.set(video.getDurationFormatted());
        total.set(String.format("%d", video.getPoints().size()));
        categoryCounts.clear();
        categoryCounts.addAll(this.selectedVideo.getCategoryCount());
    }

    public void clear() {
        this.selectedVideo = null;
        name.set("");
        duration.set("");
        total.set("");
        categoryCounts.clear();
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

    public void removeVideo() {
        videoList.remove(selectedVideo);
        selectedVideo = null;
    }

    public void reset() {
    }

    public void removePoint(Point point) {
        Optional<CategoryCount> first = categoryCounts.stream().filter(categoryCount -> categoryCount.category.equals(point.getCategory())).findFirst();

        if(first.isPresent()){
            first.get().decrement();
        }
    }

    public void addPoint(Point point) {
        Optional<CategoryCount> first = categoryCounts.stream().filter(categoryCount -> categoryCount.category.equals(point.getCategory())).findFirst();

        if(first.isPresent()){
            first.get().increment();
        }
    }
}