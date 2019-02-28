package org.laeq.model

import com.fasterxml.jackson.databind.ObjectMapper
import spock.lang.Specification

class CategoryCollectionTest extends Specification {

    CategoryCollection entity

    def setup(){
        entity = new CategoryCollection(1, "mock", false)
    }

    def "AddCategory"() {
        when:
        entity.addCategory(new Category(1, "category 1", "icon", "F00000","A"))
        entity.addCategory(new Category(2, "category 2",  "icon", "F000000","A"))
        entity.addCategory(new Category(1, "category 1",  "icon", "F000000","A"))

        then:
        entity.getCategorySet().size() == 2
        entity.categorySet.collect { it.id } == [1, 2]

    }

    def "RemoveCategory"() {
        setup:
        Category category = new Category(2, "category 2",  "icon", "F000000", "A")
        entity.addCategory(new Category(1, "category 1", "icon", "F000000","A"))
        entity.addCategory(category)
        entity.addCategory(new Category(3, "category 3",  "icon", "F00000","A"))

        when:
        entity.removeCategory(3)
        entity.removeCategory(category)

        then:
        entity.getCategorySet().size() == 1
        entity.categorySet.collect{ it.id} == [1]
    }

    def "RemoveCategory not exist"() {
        setup:
        Category category = new Category(2, "category 2",  "icon", "F000000","A")
        entity.addCategory(new Category(1, "category 1", "icon", "F000000","A"))
        entity.addCategory(category)
        entity.addCategory(new Category(3, "category 3",  "icon", "F000000","A"))

        when:
        entity.removeCategory(4)
        entity.removeCategory(new Category(5, "test", "test", "F000000" , "test"))

        then:
        entity.getCategorySet().size() == 3
    }

    def "Get category ids"(){
        when:
        Category category = new Category(2, "category 2",  "icon", "F000000","A")
        entity.addCategory(new Category(1, "category 1", "icon", "F00000","A"))
        entity.addCategory(category)
        entity.addCategory(new Category(3, "category 3",  "icon", "FFFFFF","A"))


        then:
        entity.getCategoryIds().size() == 3
        entity.getCategoryIds() == [1, 2, 3]
    }
    
    def "Get getNewCategories"(){
        setup:
        1.upto(11, {
            entity.addCategory(new Category(it, "category 1", "icon", "F000000","A"))
        })

        List<Integer> ids = [1,2,4,5,6,7,8,9]

        when:
        def result = entity.getNewCategories(ids)

        then:
        result.size() == 3
    }

    def "serialization"(){
        setup:
        Category category1 = new Category(1, "category 1", "icon 1", "color 1", "A")
        Category category2= new Category(2, "category 2", "icon 2", "color 2", "B")
        Category category3 = new Category(3, "category 3", "icon 3", "color 3", "C")

        CategoryCollection categoryCollection = new CategoryCollection(1, "collection", false)
        categoryCollection.addCategory(category1)
        categoryCollection.addCategory(category2)
        categoryCollection.addCategory(category3)

        when:
        String result = new ObjectMapper().writeValueAsString(categoryCollection)

        String expected = '{"id":1,"name":"collection","isDefault":false,"categorySet":[{"id":1,"name":"category 1","icon":"icon 1","color":"color 1","shortcut":"A"},{"id":2,"name":"category 2","icon":"icon 2","color":"color 2","shortcut":"B"},{"id":3,"name":"category 3","icon":"icon 3","color":"color 3","shortcut":"C"}]}'

        then:
        result == expected

    }
}
