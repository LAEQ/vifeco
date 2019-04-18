package org.laeq.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.util.Duration;
import org.apache.commons.lang3.time.DurationFormatUtils;

import java.io.File;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.Map;
import java.util.Objects;
import java.util.SortedSet;
import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;

@JsonIgnoreProperties({ "id", "name", "total", "createdAt", "updatedAt"})
@JsonPropertyOrder({"uuid", "path", "user", "duration", "collection", "pointSet"})
public class Video extends BaseEntity {
    private Integer id;
    private SimpleStringProperty path;
    private SimpleStringProperty name;
    private SimpleDoubleProperty duration;
    private Timestamp createdAt = new Timestamp(System.currentTimeMillis());
    private Timestamp updatedAt = new Timestamp(System.currentTimeMillis());
    private Collection collection;
    private User user;
    private SimpleLongProperty total;
    private SortedSet<Point> pointSet = new ConcurrentSkipListSet<>();
    private UUID uuid = UUID.randomUUID();

    public Video(Integer id, String path, Duration duration, User user, Collection collection) {
        this.id = id;
        this.path = new SimpleStringProperty(this, "path", path);
        this.name = new SimpleStringProperty(this, "name", pathToName(path));
        this.duration = new SimpleDoubleProperty(this, "duration", duration.toMillis());
        this.total = new SimpleLongProperty(this, "total", 0);
        this.user = user;
        this.collection = collection;
    }

    public Video(String path, Duration duration, User user, Collection collection) {
        this.path = new SimpleStringProperty(this, "test", path);
        this.name = new SimpleStringProperty(this, "name", pathToName(path));
        this.duration = new SimpleDoubleProperty(this, "duration", duration.toMillis());
        this.total = new SimpleLongProperty(this, "total", 0);
        this.user = user;
        this.collection = collection;
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

    public Collection getCollection() {
        return collection;
    }
    public void setCollection(Collection collection) {
        this.collection = collection;
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

    @JsonIgnore
    public String getDurationFormatted(){
        return DurationFormatUtils.formatDuration(duration.getValue().longValue(), "H:mm:ss", true);
    }

    @Override
    public String toString() {
        return "Video{" + id + '}';
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
        return uuid.compareTo(video.uuid) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid.toString());
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

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }

    @JsonIgnore
    public Map<Category, Long> getTotalByCategory() {
        return pointSet.stream().collect(Collectors.groupingBy(Point::getCategory, Collectors.counting()));
    }

    @JsonIgnore
    public boolean isEditable(){
        return new File(path.getValue()).exists();
    }
}
