package org.laeq.model.dao

import org.laeq.model.Drawing
import spock.lang.Specification

class DrawingDAOTest extends Specification {
    DrawingDAO dao
    HibernateUtil util
    void setup() {
        util = new HibernateUtil('hibernate.cfg.xml')
        dao = new DrawingDAO(util)
    }

    void cleanup() {
        util.shutdown()
    }

    def "test insertion"() {
        setup:
        Drawing draw = EntityGenerator.createDraw()

        when:
        dao.create(draw)

        then:
        draw.getId() != null
    }

    def "test delete"() {
        setup:
        Drawing draw = EntityGenerator.createDraw()
        dao.create(draw)

        when:
        dao.delete(draw)

        then:
        notThrown Exception
    }

    def "test findAll"(){
        setup:
        Drawing draw = EntityGenerator.createDraw()
        Drawing draw2 = EntityGenerator.createDraw()
        dao.create(draw)
        dao.create(draw2)

        when:
        List<Drawing> list = dao.findAll()

        then:
        list.size() == 2
    }
}
