package org.laeq.service;

import ch.vorburger.exec.ManagedProcessException;
import ch.vorburger.mariadb4j.DB;
import ch.vorburger.mariadb4j.DBConfigurationBuilder;
import griffon.core.artifact.GriffonService;
import griffon.metadata.ArtifactProviderFor;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonService;
import org.laeq.db.DatabaseConfigBean;
import org.laeq.db.DatabaseManager;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
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
    DatabaseManager manager;

    public MariaService() throws ManagedProcessException {

        String dbPathStr = System.getProperty("user.home") + "/vifecoDB";

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

        URL tableQuery = getClass().getClassLoader().getResource("sql/create_tables.sql");
        try {
            manager.loadFixtures(tableQuery);
        } catch (IOException | SQLException | URISyntaxException e) {
            getLog().error(e.getMessage());
        }
    }

    public void stop() throws ManagedProcessException {
        db.stop();
    }
}