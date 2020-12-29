package org.laeq;

import griffon.core.artifact.GriffonModel;
import griffon.metadata.ArtifactProviderFor;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonModel;
import org.laeq.model.Preferences;
import org.laeq.model.Point;
import org.laeq.model.Video;

import java.util.Collection;

@ArtifactProviderFor(GriffonModel.class)
public class StatisticModel extends AbstractGriffonModel {
    public ObservableList<Video> videos = FXCollections.observableArrayList();
    public SimpleIntegerProperty durationStep = new SimpleIntegerProperty(this, "durationStep", 5);
}