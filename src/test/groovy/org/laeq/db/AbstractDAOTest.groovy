package org.laeq.db

import spock.lang.Shared
import spock.lang.Specification

import java.sql.Connection
import java.sql.Statement

class AbstractDAOTest extends Specification {
    @Shared def process
    @Shared def DatabaseManager manager

    def setupSpec() {
        def builder = createBuilder()
        process = builder.start()
    }

    def cleanupSpec(){
        process.destroy()
    }

    def setup(){
        def sqlArray = ["sql/drop_tables.sql", "sql/create_tables.sql", "sql/create_sequences.sql"] as String[]

        manager = new DatabaseManager(new DatabaseConfigBean("jdbc:hsqldb:mem:.", "SA", ""))

        try{
            sqlArray.each {
                Connection con = manager.getConnection()
                Statement stmt = con.createStatement()
                stmt.executeUpdate(this.class.classLoader.getResource(it).text)
            }
        } catch (Exception e){
            println e
        }
    }

    ProcessBuilder createBuilder(){
        def javaHome = System.getProperty("java.home")
        def javaBin = javaHome + File.separator + "bin" + File.separator + "java"
        def hslqdbPath = this.class.getClassLoader().getResource("db/lib/hsqldb.jar")

        def builder = new ProcessBuilder(javaBin, "-classpath",  hslqdbPath.toExternalForm(),  "org.hsqldb.server.Server", "--database.0",  "file:hsqldb/demodb",  "--dbname.0", " testdb")
        builder.redirectErrorStream(true)

        builder
    }
}
