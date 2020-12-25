package org.laeq.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import javafx.util.Duration;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SortNatural;
import org.laeq.model.converter.DurationConverter;

import javax.persistence.*;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
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
    @Convert(converter = DurationConverter.class)
    private Duration duration;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "collection_id", nullable = false)
    private Collection collection;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "video", orphanRemoval = true, fetch = FetchType.EAGER)
    @SortNatural
    private SortedSet<Point> points = new TreeSet<>();

    @CreationTimestamp
    private Date createdAt;

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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void addPoint(Point p){
        p.setVideo(this);
        this.points.add(p);
    }

    public void removePoint(Point p){
        this.points.remove(p);
    }

    public Map<Category, Integer> getTotalGrouped(){
        Map<Category, Integer> result = new HashMap<>();

        return result;
    }

    @JsonIgnore
    public String getDurationFormatted(){
        return DurationFormatUtils.formatDuration((long) duration.toMillis(), "H:mm:ss", true);
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

    public String getCreatedAtFormatted() {
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(createdAt);
    }
}
