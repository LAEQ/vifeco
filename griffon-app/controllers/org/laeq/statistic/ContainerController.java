package org.laeq.statistic;

import griffon.core.artifact.GriffonController;
import griffon.core.controller.ControllerAction;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import griffon.transform.Threading;
import javafx.util.Duration;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;
import org.laeq.db.CategoryDAO;
import org.laeq.db.PointDAO;
import org.laeq.db.VideoDAO;
import org.laeq.model.Category;
import org.laeq.model.Video;
import org.laeq.service.MariaService;
import org.laeq.service.statistic.StatisticException;
import org.laeq.service.statistic.StatisticService;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Map;
import java.util.Set;

@ArtifactProviderFor(GriffonController.class)
public class ContainerController extends AbstractGriffonController {
    @MVCMember @Nonnull private ContainerModel model;
    @MVCMember @Nonnull private ContainerView view;


    @Inject private StatisticService statService;
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

    @ControllerAction
    @Threading(Threading.Policy.OUTSIDE_UITHREAD)
    public void compare(){
        Video video1 = this.model.getVideos().get(0);
        Video video2 = this.model.getVideos().get(1);

        statService.setVideos(video1, video2);
        statService.setDurationStep(Duration.seconds(10));

        try {
            Map<Category, Map<Video, Long>> result = statService.analyse();




        } catch (StatisticException e) {
            e.printStackTrace();
        }

        System.out.println("compare");
    }
}