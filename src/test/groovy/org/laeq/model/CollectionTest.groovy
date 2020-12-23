package org.laeq.model

import com.fasterxml.jackson.databind.ObjectMapper
import spock.lang.Specification

class CollectionTest extends Specification {

    Collection entity

    def setup(){
        entity = new Collection(1, "mock", false)
    }

    def "AddCategory"() {
        when:
        entity.addCategory(new Category(1, "category 1", "icon", "F00000","A"))
        entity.addCategory(new Category(2, "category 2",  "icon", "F000000","A"))
        entity.addCategory(new Category(1, "category 1",  "icon", "F000000","A"))

        then:
        entity.getCategories().size() == 2
        entity.categories.collect { it.id } == [1, 2]

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
        entity.getCategories().size() == 1
        entity.categories.collect{ it.id} == [1]
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
        entity.getCategories().size() == 3
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

        Collection categoryCollection = new Collection(1, "collection", false)
        categoryCollection.addCategory(category1)
        categoryCollection.addCategory(category2)
        categoryCollection.addCategory(category3)

        when:
        String result = new ObjectMapper().writeValueAsString(categoryCollection)

        String expected = '{"id":1,"name":"collection","isDefault":false,"categorySet":[{"id":1,"name":"category 1","icon":"icon 1","color":"color 1","shortcut":"A"},{"id":2,"name":"category 2","icon":"icon 2","color":"color 2","shortcut":"B"},{"id":3,"name":"category 3","icon":"icon 3","color":"color 3","shortcut":"C"}]}'

        then:
        result == expected
    }

    def "deserialization" (){
        setup:
        String json = '{\n' +
                '    "id": 1,\n' +
                '    "name": "Default",\n' +
                '    "isDefault": false,\n' +
                '    "categorySet": [\n' +
                '      {\n' +
                '        "id": 1,\n' +
                '        "name": "Moving car",\n' +
                '        "icon": "mock1",\n' +
                '        "color": "#000000",\n' +
                '        "shortcut": "A"\n' +
                '      },\n' +
                '      {\n' +
                '        "id": 2,\n' +
                '        "name": "Moving bike",\n' +
                '        "icon": "mock2",\n' +
                '        "color": "#000000",\n' +
                '        "shortcut": "S"\n' +
                '      },\n' +
                '      {\n' +
                '        "id": 3,\n' +
                '        "name": "Moving truck",\n' +
                '        "icon": "mock3",\n' +
                '        "color": "#000000",\n' +
                '        "shortcut": "D"\n' +
                '      },\n' +
                '      {\n' +
                '        "id": 4,\n' +
                '        "name": "Stopped car",\n' +
                '        "icon": "mock4",\n' +
                '        "color": "#000000",\n' +
                '        "shortcut": "F"\n' +
                '      }\n' +
                '    ]\n' +
                '  }'
        ObjectMapper mapper = new ObjectMapper()

        when:
        Collection result = mapper.readValue(json, Collection.class)

        then:
        result == new Collection(1, "Default", false)
        result.id == 1
        result.name == "Default"
        result.isDefault == false
        result.categories.contains(new Category(1, 'Moving car', 'mock1', '#000000', 'A')) == true
    }
}
