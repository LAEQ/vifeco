package org.laeq.db;

import griffon.core.artifact.GriffonService;
import griffon.metadata.ArtifactProviderFor;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonService;
import org.laeq.model.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

@javax.inject.Singleton
@ArtifactProviderFor(GriffonService.class)
public class DatabaseService extends AbstractGriffonService {
    private final DatabaseManager manager;

    private UserDAO userDAO;
    private CategoryCollectionDAO categoryCollectionDAO;
    private PointDAO pointDAO;
    private CategoryDAO categoryDAO;
    private VideoDAO videoDAO;


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

    public void init() {
        userDAO = new UserDAO(manager, UserDAO.sequence_name);
        categoryCollectionDAO = new CategoryCollectionDAO(manager, CategoryCollectionDAO.sequence_name);
        pointDAO = new PointDAO(manager, PointDAO.sequence_name);
        categoryDAO = new CategoryDAO(manager, CategoryDAO.sequence_name);
        videoDAO = new VideoDAO(manager, VideoDAO.sequence_name);

        try{
            URL tableQuery = getClass().getClassLoader().getResource("sql/create_tables.sql");
            manager.loadFixtures(tableQuery);

            URL sequenceQuery = getClass().getClassLoader().getResource("sql/create_sequences.sql");
            manager.loadFixtures(sequenceQuery);

            getLog().info("DatabaseService: tables and sequences created");

        } catch (Exception e) {
            getLog().error("DatabaseService init: cannot load create_table/create_sequence.");
        }

        try{
            userDAO.init();
            getLog().info("DatabaseService: default org.laeq.user created");
        } catch (Exception e){
            getLog().error("DatabaseService: cannot create default org.laeq.user");
        }

        try{
            categoryCollectionDAO.init();
            getLog().info("DatabaseService: default category collection created");
        } catch (Exception e){
            getLog().error("DatabaseService: cannot create default category collection");
        }
    }

    public void create(Video video) throws SQLException, DAOException {
        VideoDAO dao = new VideoDAO(manager, "video_id");

        dao.insert(video);
    }

    public List<VideoUser> getVideoUserList(){
        return new VideoUserDAO(manager, "not_applicable").findAll();
    }

    public Set<Video> findAll(VideoUser videoUser) {
        VideoDAO dao = new VideoDAO(manager, "video_id");

        return dao.findAll();
    }


    public SortedSet<Point> getVideoPoint(VideoUser videoUser){
        PointDAO pointDAO = new PointDAO(manager, PointDAO.sequence_name);

        return pointDAO.findByVideoAndUser(videoUser.getVideo(), videoUser.getUser());
    }

    public CategoryCollection getCategoryCollection(int id) throws SQLException {
        return categoryCollectionDAO.findByID(id);
    }

    public void set(VideoUser videoUser) throws SQLException {
        videoUser.setPoints(pointDAO.findByVideoAndUser(videoUser.getVideo(), videoUser.getUser()));

        int categoryCollectionId = videoUser.getVideo().getCategoryCollection().getId();

        videoUser.getVideo().setCategoryCollection(categoryCollectionDAO.findByID(categoryCollectionId));
    }

    public Set<User> getUserList() {
        return userDAO.findAll();
    }

    public void setUserActive(User user) throws SQLException, DAOException {
        userDAO.setActive(user);
    }

    public void save(Point point) throws DAOException, SQLException {
        User defaultUser = userDAO.findActive();
        point.setUser(defaultUser);
        pointDAO.insert(point);
    }


    public void save(User user) throws DAOException {
        userDAO.insert(user);
    }

    public void save(Category category) throws DAOException {
        categoryDAO.insert(category);
    }

    public VideoUser createVideoUser(File file) throws IOException, SQLException, DAOException {
        User defaultUser = userDAO.findActive();
        CategoryCollection defaultCategoryCollection = categoryCollectionDAO.findDefault();

        Media media = new Media(file.getCanonicalFile().toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);

        Video video = new Video(file.getPath(), mediaPlayer.getTotalDuration(), defaultUser, defaultCategoryCollection);
        videoDAO.insert(video);

        return new VideoUser(video, defaultUser);
    }

    public Set<User> findUsers() {
        return userDAO.findAll();
    }

    public void delete(User user) throws DAOException {
        userDAO.delete(user);
    }

    public Set<Category> findCategories() {
        return categoryDAO.findAll();
    }

    public void save(CategoryCollection entity) throws DAOException {
        categoryCollectionDAO.insert(entity);
    }

    public UserDAO getUserDAO() {
        return new UserDAO(manager, UserDAO.sequence_name);
    }

    public CategoryDAO getCategoryDAO() {
        return new CategoryDAO(manager, CategoryDAO.sequence_name);
    }

    public CategoryCollectionDAO getCategoryCollectionDAO() {
        return new CategoryCollectionDAO(manager, CategoryCollectionDAO.sequence_name);
    }
}