package org.laeq.service;

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
    private String dbName = "vifecodb";
    private DatabaseManager manager;

    public MariaService()  {
        String dbPathStr = Settings.dbPath;
        Path dbPath = Paths.get(dbPathStr);

        if(! Files.exists(dbPath)){
            try {
                Files.createDirectories(dbPath);
            } catch (IOException e) {
                getLog().error("MariaService: cannot create " + dbPathStr);

            }
        }

        String importPathStr = Settings.imporPath;
        Path importPath = Paths.get(importPathStr);

        if(! Files.exists(importPath)) {
            try {
                Files.createDirectories(importPath);
            } catch (IOException e) {
                getLog().error("Import path creation: cannot create " + dbPathStr);
            }
        }

        String videoPathStr = Settings.videoPath;
        Path videoPath = Paths.get(videoPathStr);

        if(! Files.exists(videoPath)) {
            try {
                Files.createDirectories(videoPath);
            } catch (IOException e) {
                getLog().error("Import path creation: cannot create " + videoPathStr);
            }
        }

        String statPathStr = Settings.statisticPath;
        Path statPah = Paths.get(statPathStr);

        if(! Files.exists(statPah)) {
            try {
                Files.createDirectories(statPah);
            } catch (IOException e) {
                getLog().error("Import path creation: cannot create " + statPathStr);
            }
        }

        String dbUrl = String.format("jdbc:sqlite:%s/%s.db", dbPathStr, dbName);
        System.out.println(dbUrl);
//        String mariaDbUrl = "jdbc:mariadb://localhost:3306";
        DatabaseConfigBean configBean = new DatabaseConfigBean( dbUrl, "root", "");
        manager = new DatabaseManager(configBean);

    }


    public void start()  {
//        try{
//            db.start();
//            db.createDB(dbName);
//        } catch (Exception e){
//            getLog().error("Cannot start mysql process. A mysqld process is already on the same port. You must stop this process." );
//
//        }

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

    public void stop() {
//        db.stop();
    }

    public UserDAO getUserDAO(){
        return new UserDAO(manager);
    }

    public VideoDAO getVideoDAO(){
        return new VideoDAO(manager, new CollectionDAO(manager));
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

        Video video = new Video(file.getAbsolutePath(), duration, defaultUser, defaultCollection);
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