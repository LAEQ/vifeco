package org.laeq.db

import ch.vorburger.mariadb4j.DB
import ch.vorburger.mariadb4j.DBConfigurationBuilder
import spock.lang.Shared
import spock.lang.Specification

class AbstractDAOTest extends Specification {
    @Shared def dbName = "vifecodb"
    @Shared def db
    @Shared DatabaseManager manager

    def setupSpec() {
        DBConfigurationBuilder config = DBConfigurationBuilder.newBuilder();
        config.setPort(0)
        db = DB.newEmbeddedDB(config.build())

        db.start()
        db.createDB(dbName)

        DatabaseConfigInterface configBean = new DatabaseConfigBean(config.getURL(dbName), "root", "")
        manager = new DatabaseManager(configBean)
    }

    def cleanupSpec(){
        db.stop()
    }

    def setup(){
        def sqlArray = ["sql/drop_tables.sql", "sql/create_tables.sql"] as String[]

        try{
            sqlArray.each {
              manager.loadFixtures(getClass().classLoader.getResource(it))
            }
        } catch (Exception e){
            println e
        }
    }
}
