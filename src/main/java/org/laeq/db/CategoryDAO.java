package org.laeq.db;

import org.laeq.model.Category;
import org.laeq.model.Collection;

import javax.annotation.Nonnull;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class CategoryDAO extends AbstractDAO implements DAOInterface<Category> {
    public CategoryDAO(@Nonnull DatabaseManager manager) {
        super(manager);
    }

    @Override
    public void insert(Category category) throws DAOException {
        int result = 0;

        String query = "INSERT INTO CATEGORY (NAME, ICON, COLOR, SHORTCUT) VALUES (?, ?, ?, ?);";

        try(Connection connection = getManager().getConnection();
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS))
        {
            statement.setString(1, category.getName());
            statement.setString(2, category.getIcon());
            statement.setString(3, category.getColor());
            statement.setString(4, category.getShortcut());

            result = statement.executeUpdate();

            ResultSet keys = statement.getGeneratedKeys();

            if(keys.next()){
                category.setId(keys.getInt(1));
            }
        } catch (Exception e){
            getLogger().error(e.getMessage());
        }

        if(result != 1)
            throw new DAOException("Error during inserting category: " + category.getName());
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
            category.setColor(datas.getString("COLOR"));
            category.setShortcut(datas.getString("SHORTCUT"));
            category.setCreatedAt(datas.getTimestamp("CREATED_AT"));
            category.setUpdatedAt(datas.getTimestamp("UPDATED_AT"));
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
            throw new DAOException("Error deleting category: " + category.getName());
    }

    //@todo write unit updatePosition
    public Set<Category> findByCollection(Collection collection) {
        String query = "select * from CATEGORY_COLLECTION as CC join CATEGORY as C on CC.CATEGORY_ID = C.ID where CC.COLLECTION_ID = ?;";

        Set<Category> result = new HashSet<>();

        try(Connection connection = getManager().getConnection();
            PreparedStatement statement = connection.prepareStatement(query)){

            statement.setInt(1, collection.getId());

            ResultSet queryResult = statement.executeQuery();
            result = getResult(queryResult);

        } catch (SQLException e){
            getLogger().error(e.getMessage());
        }

        return result;
    }

    public void update(Category category) throws SQLException, DAOException {
        try(Connection connection = getManager().getConnection())
        {
            String query = "UPDATE CATEGORY SET NAME=?, ICON=?, SHORTCUT=?, COLOR=? WHERE ID = ?;";
            PreparedStatement stmt = connection.prepareStatement(query);

            stmt.setString(1, category.getName());
            stmt.setString(2, category.getIcon());
            stmt.setString(3, category.getShortcut());
            stmt.setString(4, category.getColor());
            stmt.setInt(5, category.getId());

            int result = stmt.executeUpdate();

            if(result != 1){
                throw new DAOException(String.format("Error cannot update category: ", category.getName()));
            }
        }
    }
}
