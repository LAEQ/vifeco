package org.laeq.db

import org.laeq.model.Category
import org.laeq.model.Collection

class CategoryDAOTest extends AbstractDAOTest {
    def repository

    def setup(){
        repository = new CategoryDAO(manager)
    }

    def "test insertion"() {
        setup:
        Category category = new Category("mock name", "mock icon", "#FFFFFF", "A")

        when:
        repository.insert(category)

        then:
        category == new Category(1, "mock name", "mock icon", "#FFFFFF", "A")
    }

    def "test insertion with an invalid category"(){
        setup:
        Category category = new Category("", "mock icon", "#FFFFFF", "A")

        when:
        repository.insert(category)

        then:
        notThrown DAOException
    }

    def "test findAll"() {
        setup:
        Category user1 = new Category("mock name A", "mock icon", "#FFFFFF", "A")
        Category user2 = new Category("mock name B", "mock icon", "#FFFFFF", "B")

        repository.insert(user1)
        repository.insert(user2)

        when:
        def result = repository.findAll()

        then:
        result.size() == 2
        result.contains(new Category(1, "mock name A", "mock icon A", "#FFFFFF", "A")) == true
        result.contains(new Category(2,"mock name B", "mock icon B", "#FFFFFF", "B")) == true
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
            manager.loadFixtures("sql/test_fixtures.sql")
        } catch (Exception e){
            println e
        }

        Category category = new Category(1, "mock", "mock", "#FFFFFF","A")

        when:
        repository.delete(category)

        then:
        notThrown DAOException
    }

    def "test delete an unknown category" (){
        setup:
        try{
            manager.loadFixtures("sql/test_fixtures.sql")
        } catch (Exception e){
            println e
        }

        Category category = new Category(-1, "mock", "mock", "#000000", "A")
        when:
        repository.delete(category)

        then:
        thrown DAOException
    }

    def "test find by collection"(){
        setup:
        try{
            manager.loadFixtures("sql/test_fixtures.sql")
        } catch (Exception e){
            println e
        }

        Collection collection = new Collection(1, "default", true)
        when:
        Set<Category> result = repository.findByCollection(collection)

        then:
        result.size() == 4
        result.collect{it.id}.sort() == [1, 2, 3, 4]
    }
}
