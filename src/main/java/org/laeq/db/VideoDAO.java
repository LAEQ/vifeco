package org.laeq.db;

import org.laeq.model.CategoryCollection;
import org.laeq.model.User;
import org.laeq.model.Video;

import javax.annotation.Nonnull;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class VideoDAO extends AbstractDAO implements DAOInterface<Video>{
    public static String sequence_name = "video_id";

    private CategoryCollectionDAO categoryCollectionDAO;
    private UserDAO userDAO;

    public VideoDAO(@Nonnull DatabaseManager manager, String sequenceName) {
        super(manager, sequenceName);

        categoryCollectionDAO = new CategoryCollectionDAO(manager, CategoryCollectionDAO.sequence_name);
        userDAO = new UserDAO(manager, UserDAO.sequence_name);
    }

    @Override
    public void insert(Video video) throws DAOException {
        int result = 0;
        Integer nextId = getNextValue();

        if(nextId == null){
            throw new DAOException("Cannot generate the next video id from the database.");
        }

        String query = "INSERT INTO VIDEO (ID, PATH, DURATION, USER_ID, CATEGORY_COLLECTION_ID) VALUES (?, ?, ?, ?, ?);";

        try(Connection connection = getManager().getConnection();
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS))
        {
            statement.setInt(1, nextId);
            statement.setString(2, video.getPath());
            statement.setDouble(3, video.getDuration());
            statement.setInt(4, video.getUser().getId());
            statement.setInt(5, video.getCategoryCollection().getId());

            result = statement.executeUpdate();

            video.setId(nextId);
        } catch (Exception e){
            String message = String.format("VideoDAO: insert - %s - $s", video, e.getMessage());
            getLogger().error(message);
        }

        if(result != 1)
            throw new DAOException("Error during DAO insert video");
    }

    @Override
    public Set<Video> findAll() {
        String query = "select v.id as video_id,  V.path as path,  V.duration as duration, V.user_id AS user_id, \n" +
                        "V.CREATED_AT AS CREATED_AT, \n" +
                        "U.first_name AS first_name, " +
                        "U.last_name AS last_name, CC.ID AS cc_id, CC.name AS cc_name, " +
                        "count(P.video_id) as total_point, CC.Name as c_name from video AS V \n" +
                        "left join user AS U on V.user_id = U.id \n" +
                        "left join Point as P ON P.video_id = V.id  \n" +
                        "left join Category_Collection AS CC on CC.id = V.category_collection_id \n" +
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

        CategoryCollection categoryCollection = ResultHelper.generateCategoryCollection(datas);

        video.setCategoryCollection(categoryCollection);

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
}
