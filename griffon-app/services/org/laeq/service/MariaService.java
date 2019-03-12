package org.laeq.service;

import ch.vorburger.exec.ManagedProcessException;
import ch.vorburger.mariadb4j.DB;
import ch.vorburger.mariadb4j.DBConfigurationBuilder;
import griffon.core.artifact.GriffonService;
import griffon.metadata.ArtifactProviderFor;
import javafx.util.Duration;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonService;
import org.laeq.db.*;
import org.laeq.model.Collection;
import org.laeq.model.User;
import org.laeq.model.Video;
import org.laeq.settings.Settings;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;

@javax.inject.Singleton
@ArtifactProviderFor(GriffonService.class)
public class MariaService extends AbstractGriffonService {
    private DB db;
    private DBConfigurationBuilder config;
    private String dbName = "vifecodb";
    private DatabaseManager manager;

    public MariaService() throws ManagedProcessException {
        String dbPathStr = Settings.dbPath;
        Path dbPath = Paths.get(dbPathStr);

        if(! Files.exists(dbPath)){
            try {
                Files.createDirectories(dbPath);
            } catch (IOException e) {
                getLog().error("MariaService: cannot create " + dbPathStr);
                throw new ManagedProcessException("Cannot instantiate the database.");
            }
        }

        config = DBConfigurationBuilder.newBuilder();
        config.setPort(3306);
        config.setDataDir(dbPathStr);
        db = DB.newEmbeddedDB(config.build());

        DatabaseConfigBean configBean = new DatabaseConfigBean(config.getURL(dbName), "root", "");
        manager = new DatabaseManager(configBean);
    }

    public void start() throws ManagedProcessException {
        db.start();
        db.createDB(dbName);

        boolean result = false;
        result = manager.loadFixtures("sql/create_tables.sql");

        if(result){
            try{
                UserDAO userDAO = new UserDAO(manager);
                User defaultUser = userDAO.findDefault();

            } catch (DAOException | SQLException e) {
                manager.loadFixtures("sql/fixtures.sql");
            }
        }
    }

    public void stop() throws ManagedProcessException {
        db.stop();
    }

    public UserDAO getUserDAO(){
        return new UserDAO(manager);
    }

    public VideoDAO getVideoDAO(){
        return new VideoDAO(manager, new CollectionDAO(manager), new UserDAO(manager));
    }

    public CategoryDAO getCategoryDAO(){
        return new CategoryDAO(manager);
    }

    public CollectionDAO getCollectionDAO(){
        return new CollectionDAO(manager);
    }

    public PointDAO getPointDAO(){
        return new PointDAO(manager);
    }

    public Video createVideo(File file, Duration duration) throws IOException, SQLException, DAOException {
        UserDAO userDAO = getUserDAO();
        CollectionDAO collectionDAO = getCollectionDAO();

        User defaultUser = userDAO.findDefault();
        Collection defaultCollection = collectionDAO.findDefault();

        Video video = new Video(file.getPath(), duration, defaultUser, defaultCollection);
        getVideoDAO().insert(video);

        return video;
    }

    public DatabaseManager getManager() {
        return manager;
    }

    public void getTableStatus() throws SQLException, DAOException {
        //@todo
        User defaultUser = new UserDAO(manager).findDefault();
    }
}