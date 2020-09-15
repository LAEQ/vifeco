package org.laeq.db

import org.laeq.settings.Settings
import spock.lang.Shared
import spock.lang.Specification

import java.nio.file.Files
import java.nio.file.Paths
import java.sql.Connection
import java.sql.Statement

class DatabaseManagerTest extends Specification {
    @Shared def dbName = String.format("%s/%s-%s", "/tmp", "vifecodb-test", UUID.randomUUID().toString())
    @Shared def dbUrl = String.format("jdbc:sqlite:%s", dbName);
    private DatabaseManager manager

    def setup(){
        Files.deleteIfExists(Paths.get(dbName))
        DatabaseConfigInterface configBean = new DatabaseConfigBean(dbUrl, "root", "")
        manager = new DatabaseManager(configBean)
    }

    def cleanup(){
        manager.getConnection().close()
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
        def createSQL = "sql/create_tables.sql"

        when:
        def result = manager.loadFixtures(createSQL)

        then:
        result == true
    }

    def "test load fixtures"() {
        setup:
        def sqlFiles = ["sql/create_tables.sql", "sql/fixtures.sql"]


        when:
        sqlFiles.each {
            manager.loadFixtures(it)
        }

        then:
        noExceptionThrown()
    }
}
