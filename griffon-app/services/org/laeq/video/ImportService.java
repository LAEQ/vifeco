package org.laeq.video;

import com.fasterxml.jackson.databind.ObjectMapper;
import griffon.core.artifact.GriffonService;
import griffon.metadata.ArtifactProviderFor;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonService;
import org.laeq.db.DAOException;
import org.laeq.db.PointDAO;
import org.laeq.db.VideoDAO;
import org.laeq.model.Point;
import org.laeq.service.MariaService;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

@javax.inject.Singleton
@ArtifactProviderFor(GriffonService.class)
public class ImportService extends AbstractGriffonService {
    private VideoDAO videoDAO;
    private PointDAO pointDAO;
    @Inject private MariaService dbService;

    public ImportService(){

    }

    public VideoDAO getVideoDAO() {
        return videoDAO == null? dbService.getVideoDAO(): videoDAO;
    }

    public PointDAO getPointDAO() {
        return pointDAO == null? dbService.getPointDAO(): pointDAO;
    }

    public ImportService(VideoDAO videoDAO, PointDAO pointDAO){
        this.videoDAO = videoDAO;
        this.pointDAO = pointDAO;
    }

    public Point execute(String json) throws IOException {
        Point result = new ObjectMapper().readValue(json, Point.class);
//
//        result.getPointSet().stream().forEach(point -> {
//            point.setVideo(result);
//
//        });

        return result;
    }

    public boolean execute(File selectedFile) throws IOException, DAOException {
//        Point result = new ObjectMapper().readValue(selectedFile, Point.class);
//
//        getVideoDAO().insert(result);
//
//        try {
//            getPointDAO().insert(result);
//            return true;
//        } catch (SQLException e) {
//            getVideoDAO().delete(result);
//        }

        return false;
    }
}