package org.laeq.db;

import org.laeq.model.Category;
import org.laeq.model.CategoryCollection;

import javax.annotation.Nonnull;
import java.sql.*;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

public class CategoryCollectionDAO extends AbstractDAO implements DAOInterface<CategoryCollection> {
    public static String sequence_name = "category_collection_id";

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

            if(! categoryCollection.getCategorySet().isEmpty()){
                String query2 = "INSERT INTO category_collection_category(CATEGORY_COLLECTION_ID, CATEGORY_ID) VALUES(?, ?)";

                PreparedStatement statement1 = connection.prepareStatement(query2);

                for (Category category: categoryCollection.getCategorySet()) {
                    statement1.setInt(1, nextId);
                    statement1.setInt(2, category.getId());

                    statement1.addBatch();
                }

                statement1.executeBatch();
            }
        } catch (Exception e){
            getLogger().error(e.getMessage());
            throw new DAOException("Error insert CategoryCollection: " + e.getMessage());
        }
    }

    public void init() throws DAOException, SQLException {
        Integer nextId = getNextValue();

        int result = 0;

        if(nextId == null){
            throw new DAOException("Cannot generate the next category_collection id from the database.");
        }

        if(nextId > 1){
            getLogger().info("CategoryCollectionDAO: init() - default category exists");
            return;
        }

        try(Connection connection = getManager().getConnection()){
            String query = "INSERT INTO CATEGORY_COLLECTION (ID, NAME, IS_DEFAULT) VALUES (?, ?, true);";
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            statement.setInt(1, nextId);
            statement.setString(2, "Default category");

            result = statement.executeUpdate();
        }

        if(result != 1){
            throw new DAOException("Cannot save category collection:");
        } else {
            getLogger().info("CategoryCollectionDAO: default category collection created");
        }
    }

    public void setDefault(CategoryCollection categoryCollection) throws SQLException, DAOException {
        try(Connection connection = getManager().getConnection())
        {
            String query2 = "UPDATE CATEGORY_COLLECTION SET IS_ACTIVE = true WHERE ID = ?;";
            PreparedStatement statement2 = connection.prepareStatement(query2);

            statement2.setInt(1, categoryCollection.getId());

            int result2 = statement2.executeUpdate();

            categoryCollection.setIsDefault(true);

            if(result2 != 1){
                throw new DAOException("CATEGORY_COLLECTION: no category collection is active.");
            }
        }
    }

    public CategoryCollection findDefault() throws SQLException {
        String query = "SELECT * FROM CATEGORY_COLLECTION AS CC " +
                "LEFT JOIN CATEGORY_COLLECTION_CATEGORY AS CCC " +
                "ON CC.ID = CCC.CATEGORY_COLLECTION_ID " +
                "LEFT JOIN CATEGORY AS C " +
                "ON CCC.CATEGORY_ID = C.ID " +
                "WHERE CC.IS_DEFAULT = true;";

        CategoryCollection categoryCollection = null;

        try(Connection connection = getManager().getConnection();
            PreparedStatement statement = connection.prepareStatement(query);){

            ResultSet result = statement.executeQuery();
            categoryCollection = new CategoryCollection();
            while(result.next()){
                categoryCollection.setId(result.getInt(1));
                categoryCollection.setName(result.getString(2));
                categoryCollection.setIsDefault(result.getBoolean(3));

                Category category = new Category();
                category.setId(result.getInt(10));
                category.setName(result.getString(11));
                category.setIcon(result.getString(12));
                category.setColor(result.getString(13));
                category.setShortcut(result.getString(14));

                categoryCollection.addCategory(category);
            }

            return categoryCollection;
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


    public void update(CategoryCollection categoryCollection) throws DAOException {

        String query = "UPDATE CATEGORY_COLLECTION SET NAME=? WHERE ID=?";

        int result = 0;

        try(Connection connection = getManager().getConnection();
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS))
        {
            statement.setInt(2, categoryCollection.getId());
            statement.setString(1, categoryCollection.getName());

            if(statement.executeUpdate() != 1)
                throw new DAOException("Cannot update category collection name");


            //Bug hsqldb no support Array type
            StringBuilder builder = new StringBuilder();
            categoryCollection.getCategorySet().forEach(integer -> builder.append("?,"));

            String deleteQuery = String.format("DELETE  FROM CATEGORY_COLLECTION_CATEGORY WHERE CATEGORY_COLLECTION_ID = ? " +
                    "AND CATEGORY_ID NOT IN (%s) ;", builder.deleteCharAt(builder.lastIndexOf(",")).toString());

            PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);

            deleteStatement.setInt(1, categoryCollection.getId());


            ListIterator<Integer> it = categoryCollection.getCategoryIds().listIterator();
            while(it.hasNext()){
                deleteStatement.setInt(it.nextIndex() + 2, it.next());
            }

            deleteStatement.executeUpdate();
            connection.commit();

            List<Category> newCategories = categoryCollection.getNewCategories(findCollectionIdsById(categoryCollection));

            if( ! newCategories.isEmpty()){
                String query2 = "INSERT INTO category_collection_category(CATEGORY_COLLECTION_ID, CATEGORY_ID) VALUES(?, ?)";
                PreparedStatement statement1 = connection.prepareStatement(query2);

                for (Category category: newCategories) {
                    statement1.setInt(1, categoryCollection.getId());
                    statement1.setInt(2, category.getId());

                    statement1.addBatch();
                }

                statement1.executeBatch();
            }
        } catch (Exception e){
            getLogger().error(e.getMessage());
            throw new DAOException("Error for updating:" + categoryCollection + " - " + e.getMessage());
        }
    }

    public Set<Integer> findCollectionIdsById(CategoryCollection categoryCollection) throws SQLException {
      return findCollectionIdsById(categoryCollection.getId());
    }

    public Set<Integer> findCollectionIdsById(int id) throws SQLException {
        String query = "SELECT CATEGORY_ID FROM CATEGORY_COLLECTION_CATEGORY WHERE CATEGORY_COLLECTION_ID = ?";

        Set<Integer> result = new HashSet<>();

        try(Connection con = getManager().getConnection();
            PreparedStatement statement = con.prepareStatement(query)
        ){
            statement.setInt(1, id);

            ResultSet datas = statement.executeQuery();

            while(datas.next()){
                result.add(datas.getInt("CATEGORY_ID"));
            }
        }

        return result;
    }

    @Override
    public Set<CategoryCollection> findAll() {

        Set<CategoryCollection> result = new HashSet<>();

        String query = "SELECT C.ID as CAT_ID, C.NAME AS CAT_NAME, C.ICON, C.SHORTCUT, CC.ID, CC.NAME, CC.IS_DEFAULT as IS_DEFAULT FROM CATEGORY_COLLECTION as CC" +
                " LEFT JOIN CATEGORY_COLLECTION_CATEGORY as CCC ON CC.ID = CCC.CATEGORY_COLLECTION_ID" +
                " JOIN CATEGORY as C ON C.ID = CCC.CATEGORY_ID  ORDER BY CC.ID ASC;";

        try(Connection connection = getManager().getConnection();
            PreparedStatement statement = connection.prepareStatement(query);){


            ResultSet queryResult = statement.executeQuery();

            int id = 0;

            CategoryCollection categoryCollection = null;

            while(queryResult.next()){
                id = queryResult.getInt("ID");

                if(categoryCollection != null && categoryCollection.getId() == id) {
                    Category category = new Category();
                    category.setId(queryResult.getInt("CAT_ID"));
                    category.setName(queryResult.getString("CAT_NAME"));
                    category.setIcon(queryResult.getString("ICON"));
                    category.setShortcut(queryResult.getString("SHORTCUT"));
                    categoryCollection.addCategory(category);
                } else {
                    categoryCollection = new CategoryCollection();
                    categoryCollection.setId(id);
                    categoryCollection.setName(queryResult.getString("NAME"));
                    categoryCollection.setIsDefault(queryResult.getBoolean("IS_DEFAULT"));

                    Category category = new Category();
                    category.setId(queryResult.getInt("CAT_ID"));
                    category.setName(queryResult.getString("CAT_NAME"));
                    category.setIcon(queryResult.getString("ICON"));
                    category.setShortcut(queryResult.getString("SHORTCUT"));
                    categoryCollection.addCategory(category);

                    result.add(categoryCollection);
                }
            }

        } catch (SQLException e) {
            getLogger().error("Error catageryCollection: " + e.getMessage());
        }

        return result;
    }

    public CategoryCollection findByID(int id) throws SQLException {
        String query = "SELECT C.ID as CAT_ID, C.NAME AS CAT_NAME, C.ICON, C.SHORTCUT, CC.ID, CC.NAME, CC.IS_DEFAULT as IS_DEFAULT FROM CATEGORY_COLLECTION as CC" +
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
            categoryCollection.setIsDefault(datas.getBoolean("IS_DEFAULT"));

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
        if(category.getId() == 1){
            throw new DAOException("CategoryCollectionDAO: You cannot delete the default category collection");
        }

        throw new DAOException("To be implemented");
    }
}
