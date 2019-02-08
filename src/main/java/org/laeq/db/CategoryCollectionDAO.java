package org.laeq.db;

import org.laeq.model.Category;
import org.laeq.model.CategoryCollection;

import javax.annotation.Nonnull;
import java.sql.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class CategoryCollectionDAO extends AbstractDAO implements DAOInterface<CategoryCollection> {
    public CategoryCollectionDAO(@Nonnull DatabaseManager manager, String sequenceName) {
        super(manager, sequenceName);
    }

    @Override
    public void insert(CategoryCollection categoryCollection) throws DAOException {
        Integer nextId = getNextValue();

        if(nextId == null){
            throw new DAOException("Cannot generate the next category_collection id from the database.");
        }

        try(Connection connection = getManager().getConnection())
        {
            String query = "INSERT INTO CATEGORY_COLLECTION (ID, NAME) VALUES (?, ?);";
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            statement.setInt(1, nextId);
            statement.setString(2, categoryCollection.getName());

            if(statement.executeUpdate() != 1)
                throw new DAOException("Cannot save category collection:");

            categoryCollection.setId(nextId);

            String query2 = "INSERT INTO category_collection_category(CATEGORY_COLLECTION_ID, CATEGORY_ID) VALUES(?, ?)";

            PreparedStatement statement1 = connection.prepareStatement(query2);

            for (Category category: categoryCollection.getCategorySet()) {
                statement1.setInt(1, nextId);
                statement1.setInt(2, category.getId());

                statement1.addBatch();
            }

            statement1.executeBatch();

        } catch (Exception e){
            getLogger().error(e.getMessage());
            throw new DAOException("Error insert CategoryCollection: " + e.getMessage());
        }
    }

    private void debug(){
        String sql = "SELECT * FROM CATEGORY_COLLECTION_CATEGORY";

        try(Connection connection = getManager().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql))
        {
            ResultSet test = statement.executeQuery();

            while(test.next()){
                System.out.println(String.format("CC_ID: %d : C_ID %d",
                        test.getInt("CATEGORY_COLLECTION_ID"),
                        test.getInt("CATEGORY_ID")));
            }

        } catch (Exception e){
            getLogger().error(e.getMessage());
        }

    }



    public void update(CategoryCollection categoryCollection){

        String query = "UPDATE CATEGORY_COLLECTION SET NAME=? WHERE ID=?";

        int result = 0;

        try(Connection connection = getManager().getConnection();
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS))
        {
            statement.setInt(2, categoryCollection.getId());
            statement.setString(1, categoryCollection.getName());

            result = statement.executeUpdate();

        } catch (Exception e){
            getLogger().error(e.getMessage());
        }

        if(result == 1){

        }

    }

    @Override
    public Set<CategoryCollection> findAll() {
        return null;
    }

    public CategoryCollection findByID(int id) throws Exception {
        String query = "SELECT C.ID as CAT_ID, C.NAME AS CAT_NAME, C.ICON, C.SHORTCUT, CC.ID, CC.NAME FROM CATEGORY_COLLECTION as CC" +
                " LEFT JOIN CATEGORY_COLLECTION_CATEGORY as CCC ON CC.ID = CCC.CATEGORY_COLLECTION_ID " +
                "JOIN CATEGORY as C ON C.ID = CCC.CATEGORY_ID WHERE CC.ID = ?";

        try(Connection connection = getManager().getConnection();
            PreparedStatement statement = connection.prepareStatement(query);){

            statement.setInt(1, id);

            ResultSet queryResult = statement.executeQuery();

            return getCategoryResult(queryResult);

        }
    }

    private CategoryCollection getCategoryResult(ResultSet datas) throws SQLException {
        CategoryCollection categoryCollection = new CategoryCollection();

        while(datas.next()){
            categoryCollection.setId(datas.getInt("ID"));
            categoryCollection.setName(datas.getString("NAME"));

            Category category = new Category();
            category.setId(datas.getInt("CAT_ID"));
            category.setName(datas.getString("CAT_NAME"));
            category.setIcon(datas.getString("ICON"));
            category.setShortcut(datas.getString("SHORTCUT"));

            categoryCollection.addCategory(category);
        }

        return categoryCollection;
    }


    @Override
    public void delete(CategoryCollection category) throws DAOException {
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
