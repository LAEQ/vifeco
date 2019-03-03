package org.laeq.db

import org.laeq.model.Category
import org.laeq.model.Collection

class CollectionDAOTest extends AbstractDAOTest {
    CollectionDAO repository

    def setup(){
        repository = new CollectionDAO(manager, Collection.sequence_name)
    }

    def "test insertion"() {
        try{
            manager.loadFixtures(this.class.classLoader.getResource("sql/test_fixtures.sql"))
        } catch (Exception e){
            println e
        }

        def entity = new Collection()
        entity.setName("Mock NAME")
        entity.setIsDefault(false)
        entity.addCategory(new Category(1, "Moving truck", "icon/icon1.png", "#FFFFFF","A"))
        entity.addCategory(new Category(2, "Moving car", "icon/icon2.png", "#FFFFFF","B"))
        entity.addCategory(new Category(3, "Moving bike", "icon/icon3.png", "#FFFFFF","C"))
        entity.addCategory(new Category(4, "Moving bus", "icon/icon4.png",  "#FFFFFF","D"))


        when:
        repository.insert(entity)

        then:
        entity.id == 4
    }

    def "test update: modify name"() {
        try{
            manager.loadFixtures(this.class.classLoader.getResource("sql/test_fixtures.sql"))
        } catch (Exception e){
            println e
        }

        def entity = repository.findByID(1)
        entity.setName("Mock name")

        when:
        repository.update(entity)

        then:
        notThrown DAOException
    }

    def "test update: delete 2 categories"() {
        try{
            manager.loadFixtures(this.class.classLoader.getResource("sql/test_fixtures.sql"))
        } catch (Exception e){
            println e
        }

        def entity = repository.findByID(1);

        entity.removeCategory(1)
        entity.removeCategory(3)

        when:
        repository.update(entity)
        def result = repository.findCollectionIdsById(1)

        then:
        result.toArray() == [2, 4]
        notThrown DAOException
    }

    def "test update: delete 2 categories add 2 new ones"() {
        try{
            manager.loadFixtures(this.class.classLoader.getResource("sql/test_fixtures.sql"))
        } catch (Exception e){
            println e
        }

        def entity = repository.findByID(2);

        entity.removeCategory(2)
        entity.removeCategory(3)

        println entity.categorySet.size()

        entity.addCategory(new Category(1, "Moving", "Moving","F000000" , "A"))
        entity.addCategory(new Category(4, "Moving", "Moving", "F000000","A"))


        when:
        repository.update(entity)
        def result = repository.findCollectionIdsById(2)

        then:
        result.toArray() == [1, 4]
        notThrown DAOException
    }

    def "test: get list of ids of category for a specific collection."(){
        setup:
        try{
            manager.loadFixtures(this.class.classLoader.getResource("sql/test_fixtures.sql"))
        } catch (Exception e){
            println e
        }

        when:
        def result = repository.findCollectionIdsById(1)

        then:
        result.collect{it} == [1,2,3,4]
    }

    def "test insertion with an invalid category"(){
        setup:
        Collection entity = new Collection()
        entity.setName("mock")
        entity.addCategory(new Category(-1, "test", "test", "", "A"))

        when:
        repository.insert(entity)

        then:
        thrown DAOException
    }

    def "test findById"() {
        setup:
        try{
            manager.loadFixtures(this.class.classLoader.getResource("sql/test_fixtures.sql"))
        } catch (Exception e){
            println e
        }

        when:
        Collection result = repository.findByID(1)

        def expected = new Collection(1, "Default", false)
        expected.addCategory(new Category(1, "Moving truck", "icon/icon1.png",  "#FFFFFF","A"))
        expected.addCategory(new Category(2, "Moving car", "icon/icon2.png", "#FFFFFF","B"))
        expected.addCategory(new Category(3, "Moving bike", "icon/icon3.png",  "#FFFFFF","C"))
        expected.addCategory(new Category(4, "Moving bus", "icon/icon4.png",  "#FFFFFF","D"))

        then:
        result == expected

    }

    def "test findDefault"(){
        setup:
        try{
            manager.loadFixtures(this.class.classLoader.getResource("sql/test_fixtures.sql"))
        } catch (Exception e){
            println e
        }

        when:
        Collection result = repository.findDefault()

        then:
        result == new Collection(1, "Default", true)
        result.categorySet.size() == 4
    }

    def "test findAll but empty"() {
        setup:
        try{
            manager.loadFixtures(this.class.classLoader.getResource("sql/test_fixtures.sql"))
        } catch (Exception e){
            println e
        }

        when:
        def result = repository.findAll()

        then:
        result.size() == 3

    }

    def "test delete default collection fails successfully. You cannot delete it!"() {
        setup:
        try{
            manager.loadFixtures(this.class.classLoader.getResource("sql/test_fixtures.sql"))
        } catch (Exception e){
            println e
        }

        Collection categoryCollection = new Collection(1, "mock", false)

        when:
        repository.delete(categoryCollection)

        then:
        thrown DAOException
    }

    def "test delete a regular collection" (){
        setup:
        try{
            manager.loadFixtures(this.class.classLoader.getResource("sql/test_fixtures.sql"))
        } catch (Exception e){
            println e
        }

        Collection categoryCollection = new Collection(2, "mock", false)

        when:
        repository.delete(categoryCollection)

        then:
        notThrown DAOException
    }
}