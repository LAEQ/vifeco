package org.laeq.model

import com.fasterxml.jackson.databind.ObjectMapper
import spock.lang.Specification

class CategoryTest extends Specification {
    def "serialization" (){
        setup:
        Category category = new Category(1, "test", "test", "color","A")

        when:
        String result = new ObjectMapper().writeValueAsString(category)

        String expected = '{"id":1,"name":"test","icon":"test","color":"color","shortcut":"A"}'

        then:
        result == expected
    }

    def "deserialize" () {
        setup:
        String json = '{\n' +
                '        "id": 1,\n' +
                '        "name": "Moving car",\n' +
                '        "icon": "mock",\n' +
                '        "color": "#000000",\n' +
                '        "shortcut": "A"\n' +
                '      }'

        ObjectMapper mapper = new ObjectMapper()

        when:
        Category result = mapper.readValue(json, Category.class)
        Category category = new Category(1, 'Moving car', 'mock', '#000000', 'A')

        then:
        result == category
        result.name == "Moving car"
        result.icon == "mock"
        result.color == "#000000"
        result.shortcut == "A"
    }
}
