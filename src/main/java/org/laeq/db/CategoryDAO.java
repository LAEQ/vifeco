package org.laeq.db;

import org.codehaus.griffon.runtime.core.addon.AbstractGriffonAddon;
import org.laeq.model.Category;
import org.laeq.model.User;

import javax.annotation.Nonnull;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class CategoryDAO extends AbstractDAO implements DAOInterface<Category> {
    public CategoryDAO(@Nonnull DatabaseManager manager, String sequenceName) {
        super(manager, sequenceName);
    }

    @Override
    public void insert(Category category) throws DAOException {
        int result = 0;
        Integer nextId = getNextValue();

        if(nextId == null){
            throw new DAOException("Cannot generate the next user id from the database.");
        }

        String query = "INSERT INTO CATEGORY (ID, NAME, ICON, SHORTCUT) VALUES (?, ?, ?, ?);";

        try(Connection connection = getManager().getConnection();
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS))
        {
            statement.setInt(1, nextId);
            statement.setString(2, category.getName());
            statement.setString(3, category.getIcon());
            statement.setString(4, category.getShortcut());

            result = statement.executeUpdate();

            category.setId(nextId);
        } catch (Exception e){
            getLogger().error(e.getMessage());
        }

        if(result != 1)
            throw new DAOException("Error during DAO insert category");
    }

    @Override
    public Set<Category> findAll() {
        String query = "SELECT * FROM CATEGORY;";

        Set<Category> result = new HashSet<>();

        try(Connection connection = getManager().getConnection();
            PreparedStatement statement = connection.prepareStatement(query);){

            ResultSet queryResult = statement.executeQuery();
            result = getResult(queryResult);

        } catch (SQLException e){
            getLogger().error(e.getMessage());
        }

        return result;
    }

    private Set<Category> getResult(ResultSet datas) throws SQLException {
        Set<Category> result = new HashSet<>();

        while(datas.next()){
            Category category = new Category();
            category.setId(datas.getInt("ID"));
            category.setName(datas.getString("NAME"));
            category.setIcon(datas.getString("ICON"));
            category.setShortcut(datas.getString("SHORTCUT"));
            result.add(category);
        }

        return result;
    }

    @Override
    public void delete(Category category) throws DAOException {
        int result = 0;
        String query = "DELETE FROM CATEGORY WHERE ID=?";

        try(Connection connection = getManager().getConnection();
            PreparedStatement statement = connection.prepareStatement(query);)
        {
            statement.setInt(1, category.getId());

            result = statement.executeUpdate();
        } catch (Exception e){
            getLogger().error(e.getMessage());
        }

        if(result !=1)
            throw new DAOException("Error deleting a category");
    }

}
