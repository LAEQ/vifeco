package org.laeq.db

import spock.lang.Specification

class VideoUserDAOTest extends AbstractDAOTest {
    def repository
    def setup(){
        repository = new VideoUserDAO(manager, "notUsed")
    }


    def "test findAll"() {
        setup:
        try{
            manager.loadFixtures(this.class.classLoader.getResource("sql/fixtures.sql"))
        } catch (Exception e){
            println e
        }

        when:
        def result = repository.findAll()

        then:
        result.size() == 5
        result.collect{it.video.id} == [1, 1, 2, 3, 4]
        result.collect{it.total} == [5, 3, 2 , 0, 0]
        result.collect{it.user.id} == [1, 2, 1, 0, 0]

    }
}
