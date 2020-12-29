package org.laeq.db


import spock.lang.Specification

import java.nio.file.Files
import java.nio.file.Paths

class AbstractDAOTest extends Specification {
    def dbName;
    def DatabaseManager manager


    def setup(){
        def randomId = UUID.randomUUID().toString()
        dbName = String.format("%s/%s-%s", "/tmp", "vifecodb-test", randomId)
        def dbUrl = String.format("jdbc:sqlite:%s", dbName)
        DatabaseConfigInterface configBean = new DatabaseConfigBean(dbUrl, "", "")
        manager = new DatabaseManager(configBean)
        def sqlArray = ["sql/create_tables.sql"] as String[]

        try{
            sqlArray.each {
              manager.loadFixtures(it)
            }
        } catch (Exception e){
            println e
        }
    }

    def cleanup(){
        Files.deleteIfExists(Paths.get(dbName))
    }
}
