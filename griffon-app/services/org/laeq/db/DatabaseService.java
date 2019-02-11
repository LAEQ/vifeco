package org.laeq.db;

import griffon.core.artifact.GriffonService;
import griffon.metadata.ArtifactProviderFor;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonService;
import org.laeq.model.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

@javax.inject.Singleton
@ArtifactProviderFor(GriffonService.class)
public class DatabaseService extends AbstractGriffonService {
    private final DatabaseManager manager;
    private UserDAO userDAO;
    private CategoryCollectionDAO categoryCollectionDAO;

    public DatabaseService() {
        DatabaseConfigBean configBean = new DatabaseConfigBean("jdbc:hsqldb:hsql://localhost/vifecodb", "SA", "");

        manager = new DatabaseManager(configBean);
    }

    public DAOInterface factory(String entity) throws Exception {
        if (entity.equals("user")) {
            return new UserDAO(manager, UserDAO.sequence_name);
        } else if (entity.equals("category")) {
            return new CategoryDAO(manager, CategoryDAO.sequence_name);
        } else if (entity.equals("video")) {
            return new VideoDAO(manager, VideoDAO.sequence_name);
        } else if (entity.equals("point")) {
            return new PointDAO(manager, PointDAO.sequence_name);
        } else if (entity.equals("category_collection")) {
            return new CategoryCollectionDAO(manager, CategoryCollection.sequence_name);
        }

        throw new Exception("Database service: cannot instantiate a DAO");
    }

    public DatabaseManager getManager() {
        return manager;
    }

    public void init() throws Exception {
        URL tableQuery = getClass().getClassLoader().getResource("sql/create_tables.sql");
        manager.loadFixtures(tableQuery);

        URL sequenceQuery = getClass().getClassLoader().getResource("sql/create_sequences.sql");
        manager.loadFixtures(sequenceQuery);

        userDAO = new UserDAO(manager, UserDAO.sequence_name);
        userDAO.init();

        categoryCollectionDAO = new CategoryCollectionDAO(manager, CategoryCollectionDAO.sequence_name);
        categoryCollectionDAO.init();
    }

    public void create(Video video) throws SQLException, DAOException {
        VideoDAO dao = new VideoDAO(manager, "video_id");

        dao.insert(video);
    }

    public List<VideoUser> getVideoUser(){
        return new VideoUserDAO(manager, "not_applicable").findAll();
    }

    public Set<Video> findAll(VideoUser videoUser) {
        VideoDAO dao = new VideoDAO(manager, "video_id");

        return dao.findAll();
    }

    public SortedSet<Point> findByVideoUser(VideoUser videoUser){

        PointDAO dao = new PointDAO(manager, "point_id");

        return dao.findByVideoAndUser(videoUser.getVideo(), videoUser.getUser());
    }
}