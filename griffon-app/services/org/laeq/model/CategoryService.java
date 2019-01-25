package org.laeq.model;

import griffon.core.artifact.GriffonService;
import griffon.metadata.ArtifactProviderFor;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonService;

@javax.inject.Singleton
@ArtifactProviderFor(GriffonService.class)
public class CategoryService extends AbstractGriffonService {

    public Category[] getCategoryList(){
        String[] icons = new String[]{
                "icons/truck-mvt-blk.png",
                "icons/truck-mvt-red.png",
                "icons/icon-bicycle-mvt-64.png",
                "icons/icon-car-co2-black-64.png",
                "icons/icon-constr-black-64.png",
                "icons/iconmonstr-car-23-64.png",
                "icons/icon-car-elec-black-64.png",
        };

        Category[] categories = new Category[icons.length];

        for (int i = 0; i < icons.length; i++) {
            String name = icons[i].substring(6, icons[i].lastIndexOf('.') - 1);
            String path = getApplication().getResourceHandler().getResourceAsURL(icons[i]).getPath();
            Category category = new Category(name, path, "1");
            categories[i] = category;
        }

        return categories;
    }

}