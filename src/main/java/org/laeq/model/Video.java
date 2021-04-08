package org.laeq.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import javafx.util.Duration;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.laeq.model.converter.hibernate.DurationConverter;
import org.laeq.model.converter.jackson.DurationToMilliConverter;
import org.laeq.model.converter.jackson.MilliToDuration;
import org.laeq.model.converter.jackson.PathConverterSerialize;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


@JsonIgnoreProperties({"createdAt", "selected"})
@JsonPropertyOrder({"id", "path", "duration", "user", "collection", "points"})
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
    @JsonSerialize(converter = PathConverterSerialize.class)
    private String path;

    @Column(nullable = false)
    @Convert(converter = DurationConverter.class)
    @JsonSerialize(converter = DurationToMilliConverter.class)
    @JsonDeserialize(converter = MilliToDuration.class)
    private Duration duration;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "collection_id", nullable = false)
    private Collection collection;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "video", cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER)
    private List<Point> points = new ArrayList<>();

    @CreationTimestamp
    private Date createdAt;

    @Transient
    private Boolean selected = Boolean.FALSE;

    public Video() {
    }

    public Video(String path, Duration duration, Collection collection, User user) {
        this.path = path;
        this.duration = duration;
        this.setCollection(collection);
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

    public List<Point> getPoints() {
        return points;
    }
    public void setPoints(List<Point> points) {
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

    @JsonIgnore
    public List<CategoryCount> getCategoryCount(){
        Map<Category, CategoryCount> tmp = new HashMap<>();

        for (Category category : collection.getCategories()){
            tmp.put(category, new CategoryCount(category, 0));
        }

        for(Point point : points){
            tmp.get(point.getCategory()).increment();
        }

        return tmp.values().stream().collect(Collectors.toList());
    }

    @JsonIgnore
    public String getDurationFormatted(){
        return DurationFormatUtils.formatDuration((long) duration.toMillis(), "H:mm:ss", true);
    }

    @JsonIgnore
    public String pathToName(){
        return Paths.get(path).getFileName().toString();
    }

    @JsonIgnore
    public String getCreatedAtFormatted() {
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(createdAt);
    }

    @JsonIgnore
    public void updateCollection(Collection newValue) {
        this.setCollection(newValue);
        List<Point> filtered = points.stream().filter(p -> newValue.getCategories().contains(p.getCategory())).collect(Collectors.toList());
        points.clear();
        points.addAll(filtered);
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Video video = (Video) o;

        return new EqualsBuilder().append(id, video.id).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).toHashCode();
    }
}
