package org.laeq.db

import org.laeq.model.Category
import org.laeq.model.User
import spock.lang.Specification

class CategoryDAOTest extends AbstractDAOTest {
    def repository;

    def setup(){
        repository = new CategoryDAO(manager, "category_id")
    }

    def "test get next id"() {
        when:
        repository.getNextValue()
        repository.getNextValue()
        int result = repository.getNextValue()

        then:
        result == 3
    }

    def "test insertion"() {
        setup:
        Category category = new Category("mock name", "mock icon", "A")

        when:
        repository.insert(category)

        then:
        category == new Category(1, "mock name", "mock icon", "A")
    }

    def "test insertion with an invalid category"(){
        setup:
        Category category = new Category("", "mock icon", "A")

        when:
        repository.insert(category)

        then:
        notThrown DAOException
    }

    def "test findAll"() {
        setup:
        Category user1 = new Category("mock name A", "mock icon A", "A")
        Category user2 = new Category("mock name B", "mock icon B", "B")

        repository.insert(user1)
        repository.insert(user2)

        when:
        def result = repository.findAll()

        then:
        result.size() == 2
        result.contains(new Category(1, "mock name A", "mock icon A", "A")) == true
        result.contains(new Category(2,"mock name B", "mock icon B", "B")) == true
    }

    def "test findAll but empty"() {
        when:
        def result = repository.findAll()

        then:
        result.size() == 0
    }

    def "test delete an existing category"() {
        setup:
        try{
            manager.loadFixtures(this.class.classLoader.getResource("sql/fixtures.sql"))
        } catch (Exception e){
            println e
        }

        Category category = new Category(1, "mock", "mock", "A")

        when:
        repository.delete(category)

        then:
        notThrown DAOException
    }

    def "test delete an unknown category" (){
        setup:
        try{
            manager.loadFixtures(this.class.classLoader.getResource("sql/fixtures.sql").toURI().getPath())
        } catch (Exception e){
            println e
        }

        Category category = new Category(-1, "mock", "mock", "A")
        when:
        repository.delete(category)

        then:
        thrown DAOException
    }
}
