package org.laeq.model

import com.fasterxml.jackson.databind.ObjectMapper
import spock.lang.Specification

class UserTest extends Specification {
    def "serialize"(){
        setup:
        User user = new User(1, "test", "test", "test@email")

        when:
        String result = new ObjectMapper().writeValueAsString(user)

        println result

        String expected = '{"id":1,"firstName":"test","lastName":"test","email":"test@email","isActive":false}'

        then:
        result == expected
    }
}
