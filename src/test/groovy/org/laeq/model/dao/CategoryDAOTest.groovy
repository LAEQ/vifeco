package org.laeq.model.dao

import org.laeq.db.HibernateUtil
import org.laeq.model.Category
import spock.lang.Specification

class CategoryDAOTest extends Specification {
    CategoryDAO dao
    void setup() {
        dao = new CategoryDAO(new HibernateUtil('hibernate.cfg.xml'))
    }

    void cleanup() {

    }

    def "test insertion"() {
        setup:
        Category category = new Category("mock name", "mock icon", "#FFFFFF", "A")

        when:
        dao.create(category)

        then:
        category == new Category(1, "mock name", "mock icon", "#FFFFFF", "A")
    }

    def "test delete"() {
        setup:
        Category category = new Category("mock name", "mock icon", "#FFFFFF", "A")
        dao.create(category)

        when:
        dao.delete(category)

        then:
        notThrown Exception
    }

    def "test findAll"(){
        setup:
        Category category = new Category("mock name", "mock icon", "#FFFFFF", "A")
        Category category2 = new Category("mock name", "mock icon", "#FFFFFF", "B")
        dao.create(category)
        dao.create(category2)

        when:
        List<Category> list = dao.findAll();

        then:
        list.size() == 2
    }
}
