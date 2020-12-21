package org.laeq.db;

import org.laeq.model.User;

import javax.annotation.Nonnull;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class UserDAO extends AbstractDAO implements DAOInterface<User> {
    public UserDAO(@Nonnull DatabaseManager manager) {
        super(manager);
    }

    @Override
    public void insert(User user) throws DAOException {
        int result = 0;

        String query = "INSERT INTO USER (FIRST_NAME, LAST_NAME, EMAIL) VALUES (?, ?, ?);";

        try(Connection connection = getManager().getConnection();
            PreparedStatement statement = connection.prepareStatement(query))
        {
            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setString(3, user.getEmail());

            result = statement.executeUpdate();

            ResultSet keys = statement.getGeneratedKeys();

            if(keys.next()){
                user.setId(keys.getInt(1));
            }
        } catch (Exception e){
            getLogger().error(e.getMessage());
        }

        if(result != 1)
            throw new DAOException("Error during DAO insert USER");

    }

    public User findDefault() throws DAOException, SQLException {
        String query = "SELECT * from USER WHERE IS_DEFAULT = true;";

        try(Connection connection = getManager().getConnection();
            PreparedStatement statement = connection.prepareStatement(query))
        {
            ResultSet result = statement.executeQuery();

            if(result.next()){
               return generateUser(result);
            }

            throw new DAOException("UserDAO: no active user.");
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

            throw new DAOException("UserDAO: no default user.");
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
        String query = "DELETE FROM USER WHERE ID=? AND IS_DEFAULT = false;";

        try(Connection connection = getManager().getConnection();
            PreparedStatement statement = connection.prepareStatement(query);)
        {
            statement.setInt(1, user.getId());

            result = statement.executeUpdate();

        } catch (Exception e){
            getLogger().error(e.getMessage());
        }

        if(result != 1)
            throw new DAOException(String.format("Error deleting user: %s", user));
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
//        user.setId(datas.getInt("ID"));
//        user.setFirstName(datas.getString("FIRST_NAME"));
//        user.setLastName(datas.getString("LAST_NAME"));
//        user.setEmail(datas.getString("EMAIL"));
//        user.setIsDefault(datas.getBoolean("IS_DEFAULT"));
//        user.setCreatedAt(datas.getTimestamp("CREATED_AT"));
//        user.setUpdatedAt(datas.getTimestamp("UPDATED_AT"));

        return user;
    }

    public void update(User user) throws SQLException, DAOException {
        try(Connection connection = getManager().getConnection())
        {
            String query = "UPDATE USER SET FIRST_NAME=?, LAST_NAME=?, EMAIL=? WHERE ID = ?;";
            PreparedStatement statement2 = connection.prepareStatement(query);

            statement2.setString(1, user.getFirstName());
            statement2.setString(2, user.getLastName());
            statement2.setString(3, user.getEmail());
            statement2.setInt(4, user.getId());

            int result2 = statement2.executeUpdate();

            if(result2 != 1){
                throw new DAOException("UserDAO: no Useris active.");
            }
        }
    }
}
