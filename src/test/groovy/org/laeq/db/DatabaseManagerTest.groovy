package org.laeq.db

import spock.lang.Specification

import java.sql.Connection
import java.sql.SQLException
import java.sql.Statement

class DatabaseManagerTest extends Specification {
    def "test the db connection and creation of dummy table"() {
        setup: "Create a database process"
        def builder = createProcess()
        def process = builder.start()

        when:
        DatabaseConfigInterface config = new DatabaseConfigBean("jdbc:hsqldb:mem:.", "SA", "")
        DatabaseManager manager = new DatabaseManager(config)

        def result = 1

        try{
            Connection con = manager.getConnection()
            Statement stmt = con.createStatement()

            result = stmt.executeUpdate(
                    '''CREATE TABLE tutorials_tbl (
                    id INT NOT NULL, title VARCHAR(50) NOT NULL,
                    author VARCHAR(20) NOT NULL, submission_date DATE,
                    PRIMARY KEY (id));
                    '''
            )

        } catch (Exception e){
            println e
        }

        then:
        result == 0

        cleanup: "destroy the database process"
        process.destroy()
    }

    def "test loadFixtures"() {
        setup: "Create a database process"
        def builder = createProcess()
        def process = builder.start()

        and:
        def createSQL = this.class.getClassLoader().getResource("sql/create_tables.sql")

        println createSQL

        when:
        DatabaseManager manager = new DatabaseManager(new DatabaseConfigBean("jdbc:hsqldb:mem:.", "SA", ""))

        def result = manager.loadFixtures(createSQL)

        then:
        result == true

        cleanup: "destroy the database process"
        process.destroy()
    }

    def "test the create table sql"() {
        setup: "Create a database process"
        def builder = createProcess()
        def process = builder.start()

        and:
        def createSQL = this.class.getClassLoader().getResource("sql/create_tables.sql").text

        when:
        DatabaseManager manager = new DatabaseManager(new DatabaseConfigBean("jdbc:hsqldb:mem:.", "SA", ""))

        def result = 1

        try{
            Connection con = manager.getConnection()
            Statement stmt = con.createStatement()
            result = stmt.executeUpdate(createSQL)
        } catch (Exception e){
            println e
        }


        then:
        result == 0

        cleanup: "destroy the database process"
        process.destroy()
    }


    def "test table status"() {
        setup: "Create a database process"
        def builder = createProcess()
        def process = builder.start()

        and:
        def sqlString = this.class.getClassLoader().getResource("sql/create_tables.sql").text

        when:
        DatabaseManager manager = new DatabaseManager(new DatabaseConfigBean("jdbc:hsqldb:mem:.", "SA", ""))

        def result = 1

        try{
            Connection con = manager.getConnection()
            Statement stmt = con.createStatement()
            result = stmt.executeUpdate(sqlString)
        } catch (Exception e){
            println e
        }

        manager.getTableStatus()


        then:
        noExceptionThrown()

        cleanup: "destroy the database process"
        process.destroy()

    }

    def "create sequences"() {
        setup: "Create a database process"
        def builder = createProcess()
        def process = builder.start()

        and:
        def sqlString = this.class.getClassLoader().getResource("sql/create_sequences.sql").text

        when:
        DatabaseManager manager = new DatabaseManager(new DatabaseConfigBean("jdbc:hsqldb:mem:.", "SA", ""))

        def result = 1

        try{
            Connection con = manager.getConnection()
            Statement stmt = con.createStatement()
            result = stmt.executeUpdate(sqlString)
        } catch (Exception e){
            println e
        }


        then:
        result == 0

        cleanup: "destroy the database process"
        process.destroy()
    }

    def ProcessBuilder createProcess(){
        def javaHome = System.getProperty("java.home")
        def javaBin = javaHome + File.separator + "bin" + File.separator + "java"
        def hslqdbPath = this.class.getClassLoader().getResource("db/lib/hsqldb.jar")

        def builder = new ProcessBuilder(javaBin, "-classpath",  hslqdbPath.toExternalForm(),  "org.hsqldb.server.Server", "--database.0",  "file:hsqldb/testdb",  "--dbname.0", " testdb")
        builder.redirectErrorStream(true)

        return builder
    }

}
