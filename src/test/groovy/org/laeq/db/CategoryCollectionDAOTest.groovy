package org.laeq.db

import org.laeq.model.Category
import org.laeq.model.CategoryCollection

class CategoryCollectionDAOTest extends AbstractDAOTest {
    def repository;

    def setup(){
        repository = new CategoryCollectionDAO(manager, CategoryCollection.sequence_id)
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
        try{
            manager.loadFixtures(this.class.classLoader.getResource("sql/fixtures.sql"))
        } catch (Exception e){
            println e
        }

        def entity = new CategoryCollection()
        entity.setName("Mock NAME")
        entity.addCategory(new Category(1, "Moving truck", "icon/icon1.png", "A"))
        entity.addCategory(new Category(2, "Moving car", "icon/icon2.png", "B"))
//        entity.addCategory(new Category(3, "Moving bike", "icon/icon3.png", "C"))
//        entity.addCategory(new Category(4, "Moving bus", "icon/icon4.png", "D"))


        when:
        repository.insert(entity)

        then:
        entity.id == 4
    }

//    def "test insertion with an invalid category"(){
//        setup:
//        Category category = new Category("", "mock icon", "A")
//
//        when:
//        repository.insert(category)
//
//        then:
//        notThrown DAOException
//    }

    def "test findById"() {
        setup:
        try{
            manager.loadFixtures(this.class.classLoader.getResource("sql/fixtures.sql"))
        } catch (Exception e){
            println e
        }

        when:
        CategoryCollection result = repository.findByID(1)

        def expected = new CategoryCollection(1, "Default")
        expected.addCategory(new Category(1, "Moving truck", "icon/icon1.png", "A"))
        expected.addCategory(new Category(2, "Moving car", "icon/icon2.png", "B"))
        expected.addCategory(new Category(3, "Moving bike", "icon/icon3.png", "C"))
        expected.addCategory(new Category(4, "Moving bus", "icon/icon4.png", "D"))

        then:
        result == expected

    }
//
//    def "test findAll but empty"() {
//        when:
//        def result = repository.findAll()
//
//        then:
//        result.size() == 0
//    }
//
//    def "test delete an existing category"() {
//        setup:
//        try{
//            manager.loadFixtures(this.class.classLoader.getResource("sql/fixtures.sql"))
//        } catch (Exception e){
//            println e
//        }
//
//        Category category = new Category(1, "mock", "mock", "A")
//
//        when:
//        repository.delete(category)
//
//        then:
//        notThrown DAOException
//    }
//
//    def "test delete an unknown category" (){
//        setup:
//        try{
//            manager.loadFixtures(this.class.classLoader.getResource("sql/fixtures.sql").toURI().getPath())
//        } catch (Exception e){
//            println e
//        }
//
//        Category category = new Category(-1, "mock", "mock", "A")
//        when:
//        repository.delete(category)
//
//        then:
//        thrown DAOException
//    }
}
