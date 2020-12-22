package org.laeq.model.dao

import org.laeq.db.HibernateUtil
import org.laeq.model.Category
import org.laeq.model.Collection
import spock.lang.Specification

class CollectionDAOTest extends Specification {
    CollectionDAO dao;
    CategoryDAO catDao;
    Category category_1, category_2
    void setup() {
        catDao = new CategoryDAO(new HibernateUtil('hibernate.cfg.xml'))
        dao = new CollectionDAO(new HibernateUtil('hibernate.cfg.xml'))

        category_1 = new Category("mock name", "mock icon", "#FFFFFF", "A")
        category_2 = new Category("mock name", "mock icon", "#FFFFFF", "B")

        catDao.create(category_1)
        catDao.create(category_2)

    }

    void cleanup() {
    }

    def "test one insertion"(){
        setup:
        Collection col_1 = new Collection("mock col 1")
        col_1.addCategory(category_1)


        when:
        dao.create(col_1)

        then:
        col_1.getId() != null
        col_1.getCategorySet().size() == 1
    }

    def "test multiple insertion"(){
        setup:
        Collection col_1 = new Collection("mock col 1")
        col_1.addCategory(category_1)
        col_1.addCategory(category_2)
        col_1.addCategory(new Category("mock name", "mock icon", "#FFFFFF", "C"))

        when:
        dao.create(col_1)


        then:
        col_1.getId() != null
        col_1.getCategorySet().size() == 3
        catDao.findAll().size() == 3
    }
}
