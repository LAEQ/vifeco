package org.laeq.video;

import griffon.core.artifact.GriffonModel;
import griffon.metadata.ArtifactProviderFor;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.SimpleIntegerProperty;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonModel;
import org.laeq.model.Category;

import java.util.HashMap;
import java.util.Set;

@ArtifactProviderFor(GriffonModel.class)
public class CategoryModel extends AbstractGriffonModel {

    private HashMap<Category, SimpleIntegerProperty> categoryPropertyList;
    private IntegerBinding totalCount;
    private SimpleIntegerProperty total;

    public IntegerBinding totalCountProperty() {
        return totalCount;
    }

    public int getTotal() {
        return total.get();
    }

    public SimpleIntegerProperty totalProperty() {
        return total;
    }

    public void setTotal(int total) {
        this.total.set(total);
    }

    public int getTotalCount() {
        return totalCount.get();
    }

    public void generateProperties(Set<Category> keySet) {

        categoryPropertyList = new HashMap<>();

        keySet.forEach(s -> {
            categoryPropertyList.put(s, new SimpleIntegerProperty(this, s.getName(), 0));
        });

        total = new SimpleIntegerProperty(0);

        totalCount = Bindings.createIntegerBinding(() -> categoryPropertyList.values().stream().mapToInt(SimpleIntegerProperty::getValue).sum());
    }

    public SimpleIntegerProperty getCategoryProperty(Category category){
        return categoryPropertyList.get(category);
    }

    public HashMap<Category, SimpleIntegerProperty> getCategoryPropertyList() {
        return categoryPropertyList;
    }
}