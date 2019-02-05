package org.laeq.db;

import griffon.core.artifact.GriffonService;
import griffon.metadata.ArtifactProviderFor;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonService;
import org.laeq.model.User;
import org.laeq.model.Video;
import org.laeq.model.VideoUser;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

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

    public void create(Video video) throws SQLException, DAOException {
        VideoDAO dao = new VideoDAO(manager, "video_id");

        dao.insert(video);
    }

    public List<VideoUser> getVideoUser(){
        return new VideoUserDAO(manager, "not_applicable").findAll();
    }

    public Set<Video> findAll() {
        VideoDAO dao = new VideoDAO(manager, "video_id");

        return dao.findAll();
    }
}