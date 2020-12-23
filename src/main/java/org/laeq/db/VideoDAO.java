package org.laeq.db;

import org.laeq.model.Collection;
import org.laeq.model.User;
import org.laeq.model.Video;

import javax.annotation.Nonnull;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class VideoDAO extends AbstractDAO implements DAOInterface<Video>{
    private CollectionDAO collectionDAO;
    private String insertQuery = "INSERT INTO VIDEO (PATH, DURATION, USER_ID, COLLECTION_ID) VALUES (?, ?, ?, ?);";

    public VideoDAO(@Nonnull DatabaseManager manager, CollectionDAO collectionDAO) {
        super(manager);
        this.collectionDAO = collectionDAO;
    }

    @Override
    public void insert(Video video) throws DAOException {

    }

    @Override
    public List<Video> findAll() {
        List<Video> result = new ArrayList<>();



        return result;
    }



    private List<Video> getResult(ResultSet datas) throws SQLException, DAOException {
        List<Video> result = new ArrayList<>();



        return result;
    }

    private Video generateVideo(ResultSet datas) throws SQLException, DAOException {
        Video video = new Video();



        return video;
    }


    @Override
    public void delete(Video video) throws DAOException {


    }

    public void updateDuration(Video video) throws SQLException, DAOException {


    }

    public void update(Video video) throws SQLException, DAOException {


    }

    public void updateUser(Video video, User user) throws SQLException, DAOException {

    }

    public void updateCollection(Video video, Collection collection) throws SQLException, DAOException {

    }
}
