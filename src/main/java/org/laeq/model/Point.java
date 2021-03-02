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
import org.hibernate.annotations.GenericGenerator;
import org.laeq.model.converter.hibernate.DurationConverter;
import org.laeq.model.converter.jackson.CategoryConverterDeserialize;
import org.laeq.model.converter.jackson.CategoryConverterSerialize;
import org.laeq.model.converter.jackson.DurationToMilliConverter;
import org.laeq.model.converter.jackson.MilliToDuration;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "point")
@JsonIgnoreProperties({"video"})
@JsonPropertyOrder({"id", "x", "y", "start", "category"})
public class Point implements Comparable<Point> {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false)
    private Double x;
    @Column(nullable = false)
    private Double y;

    @Column(nullable = false)
    @Convert(converter = DurationConverter.class)
    @JsonSerialize(converter = DurationToMilliConverter.class)
    @JsonDeserialize(converter = MilliToDuration.class)
    private Duration start;

    @ManyToOne(cascade = {CascadeType.DETACH})
    @JoinColumn(name = "category_id", referencedColumnName = "id", nullable = false)
    @JsonSerialize(converter = CategoryConverterSerialize.class)
    @JsonDeserialize(converter = CategoryConverterDeserialize.class)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id", nullable = false)
    private Video video;

    public Point() {}

    public Point(UUID id) {
        this.id = id;
    }

    public Point(UUID id, Duration start) {
        this.id = id;
        this.start = start;
    }

    public Point(UUID id, Double x, Double y, Duration start, Category category, Video video) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.start = start;
        this.category = category;
        this.video = video;
    }

    public Point(Double x, Double y, Duration start, Category category, Video video) {
        this.x = x;
        this.y = y;
        this.start = start;
        this.category = category;
        this.video = video;
    }

    /**
     * Constructor for testing (video entity is added in addPoint method
     * @param x
     * @param y
     * @param start
     * @param category
     */
    public Point(Double x, Double y, Duration start, Category category) {
        this.x = x;
        this.y = y;
        this.start = start;
        this.category = category;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public Duration getStart() {
        return start;
    }

    public void setStart(Duration start) {
        this.start = start;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Video getVideo() {
        return video;
    }

    public void setVideo(Video video) {
        this.video = video;
    }

    public String getFloorStart(){
        return String.format("%d", (int)Math.floor(this.start.toMinutes()));
    }

    @JsonIgnore
    public String getStartFormatted(){
        return DurationFormatUtils.formatDuration((long)start.toMillis(), "H:m:s");
    }

    @JsonIgnore
    public String getStartFormatted2(){
        return String.format("%s - %s", video, DurationFormatUtils.formatDuration((long)start.toMillis(), "H:m:s"));
    }

    @Override
    public int compareTo(Point o) {
        if(this.equals(o)){
            return 0;
        }

        return this.start.compareTo(o.start);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Point point = (Point) o;

        return new EqualsBuilder().append(id, point.id).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).toHashCode();
    }

    @Override
    public String toString() {
        return String.format("%s @ %s", category.getName(), getStartFormatted());
    }
}

