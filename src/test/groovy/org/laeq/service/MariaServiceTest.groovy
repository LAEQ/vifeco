package org.laeq.service

import spock.lang.Specification

class MariaServiceTest extends Specification {

//    def "test embeded db"() {
//        setup:
//        DBConfigurationBuilder config = DBConfigurationBuilder.newBuilder()
//        config.setPort(0)
//        DB db = DB.newEmbeddedDB(config.build())
//        db.start();
//
//        String dbName = "mariaDB4jTest"
//        if (!dbName.equals("test")) {
//            db.createDB(dbName)
//        }
//
//        Connection conn = null;
//
//        when:
//        try
//        {
//            conn = DriverManager.getConnection(config.getURL(dbName), "root", "")
//            String query = "CREATE TABLE hello(world VARCHAR(100))"
//            Statement stmt = conn.createStatement()
//
//            int result = stmt.executeUpdate(query)
//
//            println result
//
//            conn.close()
//
//        } catch(Exception e){
//            println e
//        }
//
//        then:
//        true == true
//    }
}