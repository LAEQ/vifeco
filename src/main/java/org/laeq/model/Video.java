package org.laeq.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import javafx.util.Duration;
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
    @Id @GeneratedValue(generator = "increment")
    private Integer id;

    @Transient
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "uuid", updatable = false, nullable = false)
    private UUID uuid;

    @Column(nullable = false)
    private String path;

    @Column(nullable = false)
    private Duration duration;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "collection_id", nullable = false)
    private Collection collection;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(cascade = CascadeType.DETACH, mappedBy = "video", orphanRemoval = true, fetch = FetchType.EAGER)
    @SortNatural
    private SortedSet<Point> points = new TreeSet<>();

    @Transient
    private Map<Category, SortedSet<Point>> mapPoints = new HashMap<>();


    public Video() {
    }

    public Video(String path, Duration duration, Collection collection, User user) {
        this.path = path;
        this.duration = duration;
        this.setCollection(collection);
        this.user = user;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }

    public Duration getDuration() {
        return duration;
    }
    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public Collection getCollection() {
        return collection;
    }
    public void setCollection(Collection collection) {
        this.collection = collection;

        this.collection.getCategories().stream().forEach(c -> {
            mapPoints.put(c, new TreeSet<>());
        });
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

    public Map<Category, SortedSet<Point>> getMapPoints() {
        points.stream().forEach(p -> {
            mapPoints.get(p.getCategory()).add(p);
        });

        return mapPoints;
    }

    public void addPoint(Point p){
        p.setVideo(this);
        this.points.add(p);
        mapPoints.get(p.getCategory()).add(p);
    }

    public void removePoint(Point p){
        this.points.remove(p);
        mapPoints.get(p.getCategory()).remove(p);
    }

    public Map<Category, Integer> getTotalGrouped(){
        Map<Category, Integer> result = new HashMap<>();

        mapPoints.forEach((a, b)-> result.put(a, b.size()));

        return result;
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
