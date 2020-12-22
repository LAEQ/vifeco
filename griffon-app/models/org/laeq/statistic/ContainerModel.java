package org.laeq.statistic;

import griffon.core.artifact.GriffonModel;
import griffon.metadata.ArtifactProviderFor;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonModel;
import org.laeq.model.Preferences;
import org.laeq.model.Point;

import java.util.Collection;

@ArtifactProviderFor(GriffonModel.class)
public class ContainerModel extends AbstractGriffonModel {
    private ObservableList<Point> videos = FXCollections.observableArrayList();
    private SimpleIntegerProperty durationStep = new SimpleIntegerProperty(this, "durationStep", 5);
    private Preferences prefs;

    public ObservableList<Point> getVideos() {
        return videos;
    }

    public void addVideos(Collection<Point> videos) {
        this.videos.addAll(videos);
    }

    public Preferences getPrefs() {
        return prefs;
    }

    public void setPrefs(Preferences prefs) {
        this.prefs = prefs;
    }

    public int getDurationStep() {
        return durationStep.get();
    }

    public SimpleIntegerProperty durationStepProperty() {
        return durationStep;
    }

    public void setDurationStep(int durationStep) {
        this.durationStep.set(durationStep);
    }
}