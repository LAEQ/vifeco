package org.laeq.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SortNatural;

import javax.persistence.*;
import java.nio.file.Paths;
import java.util.*;


@JsonIgnoreProperties({ "id", "name", "total", "createdAt", "updatedAt"})
@JsonPropertyOrder({"uuid", "path", "user", "duration", "collection", "pointSet"})
@Entity
@Table(name = "video")
public class Video {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String path;

    @Column(nullable = false)
    private Double duration;

    @ManyToOne()
    @JoinColumn(name = "collection_id", nullable = false)
    private Collection collection;

    @ManyToOne()
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(cascade = CascadeType.ALL)
    @SortNatural()
    private SortedSet<Point> points = new TreeSet<>();

    public Video() {

    }

    public Video(String path, Double duration, Collection collection, User user) {
        this();
        this.path = path;
        this.duration = duration;
        this.collection = collection;
        this.user = user;
    }

    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }

    public Double getDuration() {
        return duration;
    }
    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public Collection getCollection() {
        return collection;
    }
    public void setCollection(Collection collection) {
        this.collection = collection;
    }

    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

    public SortedSet<Point> getPoints() {
        return points;
    }

    public void setPoints(SortedSet<Point> points) {
        this.points = points;
    }

    public void addPoint(Point point){
        this.points.add(point);
    }

    @JsonIgnore
    public String getDurationFormatted(){
        return "to do";
//        return DurationFormatUtils.formatDuration(duration.getValue().longValue(), "H:mm:ss", true);
    }

    @JsonIgnore
    public String getAbsolutePath(){
        return "to do";
//        return String.format("%s%s%s", Settings.videoPath, File.separator, this.path.getValue());
    }

    private String pathToName(String path){
        return Paths.get(path).getFileName().toString();
    }


//    @JsonIgnore
//    public Map<Category, Long> getTotalByCategory() {
//        return pointSet.stream().collect(Collectors.groupingBy(Point::getCategory, Collectors.counting()));
//    }

    public String getName() {
        return "to do";
    }
}
