package org.laeq.db

import spock.lang.Specification

import java.sql.Connection
import java.sql.DriverManager
import java.sql.Statement

class DatabaseManagerTest extends Specification {

    def schema = this.class.classLoader.rootLoader;
    def className = "org.hsqldb.server.Server";
    def javaHome = System.getProperty("java.home")
    def javaBin = javaHome + File.separator + "bin" + File.separator + "java"

    def "test the db connection and creation of dummy table"() {
        setup: "Create a database process"
        def hslqdbPath = this.class.getClassLoader().getResource("db/lib/hsqldb.jar")
        def builder = new ProcessBuilder(javaBin, "-classpath",  hslqdbPath.toExternalForm(),  "org.hsqldb.server.Server", "--database.0",  "file:hsqldb/demodb",  "--dbname.0", " testdb")
        builder.redirectErrorStream(true)
        def process = builder.start()

        given:
//        def createSQL = this.class.getClassLoader().getResource("sql/create_tables.sql").text

        when:
        DatabaseManager manager = new DatabaseManager("jdbc:hsqldb:mem:.", "SA", "")

        Connection con
        Exception exception
        int result = 1

        try{
            con = manager.getConnection()
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
            exception = e;
        }


        then:
        result == 0
        con != null
        exception == null

        cleanup: "destroy the database process"
        process.destroy()
    }

    def "test the create table sql"() {
        setup: "Create a database process"
        println "ICI"
        def hslqdbPath = this.class.getClassLoader().getResource("db/lib/hsqldb.jar")
        def builder = new ProcessBuilder(javaBin, "-classpath",  hslqdbPath.toExternalForm(),  "org.hsqldb.server.Server", "--database.0",  "file:hsqldb/demodb",  "--dbname.0", " testdb")
        builder.redirectErrorStream(true)
        def process = builder.start()

        and:
        def createSQL = this.class.getClassLoader().getResource("sql/create_tables.sql").text

        when:
        DatabaseManager manager = new DatabaseManager("jdbc:hsqldb:mem:.", "SA", "")

        Connection con
        Exception exception
        int result = 1

        try{
            con = manager.getConnection()
            Statement stmt = con.createStatement()

            result = stmt.executeUpdate(createSQL)

        } catch (Exception e){
            exception = e;
            println e
        }


        then:
        result == 0
        con != null
        exception == null

        cleanup: "destroy the database process"
        process.destroy()
    }
}
