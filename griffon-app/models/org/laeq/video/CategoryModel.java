package org.laeq.video;

import griffon.core.artifact.GriffonModel;
import griffon.metadata.ArtifactProviderFor;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.SimpleIntegerProperty;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonModel;
import org.laeq.model.Category;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Set;

@ArtifactProviderFor(GriffonModel.class)
public class CategoryModel extends AbstractGriffonModel {

    private HashMap<Category, SimpleIntegerProperty> categoryPropertyList;
    private IntegerBinding totalCount;

    public IntegerBinding totalCountProperty() {
        return totalCount;
    }

    public int getTotalCount() {
        return totalCount.get();
    }

    public void generateProperties(Set<Category> keySet) {

        categoryPropertyList = new HashMap<>();

        keySet.forEach(s -> {
            categoryPropertyList.put(s, new SimpleIntegerProperty(this, s.getName(), 0));
        });

        totalCount = Bindings.createIntegerBinding(() -> categoryPropertyList.values().stream().mapToInt(SimpleIntegerProperty::getValue).sum());
    }

    public SimpleIntegerProperty getCategoryProperty(Category category){
        return categoryPropertyList.get(category);
    }
}