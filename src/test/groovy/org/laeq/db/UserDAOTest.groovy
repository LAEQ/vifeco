package org.laeq.db

import org.laeq.model.User

class UserDAOTest extends AbstractDAOTest {
    UserDAO repository;

    def setup(){
        repository = new UserDAO(manager)
    }

    def "test insertion"() {
        setup:
        User user = new User("Luck", "Skywalker", "luke@maytheforcebewithyou.com")

        when:
        repository.insert(user)

        then:
        user == new User(1, "Luck", "Skywalker", "luke@maytheforcebewithyou.com")
    }

    def "test multiple insertion"() {
        setup:
        User user = new User("Luck", "Skywalker", "luke@maytheforcebewithyou.com")
        User user2 = new User("Luck2", "Skywalker2", "luke@maytheforcebewithyou.com")

        when:
        repository.insert(user)
        repository.insert(user2)

        then:
        user == new User(1, "Luck", "Skywalker", "luke@maytheforcebewithyou.com")
        user2 == new User(2,"Luck2", "Skywalker2", "luke@maytheforcebewithyou.com")

    }

    def "test insertion with an invalid  (no name, email, ...)"(){
        setup:
        def user = new User("", "Skywalker", "luke@maytheforcebewithyou.com")

        when:
        repository.insert(user)

        then:
        notThrown DAOException
    }

    def "test findAll"() {
        setup:
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

    def "test findAll but empty"() {
        when:
        def result = repository.findAll()

        then:
        result.size() == 0
    }

    def "test delete the default "() {
        setup:
        try{
            manager.loadFixtures(this.class.classLoader.getResource("sql/fixtures.sql"))
        } catch (Exception e){
            println e
        }

        User user = new User(1,"Luck", "Skywalker", "luke@maytheforcebewithyou.com")

        when:
        repository.delete(user)

        then:
        thrown DAOException
    }

    def "test delete an existing "() {
        setup:
        try{
            manager.loadFixtures(this.class.classLoader.getResource("sql/test_fixtures.sql"))
        } catch (Exception e){
            println e
        }

        User user = new User(2,"Luck", "Skywalker", "luke@maytheforcebewithyou.com")

        when:
        repository.delete(user)

        then:
        notThrown Exception
    }

    def "test find default"() {
        setup:
        try{
            manager.loadFixtures(this.class.classLoader.getResource("sql/test_fixtures.sql"))
        } catch (Exception e){
            println e
        }

        when:
        User user = repository.findDefault()

        then:
        user == new User(1,"Luck", "Skywalker", "luke@maytheforcebewithyou.com")
    }

    def "test delete an unknown "() {
        setup:
        try{
            manager.loadFixtures(this.class.classLoader.getResource("sql/test_fixtures.sql"))
        } catch (Exception e){
            println e
        }

        User user = new User(-1,"Luck", "Skywalker", "luke@maytheforcebewithyou.com")

        when:
        repository.delete(user)

        then:
        thrown DAOException
    }
}
