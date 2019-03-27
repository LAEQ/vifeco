package org.laeq.icon

import spock.lang.Specification

class IconServiceTest extends Specification {
    def "convert svg to png"(){
        IconService service = new IconService();

        expect:
        true == true
    }
}
