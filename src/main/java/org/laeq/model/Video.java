package org.laeq.model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.util.Duration;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class Video implements Entity{
    private Integer id;
    private SimpleStringProperty path;
    private SimpleStringProperty name;
    private SimpleDoubleProperty duration;

    public Video(Integer id, String path, Duration duration) {

        this.id = id;
        this.path = new SimpleStringProperty(this, "path", path);
        this.name = new SimpleStringProperty(Paths.get(path).getFileName().toString());
        this.duration = new SimpleDoubleProperty(this, "duration", duration.toMillis());
    }

    public Video(String path, Duration duration) {
        this.path = new SimpleStringProperty(this, "test", path);
        this.name = new SimpleStringProperty(this, "name", Paths.get(path).getFileName().toString());
        this.duration = new SimpleDoubleProperty(this, "duration", duration.toMillis());
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
}
