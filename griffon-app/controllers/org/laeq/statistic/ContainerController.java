package org.laeq.statistic;

import griffon.core.artifact.GriffonController;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;
import org.laeq.db.CategoryDAO;
import org.laeq.db.PointDAO;
import org.laeq.db.VideoDAO;
import org.laeq.model.Category;
import org.laeq.model.Video;
import org.laeq.service.MariaService;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Map;
import java.util.Set;

@ArtifactProviderFor(GriffonController.class)
public class ContainerController extends AbstractGriffonController {
    @MVCMember @Nonnull private ContainerModel model;
    @MVCMember @Nonnull private ContainerView view;

    @Inject private MariaService dbService;
    private VideoDAO videoDAO;
    private PointDAO pointDAO;
    private CategoryDAO categoryDAO;

    @Override
    public void mvcGroupInit(@Nonnull Map<String, Object> args) {
        videoDAO = dbService.getVideoDAO();
        pointDAO = dbService.getPointDAO();
        categoryDAO = dbService.getCategoryDAO();

        Set<Video> videos = videoDAO.findAll();

        videos.forEach(video -> {
            video.getPointSet().addAll(pointDAO.findByVideo(video));
            Set<Category> categorySet = categoryDAO.findByCollection(video.getCollection());
            video.getCollection().getCategorySet().addAll(categorySet);
        });

        model.addVideos(videos);
        view.init();
    }
}