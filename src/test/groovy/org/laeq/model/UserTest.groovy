package org.laeq.model

import com.fasterxml.jackson.databind.ObjectMapper
import spock.lang.Specification

class UserTest extends Specification {
    def "serialize"(){
        setup:
        User user = new User(1, "test", "test", true)

        when:
        String result = new ObjectMapper().writeValueAsString(user)

        println result

        String expected = '{"id":1,"firstName":"test","lastName":"test"}'

        then:
        result == expected
    }

    def "deserialize" (){
        setup:
        String json = '{\n' +
                '    "id": 3,\n' +
                '    "firstName": "David",\n' +
                '    "lastName": "Maignan"\n'+
                '  }'

        ObjectMapper mapper = new ObjectMapper()

        when:
        User result = mapper.readValue(json, User.class)

        then:
        result.id == 3
    }
}
