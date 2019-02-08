package org.laeq.model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.util.Duration;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.Temporal;
import java.util.Calendar;
import java.util.Objects;

public class Video extends Entity{
    private Integer id;
    private SimpleStringProperty path;
    private SimpleStringProperty name;
    private SimpleDoubleProperty duration;
    private Timestamp createdAt = new Timestamp(System.currentTimeMillis());
    private Timestamp updatedAt = new Timestamp(System.currentTimeMillis());
    private CategoryCollection categoryCollection;


    public Video(Integer id, String path, Duration duration, CategoryCollection categoryCollection) {
        this.id = id;
        this.path = new SimpleStringProperty(this, "path", path);
        this.name = new SimpleStringProperty(this, "name", pathToName(path));
        this.duration = new SimpleDoubleProperty(this, "duration", duration.toMillis());
        this.categoryCollection = categoryCollection;
    }

    public Video(String path, Duration duration, CategoryCollection categoryCollection) {
        this.path = new SimpleStringProperty(this, "test", path);
        this.name = new SimpleStringProperty(this, "name", pathToName(path));
        this.duration = new SimpleDoubleProperty(this, "duration", duration.toMillis());
        this.categoryCollection = categoryCollection;
    }

    public Video() {
        this.path = new SimpleStringProperty(this, "test", "");
        this.name = new SimpleStringProperty(this, "name", "");
        this.duration = new SimpleDoubleProperty(this, "duration", 0.0);
    }

    public String getPath() {
        return path.get();
    }
    public SimpleStringProperty pathProperty() {
        return path;
    }
    public void setPath(String path) {
        this.path.set(path);
        this.name.set(pathToName(path));
    }
    public String getName() {
        return name.get();
    }
    public SimpleStringProperty nameProperty() {
        return name;
    }
    public void setName(String name) {
        this.name.set(name);
    }
    public double getDuration() {
        return duration.get();
    }
    public SimpleDoubleProperty durationProperty() {
        return duration;
    }
    public void setDuration(double duration) {
        this.duration.set(duration);
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public CategoryCollection getCategoryCollection() {
        return categoryCollection;
    }
    public void setCategoryCollection(CategoryCollection categoryCollection) {
        this.categoryCollection = categoryCollection;
    }

    @Override
    public int getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Video video = (Video) o;
        return path.getValue().equals(video.path.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(path.getValue());
    }

    private String pathToName(String path){
        return Paths.get(path).getFileName().toString();
    }
}
