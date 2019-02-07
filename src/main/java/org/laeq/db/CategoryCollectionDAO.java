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

        System.out.println(nextId);

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

            categoryCollection.getCategorySet().forEach(category -> {
                String query2 = "INSERT INTO category_collection_category(CATEGORY_COLLECTION_ID, CATEGORY_ID) VALUES(?, ?)";
                try {
                    PreparedStatement stmt2 = connection.prepareStatement(query2);



                    stmt2.setInt(1, nextId);
                    stmt2.setInt(2, category.getId());
//
                    stmt2.executeUpdate();
                } catch (SQLException e) {
                    getLogger().error("Cannot save category collection");
                    e.printStackTrace();
                }
            });



        } catch (Exception e){
            getLogger().error(e.getMessage());
        }


        categoryCollection.getCategorySet().forEach(category -> {
            String query2 = "INSERT INTO CATEGORY_COLLECTION_CATEGORY (CATEGORY_COLLECTION_ID, CATEGORY_ID) VALUES (?, ?);";

            try(Connection connection = getManager().getConnection();
                PreparedStatement statement = connection.prepareStatement(query2, Statement.RETURN_GENERATED_KEYS))
            {
                statement.setInt(1, nextId);
                statement.setInt(2, category.getId());

                statement.executeUpdate();
            } catch (Exception e){
                getLogger().error(e.getMessage());
            }
        });
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
