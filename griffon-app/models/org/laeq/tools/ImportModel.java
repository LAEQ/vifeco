package org.laeq.tools;

import griffon.core.artifact.GriffonModel;
import griffon.metadata.ArtifactProviderFor;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonModel;
import org.laeq.model.Category;
import org.laeq.model.Collection;
import org.laeq.model.User;
import org.laeq.model.Video;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@ArtifactProviderFor(GriffonModel.class)
public class ImportModel extends AbstractGriffonModel {
    public SimpleStringProperty filename = new SimpleStringProperty("");
    public SimpleStringProperty filePath = new SimpleStringProperty("");
    public SimpleStringProperty report = new SimpleStringProperty("");
    public SimpleBooleanProperty valid = new SimpleBooleanProperty(Boolean.TRUE);
    public SimpleStringProperty warning = new SimpleStringProperty("");
    public ObservableList<String> styles = FXCollections.observableArrayList();

    public Video video;

    public ObservableList<Collection> collections = FXCollections.observableArrayList();
    public ObservableList<User> users = FXCollections.observableArrayList();

    public void setVideo(Video video) {
        this.video = video;
        this.video.setId(null);
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("Collection: id = %d - %s\n", video.getCollection().getId(), video.getCollection().toString()));
        builder.append(translate("z.categories"));
        builder.append("\n");
        video.getCollection().getCategories().stream().sorted(Comparator.comparingInt(Category::getId)).forEach(c -> {
            builder.append(String.format("%d - %s\n", c.getId(), c.getName()));
        });
        builder.append("\n");

        report.set(builder.toString());
        importIsValid();
    }

    private void importIsValid(){
        styles.clear();
        warning.set("");
        valid.set(Boolean.TRUE);

        try{
            Optional<User> user = users.stream().filter(u -> u.equals(video.getUser())).findFirst();
            video.setUser(user.get());
            Optional<Collection> collection = collections.stream().filter(collection1 -> collection1.equals(video.getCollection())).findFirst();
            video.setCollection(collection.get());
            video.getPoints().parallelStream().forEach(point -> {
                point.setVideo(video);
                point.setId(null);
                Optional<Category> first = video.getCollection().getCategories().stream().filter(category -> category.getId() == point.getCategory().getId()).findFirst();

                point.setCategory(first.get());
            });

            styles.addAll(Arrays.asList("alert", "alert-success"));
            warning.set(translate("import.video.valid"));
        } catch (Exception e){
            valid.set(Boolean.FALSE);
            video = null;
            warning.set(translate("import.video.invalid"));
            styles.addAll(Arrays.asList("alert", "alert-danger"));
        }
    }


    private String translate(String key){
        return getApplication().getMessageSource().getMessage(key);
    }

    public void clear() {
        this.video = null;
        this.styles.clear();
        this.warning.set("");
    }
}