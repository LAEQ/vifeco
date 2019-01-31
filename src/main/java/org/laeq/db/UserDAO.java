package org.laeq.db;

import org.laeq.model.User;
import javax.annotation.Nonnull;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class UserDAO extends AbstractDAO implements DAOInterface<User> {
    public UserDAO(@Nonnull DatabaseManager manager, String sequenceName) {
        super(manager, sequenceName);
    }

    @Override
    public void insert(User user) throws DAOException {
        int result = 0;
        Integer nextId = getNextValue();

        if(nextId == null){
            throw new DAOException("Cannot generate the next user id from the database.");
        }

        String query = "INSERT INTO user (ID, FIRST_NAME, LAST_NAME, EMAIL) VALUES (?, ?, ?, ?);";

        try(Connection connection = getManager().getConnection();
            PreparedStatement statement = connection.prepareStatement(query,Statement.RETURN_GENERATED_KEYS))
        {
            statement.setInt(1, nextId);
            statement.setString(2, user.getFirstName());
            statement.setString(3, user.getLastName());
            statement.setString(4, user.getEmail());

            result = statement.executeUpdate();

            user.setId(nextId);
        } catch (Exception e){
            getLogger().error(e.getMessage());
        }

        if(result != 1)
            throw new DAOException("Error during DAO insert user");

    }

    @Override
    public Set<User> findAll() {
        String query = "SELECT * from USER;";

        Set<User> result = new HashSet<>();

        try(Connection connection = getManager().getConnection();
        PreparedStatement statement = connection.prepareStatement(query);){

            ResultSet queryResult = statement.executeQuery();
            result = getResult(queryResult);

        } catch (SQLException e){
            getLogger().error(e.getMessage());
        }

        return result;
    }

    @Override
    public void delete(User user) throws DAOException {
        int result = 0;
        String query = "DELETE FROM USER WHERE ID=?";

        try(Connection connection = getManager().getConnection();
            PreparedStatement statement = connection.prepareStatement(query);)
        {
            statement.setInt(1, user.getId());

            result = statement.executeUpdate();

        } catch (Exception e){
            getLogger().error(e.getMessage());
        }

        if(result != 1)
            throw new DAOException("Error deleting a user");
    }


    private Set<User> getResult(@Nonnull ResultSet datas) throws SQLException {
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
