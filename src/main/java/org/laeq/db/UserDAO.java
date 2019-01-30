package org.laeq.db;

import org.codehaus.griffon.runtime.core.addon.AbstractGriffonAddon;
import org.laeq.model.User;

import javax.annotation.Nonnull;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class UserDAO extends AbstractGriffonAddon implements DAOInterface<User> {
    @Nonnull
    private final DatabaseManager config;

    public UserDAO(@Nonnull DatabaseManager config) {
        this.config = config;
    }

    @Override
    public boolean insert(User user) {
        int result = 0;
        String query = "INSERT INTO user (ID, FIRST_NAME, LAST_NAME, EMAIL) VALUES (NEXT VALUE FOR user_id, ?, ?, ?);";

        try(Connection connection = config.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);)
        {
            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setString(3, user.getEmail());

            result = statement.executeUpdate();

            ResultSet keys = statement.getGeneratedKeys();

            if(keys.next()){
                user.setId(keys.getInt(1));
            }

            return true;

        } catch (SQLException e){
            System.out.println(e.getMessage());
            getLog().error(e.getMessage());
            return false;
        }
    }

    @Override
    public Set<User> findAll() {
        String query = "SELECT * from user;";

        Set<User> result = new HashSet<>();

        try(Connection connection = config.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);){

            ResultSet queryResult = statement.executeQuery();
            result = getResult(queryResult);

        } catch (SQLException e){
            System.out.println(e);
            getLog().error(e.getMessage());
        }

        return result;
    }

    @Override
    public boolean delete(User user) {
        int result = 0;
        String query = "DELETE FROM user  WHERE ID=?";

        try(Connection connection = config.getConnection();
            PreparedStatement statement = connection.prepareStatement(query);)
        {
            statement.setInt(1, user.getId());

            result = statement.executeUpdate();

            ResultSet keys = statement.getGeneratedKeys();

            if(keys.next()){
                user.setId(keys.getInt(1));
            }

            return result == 1;

        } catch (SQLException e){
            System.out.println(e.getMessage());
            getLog().error(e.getMessage());
            return false;
        }
    }


    private Set<User> getResult(ResultSet datas) throws SQLException {
        Set<User> result = new HashSet<>();

        while(datas.next()){
            User user = new User();
            user.setId(datas.getInt("ID"));
            user.setFirstName(datas.getString("FIRST_NAME"));
            user.setLastName(datas.getString("LAST_NAME"));
            user.setEmail(datas.getString("EMAIL"));
            result.add(user);
        }

        return result;
    }
}
