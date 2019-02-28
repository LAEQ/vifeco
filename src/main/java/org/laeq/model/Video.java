package org.laeq.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.util.Duration;

import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.SortedSet;
import java.util.concurrent.ConcurrentSkipListSet;

@JsonIgnoreProperties({ "id", "name", "total", "createdAt", "updatedAt"})
@JsonPropertyOrder({"path", "user", "duration", "categoryCollection", "pointSet"})
public class Video extends BaseEntity {
    private Integer id;
    private SimpleStringProperty path;
    private SimpleStringProperty name;
    private SimpleDoubleProperty duration;
    private Timestamp createdAt = new Timestamp(System.currentTimeMillis());
    private Timestamp updatedAt = new Timestamp(System.currentTimeMillis());
    private CategoryCollection categoryCollection;
    private User user;
    private SimpleLongProperty total;
    private SortedSet<Point> pointSet = new ConcurrentSkipListSet<>();

    public Video(Integer id, String path, Duration duration, User user, CategoryCollection categoryCollection) {
        this.id = id;
        this.path = new SimpleStringProperty(this, "path", path);
        this.name = new SimpleStringProperty(this, "name", pathToName(path));
        this.duration = new SimpleDoubleProperty(this, "duration", duration.toMillis());
        this.total = new SimpleLongProperty(this, "total", 0);
        this.user = user;
        this.categoryCollection = categoryCollection;
    }

    public Video(String path, Duration duration, User user, CategoryCollection categoryCollection) {
        this.path = new SimpleStringProperty(this, "test", path);
        this.name = new SimpleStringProperty(this, "name", pathToName(path));
        this.duration = new SimpleDoubleProperty(this, "duration", duration.toMillis());
        this.total = new SimpleLongProperty(this, "total", 0);
        this.user = user;
        this.categoryCollection = categoryCollection;
    }

    public Video() {
        this.path = new SimpleStringProperty(this, "test", "");
        this.name = new SimpleStringProperty(this, "name", "");
        this.duration = new SimpleDoubleProperty(this, "duration", 0.0);
        this.total = new SimpleLongProperty(this, "total", 0);
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

    public long getTotal() {
        return total.get();
    }
    public SimpleLongProperty totalProperty() {
        return total;
    }
    public void setTotal(long total) {
        this.total.set(total);
    }

    @Override
    public String toString() {
        return "Video{" +
                "id=" + id +
                ", path=" + path.getValue() +
                ", name=" + name.getValue() +
                ", categoryCollection=" + categoryCollection +
                ", user=" + user +
                '}';
    }

    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public int getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public void addPoint(Point point){
        pointSet.add(point);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Video video = (Video) o;
        return id.equals(video.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    private String pathToName(String path){
        return Paths.get(path).getFileName().toString();
    }

    public SortedSet<Point> getPointSet() {
        return pointSet;
    }

    public long totalPoints() {
        return pointSet.size();
    }
}
