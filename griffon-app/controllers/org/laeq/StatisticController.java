package org.laeq;

import griffon.core.RunnableWithArgs;
import griffon.core.artifact.GriffonController;
import griffon.core.controller.ControllerAction;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import griffon.transform.Threading;
import javafx.collections.transformation.FilteredList;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;
import org.laeq.db.CategoryDAO;
import org.laeq.db.PointDAO;
import org.laeq.db.VideoDAO;
import org.laeq.model.Video;
import org.laeq.service.MariaService;
import org.laeq.service.statistic.StatisticService;
import org.laeq.settings.Settings;
import org.laeq.ui.DialogService;
import org.laeq.user.PreferencesService;
import org.laeq.video.ExportService;
import org.laeq.video.ImportService;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@ArtifactProviderFor(GriffonController.class)
public class StatisticController extends AbstractGriffonController {
    @MVCMember @Nonnull private StatisticModel model;
    @MVCMember @Nonnull private StatisticView view;

    @Inject private DatabaseService dbService;
    @Inject private StatisticService statService;
    @Inject private ExportService exportService;

    private VideoDAO videoDAO;
    private PointDAO pointDAO;
    private CategoryDAO categoryDAO;
    private TranslationService translationService;

    @Override
    public void mvcGroupInit(@Nonnull Map<String, Object> args) {
        try{
            model.videos.addAll(dbService.videoDAO.findAll());

            getApplication().getEventRouter().publishEvent("status.info", Arrays.asList("db.success.fetch"));
        } catch (Exception e){
            getApplication().getEventRouter().publishEvent("status.error", Arrays.asList("db.error.fetch"));
        }

//        List<Point> videos = videoDAO.findAll();

//        videos.forEach(video -> {
//            video.getPointSet().addAll(pointDAO.findByVideo(video));
//            Set<Category> categorySet = categoryDAO.findByCollection(video.getCollection());
//            video.getCollection().getCategorySet().addAll(categorySet);
//        });


        getApplication().getEventRouter().addEventListener(listeners());
    }

    @ControllerAction
    @Threading(Threading.Policy.OUTSIDE_UITHREAD)
    public void compare(){
        List<Video> videos = model.videos.stream().filter(v -> v.getSelected()).collect(Collectors.toList());

        if(videos.size() != 2 || videos.get(0).getCollection().equals(videos.get(1).getCollection()) == false){
            getApplication().getEventRouter().publishEvent("status.error", Arrays.asList("statistic.video.selection.error"));
            return;
        }

        statService.compare(videos, model.durationStep.get());
        String statFileName = String.format("%s%s%s.json", Settings.statisticPath, File.separator, videos.get(0).pathToName());
        exportService.export(statService);

//        model.getVideos().parallelStream().forEach(video -> {
//            Iterator it = FileUtils.iterateFiles(new File(Settings.imporPath), null, false);
//
//            while (it.hasNext()){
//                File file = (File) it.next();
//
//                if(file.getName().contains(video.getName())){
//                    try {
//
//                        StatisticService statService = new StatisticService();
//
//                        String content = FileUtils.readFileToString(file, "UTF-8");
//                        Point importVideo = importService.execute(content);
//
//                        String statFileName = String.format("%s%s%s-%s.json", Settings.statisticPath, File.separator, video.getName(), System.currentTimeMillis());
//
//                        statService.setVideos(video, importVideo);
//                        statService.setDurationStep(Duration.seconds(model.getDurationStep()));
//                        statService.execute();
//
//                        exportService.export(statService);
//
//                    } catch (IOException | StatisticException e) {
//                        getLog().error(e.getMessage());
//                    }
//                }
//            }
//        });
    }

    private Map<String, RunnableWithArgs> listeners(){
        Map<String, RunnableWithArgs> list = new HashMap<>();


        return list;
    }
}