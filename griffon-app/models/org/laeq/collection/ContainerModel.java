package org.laeq.collection;

import griffon.core.artifact.GriffonModel;
import griffon.metadata.ArtifactProviderFor;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonModel;
import org.laeq.model.CategoryCollection;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Set;

@ArtifactProviderFor(GriffonModel.class)
public class ContainerModel extends AbstractGriffonModel {
    private ObservableList<CategoryCollection> collections = FXCollections.observableArrayList();

    public ObservableList<CategoryCollection> getCollections() {
        return collections;
    }

    public void addCollections(Set<CategoryCollection> collections) {
        this.collections.addAll(collections);
    }
}