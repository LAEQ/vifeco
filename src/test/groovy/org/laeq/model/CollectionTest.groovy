package org.laeq.model

import com.fasterxml.jackson.databind.ObjectMapper
import spock.lang.Specification

class CollectionTest extends Specification {

    Collection entity

    def setup(){
        entity = new Collection("mock")
        entity.setId(1)
        entity.setDefault(Boolean.TRUE)
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

    def "has category"(){
        setup:
        entity.addCategory(new Category(1, "category 1", "icon", "F00000","A"))

        expect:
        entity.hasCategory(new Category(1, "category 1", "icon", "F00000","A")) == true
    }

    def "has not category"(){
        setup:
        entity.addCategory(new Category(1, "category 1", "icon", "F00000","A"))

        expect:
        entity.hasCategory(new Category(2, "category 1", "icon", "F00000","A")) == false
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

        String expected = '{"name":"collection","categories":[{"id":1,"name":"category 1"},{"id":2,"name":"category 2"},{"id":3,"name":"category 3"}]}'

        then:
        result == expected
    }

    def "deserialization" () {
        setup:
        String json = '{\n' +
                '    "id": 1,\n' +
                '    "name": "Default",\n' +
                '    "isDefault": false,\n' +
                '    "categories": [\n' +
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
        result.name == "Default"
        result.categories.size() == 4
    }
}
