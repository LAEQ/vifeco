package org.laeq.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import org.laeq.model.serializer.PointDeserializer;
import javax.persistence.*;
import javafx.util.Duration;


@JsonIgnoreProperties({"video", "icon", "duration", "createdAt", "updatedAt", "category", "uuid"})
@JsonPropertyOrder({"id", "x", "y", "categoryId", "startDouble", "videoId"})
@JsonDeserialize(using = PointDeserializer.class)
@Entity
@Table(name = "point")
public class Point implements Comparable<Point> {
    @Id @GeneratedValue(generator = "increment")
    private Integer id;
    @Column(nullable = false)
    private double x;
    @Column(nullable = false)
    private double y;
    @Column(nullable = false)
    private Duration start;

    @ManyToOne(cascade = {CascadeType.DETACH})
    @JoinColumn(name = "category_id", referencedColumnName = "id", nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id", nullable = false)
    private Video video;

    public Point() {}

    public Point(double x, double y, Duration start, Category category, Video video) {
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
    public Point(double x, double y, Duration start, Category category) {
        this.x = x;
        this.y = y;
        this.start = start;
        this.category = category;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
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

    @Override
    public int compareTo(Point o) {
        int compare = this.start.compareTo(o.start);

        return compare;
    }
}

