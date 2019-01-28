package org.laeq.db

import spock.lang.Specification

import java.sql.Connection

class DatabaseManagerTest extends Specification {
    def "test the db connection"() {

        given: "db connection settings"

        when:
        DatabaseManager manager = new DatabaseManager();
        Connection result = manager.getConnection();
        println result
        then:
        true == true
    }
}
