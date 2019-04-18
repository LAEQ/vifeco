package org.laeq.statistic;

import griffon.core.artifact.GriffonController;
import griffon.core.controller.ControllerAction;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import griffon.transform.Threading;
import javafx.util.Duration;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;
import org.laeq.db.CategoryDAO;
import org.laeq.db.PointDAO;
import org.laeq.db.VideoDAO;
import org.laeq.model.Category;
import org.laeq.model.Point;
import org.laeq.model.Video;
import org.laeq.service.MariaService;
import org.laeq.service.statistic.StatisticException;
import org.laeq.service.statistic.StatisticService;
import org.laeq.settings.Settings;
import org.laeq.video.ExportService;
import org.laeq.video.ImportService;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@ArtifactProviderFor(GriffonController.class)
public class ContainerController extends AbstractGriffonController {
    @MVCMember @Nonnull private ContainerModel model;
    @MVCMember @Nonnull private ContainerView view;


    @Inject private StatisticService statService;
    @Inject private MariaService dbService;
    @Inject private ImportService importService;
    @Inject private ExportService exportService;

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

    private void setPointId(Video video, int pointId){
        for (Point point : video.getPointSet()) {
            point.setId(pointId++);
            point.setVideo(video);
        }
    }

    @ControllerAction
    @Threading(Threading.Policy.OUTSIDE_UITHREAD)
    public void compare(){
        model.getVideos().forEach(video -> {

            Iterator it = FileUtils.iterateFiles(new File(Settings.imporPath), null, false);

            while (it.hasNext()){
                File file = (File) it.next();

                if(file.getName().contains(video.getName())){
                    try {

                        String content = FileUtils.readFileToString(file, "UTF-8");
                        Video importVideo = importService.execute(content);

                        String statFileName = String.format("%s%s%s-%s.json", Settings.statisticPath, File.separator, video.getName(), System.currentTimeMillis());

                        statService.setVideos(video, importVideo);
                        statService.setDurationStep(Duration.seconds(1));
                        statService.execute();
                        exportService.export(statService);

                    } catch (IOException | StatisticException e) {
                        getLog().error(e.getMessage());
                    }

                }
            }

        });


        runInsideUISync(() -> view.loadStatisticPage());

//        Video video1 = this.model.getVideos().get(0);
//        Video video2 = this.model.getVideos().get(1);
//
//        statService.setVideos(video1, video2);
//        statService.setDurationStep(Duration.seconds(10));
//
//        try {
//            Map<Category, Map<Video, Long>> result = statService.tarjanDiff();
//
//        } catch (StatisticException e) {
//            e.printStackTrace();
//        }

        System.out.println("compare");
    }
}