package org.laeq.menu;

import griffon.core.Observable;
import griffon.core.artifact.GriffonModel;
import griffon.metadata.ArtifactProviderFor;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonModel;

import javax.annotation.Nonnull;

@ArtifactProviderFor(GriffonModel.class)
public class BottomModel extends AbstractGriffonModel {
    public SimpleStringProperty message = new SimpleStringProperty(this, "message", "");
    public ObservableList<String> styles = FXCollections.observableArrayList();
}