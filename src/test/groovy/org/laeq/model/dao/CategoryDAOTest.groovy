package org.laeq.model.dao


import org.laeq.model.Category
import spock.lang.Specification

class CategoryDAOTest extends Specification {
    CategoryDAO dao
    HibernateUtil util
    void setup() {
        util = new HibernateUtil('hibernate.cfg.xml')
        dao = new CategoryDAO(util)
    }

    void cleanup() {
        util.shutdown()
    }

    def "test insertion"() {
        setup:
        Category category = EntityGenerator.createCategory("A")

        when:
        dao.create(category)

        then:
        category.getId() != null
    }


    def "test invalid name"() {
        setup:
        Category category = EntityGenerator.createCategory("A")
        category.setName("")

        when:
        def result = dao.create(category)

        then:
        result == false
    }

    def "test delete"() {
        setup:
        Category category = EntityGenerator.createCategory("A")
        dao.create(category)

        when:
        dao.delete(category)

        then:
        notThrown Exception
    }

    def "test findAll"(){
        setup:
        Category category = EntityGenerator.createCategory("A")
        Category category2 = EntityGenerator.createCategory("B")
        dao.create(category)
        dao.create(category2)

        when:
        List<Category> list = dao.findAll();

        then:
        list.size() == 2
    }
}
