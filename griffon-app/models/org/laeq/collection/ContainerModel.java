package org.laeq.collection;

import griffon.core.artifact.GriffonModel;
import griffon.core.i18n.MessageSource;
import griffon.metadata.ArtifactProviderFor;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonModel;
import org.laeq.model.Category;
import org.laeq.model.Collection;
import org.laeq.model.Preferences;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@ArtifactProviderFor(GriffonModel.class)
public class ContainerModel extends AbstractGriffonModel {
    private ObservableList<Collection> collections = FXCollections.observableArrayList();
    private Map<Category, SimpleBooleanProperty> categorySBP = new HashMap<>();
    private SimpleBooleanProperty isDefault = new SimpleBooleanProperty(this, "isDefault", false);
    private SimpleStringProperty name = new SimpleStringProperty(this, "name", "");
    private Collection selectedCollection;

    private Preferences preferences;

    private String errors = "";

    public void addCategories(Set<Category> categories){
        categories.forEach(category -> {
            SimpleBooleanProperty sbp = new SimpleBooleanProperty(false);
            categorySBP.put(category, sbp);
        });
    }

    public SimpleBooleanProperty getSBP(Category category){
        return categorySBP.get(category);
    }

    public ObservableList<Collection> getCollections() {
        return collections;
    }

    public void addCollections(Set<Collection> collections) {
        this.collections.addAll(collections);
    }

    public Set<Category> getCategories(){
        return categorySBP.keySet();
    }

    public void setIsDefault(boolean isDefault) {
        this.isDefault.set(isDefault);
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public boolean isIsDefault() {
        return isDefault.get();
    }

    public SimpleBooleanProperty isDefaultProperty() {
        return isDefault;
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setSelectedCollection(Collection collection) {
        clearForm();

        this.selectedCollection = collection;
        setName(collection.getName());
        setIsDefault(collection.isIsDefault());
        collection.getCategorySet().forEach(category -> categorySBP.get(category).setValue(true));
    }

    public void clearForm(){
        this.selectedCollection = null;
        setName("");
        setIsDefault(false);
        categorySBP.values().forEach(sbp -> sbp.setValue(false));
        errors = "";
    }

    public Collection getSelectedCollection() {
        return selectedCollection;
    }

    public Boolean valid() {
        Boolean result = true;

        MessageSource messageSource = getApplication().getMessageSource();

        StringBuilder builder = new StringBuilder();

        if(getName().length() == 0){
            builder.append("\n - Name");
            result = false;
        }

        Boolean isOneSelected = categorySBP.values().stream().anyMatch( e-> e.getValue());

        if(!isOneSelected){
            builder.append("\n - categories: ");
            builder.append("org.laeq.model.collection.validation.categories");
            result = false;
        }

//        errors = messageSource.getMessage("org.laeq.model.invalid_fields", Locale.CANADA);
        errors +=  builder.toString();

        return result;
    }

    public String getErrors() {
        return errors;
    }

    public Collection generateCollection() {
        Collection collection = new Collection();
        collection.setCategorySet(this.categorySBP.entrySet().stream().filter(x -> x.getValue().getValue()).map(map -> map.getKey()).collect(Collectors.toSet()));
        collection.setName(getName());
        collection.setIsDefault(isIsDefault());

        if(this.selectedCollection != null) {
            collection.setId(this.selectedCollection.getId());
        }

        return collection;
    }

    public void update(Collection collection) {
        if(this.selectedCollection != null){
            this.selectedCollection.setName(collection.getName());
            this.selectedCollection.setIsDefault(collection.isIsDefault());
            this.selectedCollection.getCategorySet().clear();
            this.selectedCollection.getCategorySet().addAll(collection.getCategorySet());
        } else {
            collections.add(collection);
        }


        if(this.selectedCollection.getProut()){
            this.selectedCollection.setProut(false);
        } else {
            this.selectedCollection.setProut(true);
        }

        clearForm();
    }

    public void delete(Collection collection) {
        collections.remove(collection);
    }

    public Preferences getPreferences() {
        return preferences;
    }

    public void setPreferences(Preferences preferences) {
        this.preferences = preferences;
    }
}
