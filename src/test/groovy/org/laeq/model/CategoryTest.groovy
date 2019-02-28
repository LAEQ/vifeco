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
}
