package org.laeq.db;

import griffon.core.artifact.GriffonService;
import griffon.metadata.ArtifactProviderFor;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonService;
import org.laeq.model.User;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;

@javax.inject.Singleton
@ArtifactProviderFor(GriffonService.class)
public class DatabaseService extends AbstractGriffonService {
    private final DatabaseManager manager;

    public DatabaseService() {
        DatabaseConfigBean configBean = new DatabaseConfigBean("jdbc:hsqldb:hsql://localhost/vifecodb", "SA", "");

        manager = new DatabaseManager(configBean);
    }

    public DatabaseManager getManager() {
        return manager;
    }

    public void init() throws SQLException, IOException, URISyntaxException {
        URL tableQuery = getClass().getClassLoader().getResource("sql/create_tables.sql");
        manager.loadFixtures(tableQuery);

        URL sequenceQuery = getClass().getClassLoader().getResource("sql/create_sequences.sql");
        manager.loadFixtures(sequenceQuery);
    }
}