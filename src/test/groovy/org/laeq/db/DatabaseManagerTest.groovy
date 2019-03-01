package org.laeq.db

import ch.vorburger.mariadb4j.DB
import ch.vorburger.mariadb4j.DBConfigurationBuilder
import spock.lang.Specification
import java.sql.Connection
import java.sql.Statement

class DatabaseManagerTest extends Specification {

    private String dbName = "vifecodb"
    private DB db
    private DatabaseManager manager

    def setup(){
        DBConfigurationBuilder config = DBConfigurationBuilder.newBuilder();
        config.setPort(0)
        db = DB.newEmbeddedDB(config.build())

        db.start()
        db.createDB(dbName)

        DatabaseConfigInterface configBean = new DatabaseConfigBean(config.getURL(dbName), "root", "")
        manager = new DatabaseManager(configBean)
    }

    def cleanup(){
        db.stop()
    }

    def "test the db connection and create a table"() {
        setup:
        def result = 1
        def resultSet

        when:

        try{
            Connection con = manager.getConnection()
            Statement stmt = con.createStatement()

            result = stmt.executeUpdate("CREATE TABLE hello(world VARCHAR(100))")
            result = stmt.executeUpdate("INSERT INTO hello VALUES('Hello, world');")

            Statement preparedStatement = con.createStatement()
            resultSet = preparedStatement.executeQuery("Select * from hello")

        } catch (Exception e){
            println e
        }

        then:
        result == 1
        resultSet.next() == true
    }

    def "test create tables"() {
        setup:
        def createSQL = this.class.getClassLoader().getResource("sql/create_tables.sql")

        when:
        def result = manager.loadFixtures(createSQL)

        then:
        noExceptionThrown()
    }

    def "test load fixtures"() {
        setup:
        def createSQL = this.class.getClassLoader().getResource("sql/create_tables.sql")
        def fixturesSQL = this.class.getClassLoader().getResource("sql/fixtures.sql")


        when:
        manager.loadFixtures(createSQL)
        manager.loadFixtures(fixturesSQL)

        then:
        noExceptionThrown()
    }
}
