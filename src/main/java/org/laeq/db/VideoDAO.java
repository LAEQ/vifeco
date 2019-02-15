package org.laeq.db;

import org.laeq.model.Video;

import javax.annotation.Nonnull;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class VideoDAO extends AbstractDAO implements DAOInterface<Video>{
    public static String sequence_name = "video_id";

    private CategoryCollectionDAO categoryCollectionDAO;

    public VideoDAO(@Nonnull DatabaseManager manager, String sequenceName) {
        super(manager, sequenceName);

        categoryCollectionDAO = new CategoryCollectionDAO(manager, CategoryCollectionDAO.sequence_name);
    }

    @Override
    public void insert(Video video) throws DAOException {
        int result = 0;
        Integer nextId = getNextValue();

        if(nextId == null){
            throw new DAOException("Cannot generate the next video id from the database.");
        }

        String query = "INSERT INTO VIDEO (ID, PATH, DURATION, CATEGORY_COLLECTION_ID) VALUES (?, ?, ?, ?);";

        try(Connection connection = getManager().getConnection();
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS))
        {
            statement.setInt(1, nextId);
            statement.setString(2, video.getPath());
            statement.setDouble(3, video.getDuration());
            statement.setInt(4, video.getCategoryCollection().getId());

            result = statement.executeUpdate();

            video.setId(nextId);
        } catch (Exception e){
            getLogger().error(e.getMessage());
        }

        if(result != 1)
            throw new DAOException("Error during DAO insert video");
    }

    @Override
    public Set<Video> findAll() {
        String query = "SELECT * FROM VIDEO;";

        Set<Video> result = new HashSet<>();

        try(Connection connection = getManager().getConnection();
            PreparedStatement statement = connection.prepareStatement(query);){

            ResultSet queryResult = statement.executeQuery();
            result = getResult(queryResult);

        } catch (SQLException e){
            getLogger().error(e.getMessage());
        }

        return result;
    }

    private Set<Video> getResult(ResultSet datas) throws SQLException {
        Set<Video> result = new HashSet<>();

        while(datas.next()){
            result.add(generateVideo(datas));
        }

        return result;
    }

    private Video generateVideo(ResultSet datas) throws SQLException {
        Video video = new Video();
        video.setId(datas.getInt("ID"));
        video.setPath(datas.getString("PATH"));
        video.setDuration((datas.getDouble("DURATION")));
        video.setCreatedAt(datas.getTimestamp("CREATED_AT"));
        video.setUpdatedAt(datas.getTimestamp("UPDATED_AT"));
        video.setCategoryCollection(categoryCollectionDAO.findByID(datas.getInt("CATEGORY_COLLECTION_ID")));

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
}
