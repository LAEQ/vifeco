package org.laeq.db;

import org.laeq.model.Category;
import org.laeq.model.Collection;

import javax.annotation.Nonnull;
import java.sql.*;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

public class CollectionDAO extends AbstractDAO implements DAOInterface<Collection> {
    public CollectionDAO(@Nonnull DatabaseManager manager) {
        super(manager);
    }

    @Override
    public void insert(Collection collection) throws DAOException {

        try(Connection connection = getManager().getConnection())
        {
            String query = "INSERT INTO COLLECTION (NAME) VALUES (?);";
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, collection.getName());

            if(statement.executeUpdate() != 1)
                throw new DAOException("Cannot save category collection:");

            ResultSet keys = statement.getGeneratedKeys();

            if(keys.next()){
                collection.setId(keys.getInt(1));
            }

            if(! collection.getCategorySet().isEmpty()){
                String query2 = "INSERT INTO CATEGORY_COLLECTION (COLLECTION_ID, CATEGORY_ID) VALUES(?, ?)";

                PreparedStatement statement1 = connection.prepareStatement(query2);

                for (Category category: collection.getCategorySet()) {
                    statement1.setInt(1, collection.getId());
                    statement1.setInt(2, category.getId());

                    statement1.addBatch();
                }

                statement1.executeBatch();
            }
        } catch (Exception e){
            getLogger().error(e.getMessage());
            throw new DAOException("Error insert Collection: " + e.getMessage());
        }
    }

    public Collection findDefault() throws SQLException {
        String query = "SELECT * FROM COLLECTION AS CC " +
                "LEFT JOIN CATEGORY_COLLECTION AS CCC " +
                "ON CC.ID = CCC.COLLECTION_ID " +
                "LEFT JOIN CATEGORY AS C " +
                "ON CCC.CATEGORY_ID = C.ID " +
                "WHERE CC.IS_DEFAULT = true;";

        Collection collection = null;

        try(Connection connection = getManager().getConnection();
            PreparedStatement statement = connection.prepareStatement(query);){

            ResultSet result = statement.executeQuery();
            collection = new Collection();
            while(result.next()){
                collection.setId(result.getInt(1));
                collection.setName(result.getString(2));
                collection.setIsDefault(result.getBoolean(3));

                Category category = new Category();
                category.setId(result.getInt(10));
                category.setName(result.getString(11));
                category.setIcon(result.getString(12));
                category.setColor(result.getString(13));
                category.setShortcut(result.getString(14));

                if(category.getName() != null){
                    collection.addCategory(category);
                }
            }

            return collection;
        }
    }

    private void debug(){
        String sql = "SELECT * FROM CATEGORY_COLLECTION";

        try(Connection connection = getManager().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql))
        {
            ResultSet test = statement.executeQuery();

            while(test.next()){
                System.out.println(String.format("CC_ID: %d : C_ID %d",
                        test.getInt("COLLECTION_ID"),
                        test.getInt("CATEGORY_ID")));
            }

        } catch (Exception e){
            getLogger().error(e.getMessage());
        }

    }


    public void update(Collection collection) throws DAOException {

        String query = "UPDATE COLLECTION SET NAME=? WHERE ID=?";

        int result = 0;

        try(Connection connection = getManager().getConnection();
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS))
        {
            statement.setInt(2, collection.getId());
            statement.setString(1, collection.getName());

            if(statement.executeUpdate() != 1)
                throw new DAOException("Cannot update category collection name");


            //Bug hsqldb no support Array type
            StringBuilder builder = new StringBuilder();
            collection.getCategorySet().forEach(integer -> builder.append("?,"));

            String deleteQuery = String.format("DELETE  FROM CATEGORY_COLLECTION WHERE COLLECTION_ID = ? " +
                    "AND CATEGORY_ID NOT IN (%s) ;", builder.deleteCharAt(builder.lastIndexOf(",")).toString());

            PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);

            deleteStatement.setInt(1, collection.getId());


            ListIterator<Integer> it = collection.getCategoryIds().listIterator();
            while(it.hasNext()){
                deleteStatement.setInt(it.nextIndex() + 2, it.next());
            }

            deleteStatement.executeUpdate();


            List<Category> newCategories = collection.getNewCategories(findCollectionIdsById(collection));

            if( ! newCategories.isEmpty()){
                String query2 = "INSERT INTO CATEGORY_COLLECTION(COLLECTION_ID, CATEGORY_ID) VALUES(?, ?)";
                PreparedStatement statement1 = connection.prepareStatement(query2);

                for (Category category: newCategories) {
                    statement1.setInt(1, collection.getId());
                    statement1.setInt(2, category.getId());

                    statement1.addBatch();
                }

                statement1.executeBatch();
            }
        } catch (Exception e){
            getLogger().error(e.getMessage());
            throw new DAOException("Error for updating:" + collection + " - " + e.getMessage());
        }
    }

    public Set<Integer> findCollectionIdsById(Collection collection) throws SQLException {
      return findCollectionIdsById(collection.getId());
    }

    public Set<Integer> findCollectionIdsById(int id) throws SQLException {
        String query = "SELECT CATEGORY_ID FROM CATEGORY_COLLECTION WHERE COLLECTION_ID = ?";

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
    public Set<Collection> findAll() {

        Set<Collection> result = new HashSet<>();

        String query = "SELECT C.ID as CAT_ID, C.NAME AS CAT_NAME, C.ICON, C.SHORTCUT, CC.ID, CC.NAME, CC.IS_DEFAULT as IS_DEFAULT FROM COLLECTION as CC" +
                " LEFT JOIN CATEGORY_COLLECTION as CCC ON CC.ID = CCC.COLLECTION_ID" +
                " LEFT JOIN CATEGORY as C ON C.ID = CCC.CATEGORY_ID  ORDER BY CC.ID ASC;";

        try(Connection connection = getManager().getConnection();
            PreparedStatement statement = connection.prepareStatement(query);){


            ResultSet queryResult = statement.executeQuery();

            int id = 0;

            Collection collection = null;

            while(queryResult.next()){
                id = queryResult.getInt("ID");

                if(collection != null && collection.getId() == id) {
                    Category category = new Category();
                    category.setId(queryResult.getInt("CAT_ID"));
                    category.setName(queryResult.getString("CAT_NAME"));
                    category.setIcon(queryResult.getString("ICON"));
                    category.setShortcut(queryResult.getString("SHORTCUT"));
                    collection.addCategory(category);
                } else {
                    collection = new Collection();
                    collection.setId(id);
                    collection.setName(queryResult.getString("NAME"));
                    collection.setIsDefault(queryResult.getBoolean("IS_DEFAULT"));

                    Category category = new Category();
                    category.setId(queryResult.getInt("CAT_ID"));
                    category.setName(queryResult.getString("CAT_NAME"));
                    category.setIcon(queryResult.getString("ICON"));
                    category.setShortcut(queryResult.getString("SHORTCUT"));

                    if(category.getName() != null){
                        collection.addCategory(category);
                    }

                    result.add(collection);
                }
            }

        } catch (SQLException e) {
            getLogger().error("Error catageryCollection: " + e.getMessage());
        }

        return result;
    }

    public Collection findByID(int id) throws SQLException {
        String query = "SELECT C.ID as CAT_ID, C.NAME AS CAT_NAME, C.ICON, C.SHORTCUT, CC.ID, CC.NAME, CC.IS_DEFAULT as IS_DEFAULT FROM COLLECTION as CC" +
                " LEFT JOIN CATEGORY_COLLECTION as CCC ON CC.ID = CCC.COLLECTION_ID " +
                "JOIN CATEGORY as C ON C.ID = CCC.CATEGORY_ID WHERE CC.ID = ?";

        try(Connection connection = getManager().getConnection();
            PreparedStatement statement = connection.prepareStatement(query);){

            statement.setInt(1, id);

            ResultSet queryResult = statement.executeQuery();

            return getCategoryResult(queryResult);
        }
    }

    private Collection getCategoryResult(ResultSet datas) throws SQLException {
        Collection collection = new Collection();

        while(datas.next()){
            collection.setId(datas.getInt("ID"));
            collection.setName(datas.getString("NAME"));
            collection.setIsDefault(datas.getBoolean("IS_DEFAULT"));

            Category category = new Category();
            category.setId(datas.getInt("CAT_ID"));
            category.setName(datas.getString("CAT_NAME"));
            category.setIcon(datas.getString("ICON"));
            category.setShortcut(datas.getString("SHORTCUT"));

            collection.addCategory(category);
        }

        return collection;
    }

    @Override
    public void delete(Collection collection) throws DAOException {
        if(collection.getId() == 1){
            throw new DAOException("CollectionDAO: You cannot delete the default category collection");
        }

        int result = 0;
        String query = "DELETE FROM COLLECTION WHERE ID=? AND IS_DEFAULT = false";


        try(Connection connection = getManager().getConnection();
            PreparedStatement statement = connection.prepareStatement(query);)
        {
            statement.setInt(1, collection.getId());

            result = statement.executeUpdate();

        } catch (Exception e){
            getLogger().error(e.getMessage());
        }

        if(result != 1)
            throw new DAOException(String.format("Cannot delete %s", collection));

    }

    public void save(Collection selectedCollection) throws DAOException {
        if(selectedCollection.getId() == 0){
            insert(selectedCollection);
        } else {
            update(selectedCollection);
        }
    }
}
