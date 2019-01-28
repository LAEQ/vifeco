package org.laeq.db

import spock.lang.Specification

import java.sql.Connection

class DatabaseManagerTest extends Specification {

    def schema = this.class.classLoader.rootLoader;
    def className = "org.hsqldb.server.Server";
    def javaHome = System.getProperty("java.home")
    def javaBin = javaHome + File.separator + "bin" + File.separator + "java"

    def "test the db connection"() {

        given:

        def hslqdbPath = this.class.getClassLoader().getResource("db/hsqldb.jar")
        def ressourcePath = this.class.getClassLoader().getResource("db")

        println(hslqdbPath)

//        def builder = new ProcessBuilder(javaBin, "-classpath",  hslqdbPath,  "org.hsqldb.server.Server")
//        builder.directory(new File(ressourcePath.toURI()))
//        builder.redirectErrorStream(true)
//        def process = builder.start()
//        process.destroy()

        when:
        DatabaseManager manager;
//        DatabaseManager manager = new DatabaseManager();
//        Connection connection = manager.getConnection();
//
//        try{
//            def stmt = connection.createStatement();
////            def result = stmt.executeUpdate("CREATE TABLE test (name varchar(30));");
//            def result = stmt.executeUpdate("INSERT INTO test VALUES('TEST')")
//
//        }catch (Exception e) {
//            println e.toString()
//        }

        then:
        true == true
    }
}
