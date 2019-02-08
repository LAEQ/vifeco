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
            PreparedStatement statement = connection.prepareStatement(query))
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

    public void setActive(User user) throws SQLException, DAOException {
        try(Connection connection = getManager().getConnection())
        {
            String query = "UPDATE USER SET IS_ACTIVE = false;";
            PreparedStatement statement = connection.prepareStatement(query);

            int result = statement.executeUpdate();

            connection.commit();

            String query2 = "UPDATE USER SET IS_ACTIVE = true WHERE ID = ?;";
            PreparedStatement statement2 = connection.prepareStatement(query2);

            statement2.setInt(1, user.getId());

            int result2 = statement2.executeUpdate();

            user.setIsActive(true);

            if(result2 != 1){
                throw new DAOException("UserDAO: no user is active.");
            }
        }
    }

    public User findActive() throws DAOException, SQLException {
        String query = "SELECT * from USER WHERE IS_ACTIVE = true;";

        try(Connection connection = getManager().getConnection();
            PreparedStatement statement = connection.prepareStatement(query))
        {
            ResultSet result = statement.executeQuery();

            if(result.next()){
               return generateUser(result);
            }

            throw new DAOException("UserDAO: no user is active.");
        }
    }

    public User findById(int id) throws DAOException, SQLException {
        String query = "SELECT * from USER WHERE ID = ?;";

        try(Connection connection = getManager().getConnection();
            PreparedStatement statement = connection.prepareStatement(query))
        {
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();

            if(result.next()){
                return generateUser(result);
            }

            throw new DAOException("UserDAO: no user is active.");
        }
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
            result.add(generateUser(datas));
        }

        return result;
    }

    private User generateUser(ResultSet datas) throws SQLException {
        User user = new User();
        user.setId(datas.getInt("ID"));
        user.setFirstName(datas.getString("FIRST_NAME"));
        user.setLastName(datas.getString("LAST_NAME"));
        user.setEmail(datas.getString("EMAIL"));
        user.setIsActive(datas.getBoolean("IS_ACTIVE"));
        user.setCreatedAt(datas.getTimestamp("CREATED_AT"));
        user.setUpdatedAt(datas.getTimestamp("UPDATED_AT"));

        return user;
    }
}
