package org.laeq.menu;

import griffon.core.artifact.GriffonModel;
import griffon.metadata.ArtifactProviderFor;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonModel;

@ArtifactProviderFor(GriffonModel.class)
public class BottomModel extends AbstractGriffonModel {
    public SimpleStringProperty message = new SimpleStringProperty(this, "message", "");
    public ObservableList<String> styles = FXCollections.observableArrayList();
}