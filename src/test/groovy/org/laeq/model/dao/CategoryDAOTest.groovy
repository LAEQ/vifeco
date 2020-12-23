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
        Category category = EntityGenerator.createCategory("A")

        when:
        dao.create(category)

        then:
        category.getId() != null
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
