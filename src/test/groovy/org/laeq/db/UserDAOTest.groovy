package org.laeq.db

import org.laeq.model.User
import spock.lang.Shared
import spock.lang.Specification

import java.sql.Connection
import java.sql.Statement

class UserDAOTest extends Specification {
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
        def sqlArray = ["sql/create_tables.sql", "sql/create_sequences.sql"] as String[]

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

    def "test User insertion"() {
        setup:
        User user = new User("Luck", "Skywalker", "luke@maytheforcebewithyou.com")
        UserDAO repository = new UserDAO(manager);

        when:
        boolean  result = repository.insert(user)

        then:
        result == true
        user == new User(0, "Luck", "Skywalker", "luke@maytheforcebewithyou.com")
    }

    def "test findAll"() {
        setup:
        UserDAO repository = new UserDAO(manager);
        User user1 = new User("Luck", "Skywalker", "luke@maytheforcebewithyou.com")
        User user2 = new User("Darth", "Vader", "darth@iamyourfatcher.com")

        repository.insert(user1)
        repository.insert(user2)

        when:
        def result = repository.findAll()

        then:
        result.size() == 2
        result.contains(new User(1,"Luck", "Skywalker", "luke@maytheforcebewithyou.com")) == true
        result.contains(new User(2,"Darth", "Vader", "darth@iamyourfatcher.com")) == true
    }

    def "test delete User"() {
        setup:
        try{
            manager.loadFixtures(this.class.classLoader.getResource("sql/fixtures.sql").toURI().getPath())
        } catch (Exception e){
            println e
        }

        UserDAO repository = new UserDAO(manager);
        User user = new User(1,"Luck", "Skywalker", "luke@maytheforcebewithyou.com")

        when:
        def result = repository.delete(user)

        then:
        result == true
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
