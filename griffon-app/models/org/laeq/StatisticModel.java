package org.laeq;

import griffon.core.artifact.GriffonModel;
import griffon.metadata.ArtifactProviderFor;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonModel;
import org.laeq.model.Video;
import org.laeq.model.statistic.Tarjan;

@ArtifactProviderFor(GriffonModel.class)
public class StatisticModel extends AbstractGriffonModel {
    public ObservableList<Video> videos = FXCollections.observableArrayList();
    public SimpleIntegerProperty durationStep = new SimpleIntegerProperty(this, "durationStep", 5);

    public ObservableList<Tarjan> tarjans = FXCollections.observableArrayList();

    public SimpleStringProperty videoName = new SimpleStringProperty();
    public SimpleStringProperty user1 = new SimpleStringProperty();
    public SimpleStringProperty user2 = new SimpleStringProperty();
    public SimpleStringProperty collection = new SimpleStringProperty();
    public SimpleStringProperty duration = new SimpleStringProperty();

    public void reset() {
        tarjans.clear();
        videoName.set("");
        user1.set("");
        user2.set("");
        collection.set("");
        duration.set("");
    }
}