package org.laeq.model;

import javafx.util.Duration;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class Video implements Entity{
    private Integer id;
    private String path;
    private String name;
    private Duration duration;

    public Video(Integer id, String path, Duration duration) {
        this.id = id;
        this.path = path;
        this.name = Paths.get(path).getFileName().toString();
        this.duration = duration;
    }

    public Video(String path, Duration duration) {
        this.path = path;
        this.duration = duration;

        Path filePath = Paths.get(path);

        this.name = (filePath.getFileName().toString());
    }

    public Video() {

    }

    public void setPath(String path) {
        this.path = path;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }
    public String getName() {
        return name;
    }
    public Duration getDuration() {
        return duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Video video = (Video) o;
        return id.equals(video.id) &&
                path.equals(video.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, path);
    }
}
