package org.laeq;

import griffon.core.artifact.GriffonModel;
import griffon.metadata.ArtifactProviderFor;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonModel;
import org.laeq.model.Category;
import org.laeq.model.Collection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@ArtifactProviderFor(GriffonModel.class)
public class CollectionModel extends AbstractGriffonModel {
    public ObservableList<Collection> collections = FXCollections.observableArrayList();
    public ObservableList<Category> categories = FXCollections.observableArrayList();
    public Map<Category, SimpleBooleanProperty> categorySBP = new HashMap<>();
    public SimpleStringProperty name = new SimpleStringProperty(this, "name", "");
    public Collection selectedCollection;

    public void addCategories(List<Category> categories){
        categories.forEach(category -> {
            SimpleBooleanProperty sbp = new SimpleBooleanProperty(false);
            categorySBP.put(category, sbp);
        });
    }


    public void setSelectedCollection(Collection collection) {
        this.selectedCollection = collection;

        name.set(collection.getName());
        collection.getCategories().forEach(category -> categorySBP.get(category).setValue(true));
    }

    public void clear(){
        this.selectedCollection = null;
        name.set("");
        categorySBP.values().forEach(sbp -> sbp.setValue(false));
    }

    public Set<Category> getCategories(){
        return categorySBP.keySet();
    }

    public Collection getCollection() {
        if(this.selectedCollection == null){
            this.selectedCollection = new Collection();
            this.selectedCollection.setDefault(Boolean.FALSE);
        }

        this.selectedCollection.setName(name.get());
        this.selectedCollection.getCategories().clear();

        for (Map.Entry<Category, SimpleBooleanProperty> me : categorySBP.entrySet()) {
            if(me.getValue().getValue()){
                this.selectedCollection.getCategories().add(me.getKey());
            }
        }

        return this.selectedCollection;
    }
}
