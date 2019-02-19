package org.laeq.video

import org.laeq.model.Category
import spock.lang.Specification

class CategoryModelTest extends Specification {

    def "GetTotalCount"() {
        given: "A number of categories"

        String[] c = ["a", "b", "c", "d"]
        Set<Category> set = new HashSet<>();

        for (int i = 0; i < c.length; i++) {
            set.add(new Category(c[i],c[i], "F000000", c[i]))
        }

        when:
        CategoryModel model = new CategoryModel()
        model.generateProperties(set)

        set.each { model.getCategoryProperty(it).set(11)}

        set.each {model.getCategoryProperty(it).set(model.getCategoryProperty(it).get() + 1)}

        then:
        model.getTotal() == 48
    }
}
