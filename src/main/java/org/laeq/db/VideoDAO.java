package org.laeq.db;

import javafx.util.Duration;
import org.laeq.model.Category;
import org.laeq.model.Video;

import javax.annotation.Nonnull;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class VideoDAO extends AbstractDAO implements DAOInterface<Video>{
    public VideoDAO(@Nonnull DatabaseManager manager, String sequenceName) {
        super(manager, sequenceName);
    }

    @Override
    public void insert(Video video) throws DAOException {
        int result = 0;
        Integer nextId = getNextValue();

        if(nextId == null){
            throw new DAOException("Cannot generate the next video id from the database.");
        }

        String query = "INSERT INTO VIDEO (ID, PATH, DURATION) VALUES (?, ?, ?);";

        try(Connection connection = getManager().getConnection();
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS))
        {
            statement.setInt(1, nextId);
            statement.setString(2, video.getPath());
            statement.setDouble(3, video.getDuration().toSeconds());

            result = statement.executeUpdate();

            video.setId(nextId);

            System.out.println(getLogger());
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
            Video video = new Video();
            video.setId(datas.getInt("ID"));
            video.setPath(datas.getString("PATH"));
            video.setDuration(Duration.seconds(datas.getDouble("DURATION")));
            result.add(video);
        }

        return result;
    }


    @Override
    public void delete(Video video) throws DAOException {
        int result = 0;
        String query = "DELETE FROM VIDEO WHERE ID=?";

        System.out.println(findAll().size());

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
