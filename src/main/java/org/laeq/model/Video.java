package org.laeq.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.util.Duration;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.hibernate.annotations.GenericGenerator;
import org.laeq.settings.Settings;

import javax.persistence.*;
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
@Entity
@Table(name = "video")
public class Video {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator",
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String path;

    @Column(nullable = false)
    private Double duration;

    @ManyToOne()
    @JoinColumn(name = "collection_id")
    private Collection collection;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;


    private SortedSet<Point> pointSet;


    @JsonIgnore
    public String getDurationFormatted(){
        return "";
//        return DurationFormatUtils.formatDuration(duration.getValue().longValue(), "H:mm:ss", true);
    }

    @JsonIgnore
    public String getAbsolutePath(){
        return "";
//        return String.format("%s%s%s", Settings.videoPath, File.separator, this.path.getValue());
    }

    public void addPoint(Point point){
        pointSet.add(point);
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

    @JsonIgnore
    public Map<Category, Long> getTotalByCategory() {
        return pointSet.stream().collect(Collectors.groupingBy(Point::getCategory, Collectors.counting()));
    }

    @JsonIgnore
    public boolean isEditable(){
        return true;
//        return new File(path.getValue()).exists();
    }
}
