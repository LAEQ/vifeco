package org.laeq.model.dao

import org.laeq.model.Category
import org.laeq.model.Collection
import spock.lang.Specification

class CollectionDAOTest extends Specification {
    CollectionDAO dao;
    CategoryDAO catDao;
    Category category_1, category_2
    HibernateUtil util

    void setup() {
        util = new HibernateUtil('hibernate.cfg.xml')

        catDao = new CategoryDAO(util)
        dao = new CollectionDAO(util)

        category_1 = EntityGenerator.createCategory("A")
        category_2 = EntityGenerator.createCategory("B")

        catDao.create(category_1)
        catDao.create(category_2)
    }

    void cleanup() {
        util.shutdown()
    }

    def "test one insertion"(){
        setup:
        Collection collection = new Collection("mock col 1")
        collection.addCategory(category_1)
        collection.addCategory(category_2)

        when:
        dao.create(collection)

        then:
        collection.getId() != null
        collection.getCategories().size() == 2
    }

    def "test invalid name"(){
        setup:
        Collection collection = new Collection("")

        when:
        dao.create(collection)

        then:
        thrown Exception
    }

    def "test multiple collection "(){
        setup:
        Collection col_1 = new Collection("mock col 1")
        col_1.addCategory(category_1)
        Collection col_2 = new Collection("mock col 1")
        col_2.addCategory(category_1)

        when:
        dao.create(col_1)
        dao.create(col_2)

        then:
        dao.findAll().size() == 2
    }

    def "test delete collection "(){
        setup:
        Collection col_1 = new Collection("mock col 1")
        col_1.addCategory(category_1)
        dao.create(col_1)

        when:
        dao.delete(col_1)

        then:
        notThrown Exception
    }

    def "fetch eager category"(){
        setup:
        Collection col_1 = new Collection("mock col 1")
        col_1.addCategory(category_1)
        dao.create(col_1)
        def id = col_1.getId()

        when:
        def col = dao.findOneById(id)

        then:
        col.getId() == id
        col.getCategories().size() == 1
    }

    def "delete one collection to test CascadeType.PERSIST, CascadeType.MERGE"(){
        setup:
        Collection col_1 = new Collection("mock col 1")
        col_1.addCategory(category_1)
        Collection col_2 = new Collection("mock col 2")
        col_2.addCategory(category_1)
        col_2.addCategory(category_2)
        dao.create(col_1)
        dao.create(col_2)

        when:
        dao.delete(col_1)
        def col = dao.findOneById(col_2.getId())

        then:
        col != null
        col.getCategories().size() == 2
    }

    def "cannot delete a category coz CascadeType"(){
        setup:
        Collection col_1 = new Collection("mock col 1")
        col_1.addCategory(category_1)
        Collection col_2 = new Collection("mock col 2")
        col_2.addCategory(category_1)
        col_2.addCategory(category_2)
        dao.create(col_1)
        dao.create(col_2)

        when:
        def result = catDao.delete(category_1)

        then:
        result == false
    }

    def "FindDefault"() {
        setup:
        Collection collection = new Collection("I am default")
        collection.setDefault(Boolean.TRUE)
        dao.create(collection)

        for (i in 5) {
            dao.create(new Collection('not default'))
        }

        when:
        Collection result = dao.findDefault()

        then:
        result.getName() == "I am default"
    }
}
