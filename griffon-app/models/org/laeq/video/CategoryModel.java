package org.laeq.video;

import griffon.core.artifact.GriffonModel;
import griffon.metadata.ArtifactProviderFor;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.LongBinding;
import javafx.beans.property.SimpleLongProperty;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonModel;
import org.laeq.model.Category;
import org.laeq.model.VideoUser;
import org.laeq.video.category.CategoryGroup;

import java.util.HashMap;
import java.util.Set;

@ArtifactProviderFor(GriffonModel.class)
public class CategoryModel extends AbstractGriffonModel {
    private final HashMap<Category, CategoryGroup> categoryList = new HashMap<>();
    private HashMap<Category, SimpleLongProperty> categoryPropertyList;
    private LongBinding total;
    private VideoUser videoUser;

    public long getTotal() {
        return total.get();
    }
    public LongBinding totalProperty() {
        return total;
    }

    public void generateProperties() {
        categoryPropertyList = new HashMap<>();

        videoUser.getVideo().getCategoryCollection().getCategorySet().forEach(category -> {
            long total = videoUser.getPoints().stream().filter(point -> {
                return point.getCategory().equals(category);
            }).count();

            categoryPropertyList.put(category, new SimpleLongProperty(this, category.getName(), total));
            SimpleLongProperty test = new SimpleLongProperty(this, category.getName(), total);
        });

        total = Bindings.createLongBinding(() -> categoryPropertyList.values().stream().mapToLong(SimpleLongProperty::getValue).sum());
    }

    public SimpleLongProperty getCategoryProperty(Category category){
        return categoryPropertyList.get(category);
    }

    public HashMap<Category, SimpleLongProperty> getCategoryPropertyList() {
        return categoryPropertyList;
    }


    public void setItem(VideoUser videoUser) {
        this.videoUser = videoUser;
        generateProperties();
    }

    public Set<Category> getCategorySet() {
        return this.videoUser.getVideo().getCategoryCollection().getCategorySet();
    }
}