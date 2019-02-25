package org.laeq.video;

import griffon.core.artifact.GriffonModel;
import griffon.core.i18n.MessageSource;
import griffon.metadata.ArtifactProviderFor;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonModel;
import org.laeq.model.CategoryCollection;
import org.laeq.model.User;
import org.laeq.model.Video;

import java.util.Locale;

@ArtifactProviderFor(GriffonModel.class)
public class ContainerModel extends AbstractGriffonModel {
    private ObservableList<Video> videoList = FXCollections.observableArrayList();
    private ObservableList<User> userSet = FXCollections.observableArrayList();
    private ObservableList<CategoryCollection> collectionSet = FXCollections.observableArrayList();
    private Video selectedVideo;
    private String errors = "";

    private SimpleIntegerProperty userId = new SimpleIntegerProperty(this, "userId", 0);
    private SimpleIntegerProperty categoryCollectionId = new SimpleIntegerProperty(this, "categoryCollectionId", 0);

    public int getUserId() {
        return userId.get();
    }
    public SimpleIntegerProperty userIdProperty() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId.set(userId);
    }

    public int getCategoryCollectionId() {
        return categoryCollectionId.get();
    }
    public SimpleIntegerProperty categoryCollectionIdProperty() {
        return categoryCollectionId;
    }
    public void setCategoryCollectionId(int categoryCollectionId) {
        this.categoryCollectionId.set(categoryCollectionId);
    }

    public Video getSelectedVideo() {
        return selectedVideo;
    }

    public ObservableList<Video> getVideoList() {
        return videoList;
    }

    public void setVideoList(ObservableList<Video> videoList) {
        this.videoList = videoList;
    }

    public ObservableList<CategoryCollection> getCollectionSet() {
        return collectionSet;
    }

    public ObservableList<User> getUserSet() {
        return userSet;
    }

    public void setSelectedVideo(Video selectedItem) {
        this.selectedVideo = selectedItem;
    }

    public boolean valid() {
        boolean result = true;

        StringBuilder builder = new StringBuilder();

        MessageSource messageSource = getApplication().getMessageSource();

        if(this.selectedVideo == null){
            builder.append(messageSource.getMessage("org.leaq.video.validation.no_video_selected", Locale.CANADA));
            result = false;
        }

        if(getUserId() == 0){
            builder.append(messageSource.getMessage("org.leaq.video.validation.no_user_selected", Locale.CANADA));
            result = false;
        }

        if(getUserId() == 0){
            builder.append(messageSource.getMessage("org.leaq.video.validation.no_colection_selected", Locale.CANADA));
            result = false;
        }

        errors = builder.toString();

        return result;
    }

    public String getErrors() {
        return errors;
    }

    public void clear() {
        this.selectedVideo = null;
        this.errors = "";
    }

    public Video generateVideo() {
        Video video = new Video();
        video.setId(this.selectedVideo.getId());

        User user = userSet.stream().filter(user1 -> user1.getId() == getUserId()).findAny().get();
        CategoryCollection categoryCollection = collectionSet.stream().filter(cc -> cc.getId() == getCategoryCollectionId()).findAny().get();

        video.setUser(user);
        video.setCategoryCollection(categoryCollection);

        return video;
    }

    public void update(Video video) {
        System.out.println(video);
        this.selectedVideo.setUser(video.getUser());
        this.selectedVideo.setCategoryCollection(video.getCategoryCollection());
    }
}