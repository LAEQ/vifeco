package org.laeq.model;

import javafx.beans.property.SimpleStringProperty;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Video {
    private final SimpleStringProperty path;
    private final SimpleStringProperty name;

    public Video(String path) {
        this.path = new SimpleStringProperty(path);

        Path filePath = Paths.get(path);

        this.name = new SimpleStringProperty(filePath.getFileName().toString());
    }

    public String getPath() {
        return path.get();
    }

    public SimpleStringProperty pathProperty() {
        return path;
    }

    public void setPath(String path) {
        this.path.set(path);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(Paths.get(getPath()).getFileName().toString());
    }
}
