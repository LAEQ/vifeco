package org.laeq;

import griffon.core.RunnableWithArgs;
import griffon.core.artifact.GriffonController;
import griffon.core.controller.ControllerAction;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import griffon.transform.Threading;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;
import org.laeq.model.Video;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ArtifactProviderFor(GriffonController.class)
public class
StatisticController extends AbstractGriffonController {
    @MVCMember @Nonnull private StatisticModel model;
    @MVCMember @Nonnull private StatisticView view;

    @Inject private DatabaseService dbService;


    @Override
    public void mvcGroupInit(@Nonnull Map<String, Object> args) {
        try{
            model.videos.addAll(dbService.videoDAO.findAll());

            getApplication().getEventRouter().publishEvent("status.info", Arrays.asList("db.success.fetch"));
        } catch (Exception e){
            getApplication().getEventRouter().publishEvent("status.error", Arrays.asList("db.error.fetch"));
        }

        getApplication().getEventRouter().addEventListener(listeners());

        StatisticService statisticService = new StatisticService();
        try {
            statisticService.execute(dbService.videoDAO.findAll(), 5);
            runInsideUISync(() -> {
                view.display(statisticService);
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ControllerAction
    @Threading(Threading.Policy.OUTSIDE_UITHREAD)
    public void compare(){
        List<Video> videos = model.videos.stream().filter(v -> v.getSelected()).collect(Collectors.toList());

        if(videos.size() != 2 || videos.get(0).getCollection().equals(videos.get(1).getCollection()) == false){
            getApplication().getEventRouter().publishEvent("status.error", Arrays.asList("statistic.video.selection.error"));
            return;
        }

        try {
            StatisticService statService = new StatisticService();
            statService.execute(videos, model.durationStep.get());

            runInsideUISync(() -> {
                view.display(statService);
            });

        }catch (Exception e){
            e.printStackTrace();
            getApplication().getEventRouter().publishEvent("status.error", Arrays.asList("statistic.video.selection.error"));
        }
    }

    private Map<String, RunnableWithArgs> listeners(){
        Map<String, RunnableWithArgs> list = new HashMap<>();


        return list;
    }
}