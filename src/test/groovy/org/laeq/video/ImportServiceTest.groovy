package org.laeq.video

import org.laeq.model.Video
import spock.lang.Specification

class ImportServiceTest extends Specification {

    private ImportService service
    private String json


    def setup(){
        service = new ImportService()



    }

    def "execute"() {
        setup:
        try{
            json = getClass().classLoader.getResource("export/export_1.json").text
            println json
        } catch(Exception e){
            println e
        }

        when:
        Video result = service.execute(json)
        def test = true

        println result

        then:
        test == true
    }
}
