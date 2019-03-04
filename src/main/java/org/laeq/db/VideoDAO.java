package org.laeq.db;

import org.laeq.model.Collection;
import org.laeq.model.User;
import org.laeq.model.Video;

import javax.annotation.Nonnull;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class VideoDAO extends AbstractDAO implements DAOInterface<Video>{
    private CollectionDAO collectionDAO;
    private UserDAO userDAO;

    public VideoDAO(@Nonnull DatabaseManager manager, CollectionDAO collectionDAO, UserDAO userDAO) {
        super(manager);
        this.collectionDAO = collectionDAO;
        this.userDAO = userDAO;
    }

    @Override
    public void insert(Video video) throws DAOException {
        int result = 0;

        String query = "INSERT INTO VIDEO (PATH, DURATION, USER_ID, COLLECTION_ID) VALUES (?, ?, ?, ?);";

        try(Connection connection = getManager().getConnection();
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS))
        {
            statement.setString(1, video.getPath());
            statement.setDouble(2, video.getDuration());
            statement.setInt(3, video.getUser().getId());
            statement.setInt(4, video.getCollection().getId());

            result = statement.executeUpdate();

            ResultSet keys = statement.getGeneratedKeys();

            if(keys.next()){
                video.setId(keys.getInt(1));
            }
        } catch (Exception e){
            String message = String.format("VideoDAO: insert - %s - $s", video, e.getMessage());
            getLogger().error(message);
        }

        if(result != 1)
            throw new DAOException("Error during DAO insert video");
    }

    @Override
    public Set<Video> findAll() {
        String query = "select V.id as video_id,  V.path as path,  V.duration as duration, V.user_id AS user_id, \n" +
                        "V.CREATED_AT AS CREATED_AT, \n" +
                        "U.first_name AS first_name, " +
                        "U.last_name AS last_name, CC.ID AS cc_id, CC.name AS cc_name, " +
                        "count(P.video_id) as total_point, CC.Name as c_name from VIDEO AS V \n" +
                        "left join USER AS U on V.user_id = U.id \n" +
                        "left join POINT as P ON P.video_id = V.id  \n" +
                        "left join COLLECTION AS CC on CC.id = V.collection_id \n" +
                        "GROUP BY P.video_id, V.user_id, V.id, V.created_at, CC.id, U.first_name, U.last_name, CC.ID, CC.name;";

        Set<Video> result = new HashSet<>();

        try(Connection connection = getManager().getConnection();
            PreparedStatement statement = connection.prepareStatement(query);){

            ResultSet queryResult = statement.executeQuery();
            result = getResult(queryResult);

        } catch (SQLException | DAOException e){
            getLogger().error(e.getMessage());
        }

        return result;
    }



    private Set<Video> getResult(ResultSet datas) throws SQLException, DAOException {
        Set<Video> result = new HashSet<>();

        while(datas.next()){
            result.add(generateVideo(datas));
        }

        return result;
    }

    private Video generateVideo(ResultSet datas) throws SQLException, DAOException {
        Video video = new Video();

        video.setId(datas.getInt("VIDEO_ID"));
        video.setPath(datas.getString("PATH"));
        video.setDuration((datas.getDouble("DURATION")));
        video.setCreatedAt(datas.getTimestamp("CREATED_AT"));
        video.setTotal(datas.getInt("TOTAL_POINT"));

        User user = ResultHelper.generateUser(datas);
        video.setUser(user);

        Collection collection = ResultHelper.generateCategoryCollection(datas);

        video.setCollection(collection);

        return video;
    }


    @Override
    public void delete(Video video) throws DAOException {
        int result = 0;
        String query = "DELETE FROM VIDEO WHERE ID=?";

        try(Connection connection = getManager().getConnection();
            PreparedStatement statement = connection.prepareStatement(query);)
        {
            statement.setInt(1, video.getId());
            result = statement.executeUpdate();
        } catch (Exception e){
            getLogger().error(e.getMessage());
        }

        if(result !=1)
            throw new DAOException("Error deleting a video");
    }

    public void editVideo(Video video) {

    }

    public void update(Video video) throws SQLException, DAOException {
        try(Connection connection = getManager().getConnection())
        {
            String query = "UPDATE VIDEO SET USER_ID=?, COLLECTION_ID=? WHERE ID = ?;";
            PreparedStatement stmt = connection.prepareStatement(query);

            stmt.setInt(1, video.getUser().getId());
            stmt.setInt(2, video.getCollection().getId());
            stmt.setInt(3, video.getId());

            int result = stmt.executeUpdate();

            if(result != 1){
                throw new DAOException("UserDAO: cannot update.");
            }
        }

    }

    public void updateUser(Video video, User user) throws SQLException, DAOException {
        try(Connection connection = getManager().getConnection())
        {
            String query = "UPDATE VIDEO SET USER_ID=? WHERE ID = ?;";
            PreparedStatement stmt = connection.prepareStatement(query);

            stmt.setInt(1, user.getId());
            stmt.setInt(2, video.getId());

            int result = stmt.executeUpdate();

            if(result != 1){
                throw new DAOException("VideoDAO: cannot update. USER_ID");
            }
        }
    }

    public void updateCollection(Video video, Collection collection) throws SQLException, DAOException {
        try(Connection connection = getManager().getConnection())
        {
            String query = "UPDATE VIDEO SET COLLECTION_ID=? WHERE ID = ?;";
            PreparedStatement stmt = connection.prepareStatement(query);

            stmt.setInt(1, collection.getId());
            stmt.setInt(2, video.getId());

            int result = stmt.executeUpdate();

            if(result != 1){
                throw new DAOException("VideoDAO: cannot update COLLECTION_ID");
            }
        }
    }
}
