package org.laeq;

import griffon.core.RunnableWithArgs;
import griffon.core.artifact.GriffonController;
import griffon.core.controller.ControllerAction;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import griffon.transform.Threading;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;
import org.laeq.model.Video;
import org.laeq.model.statistic.MatchedPoint;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.File;
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
    @Inject private ExportService exportService;

    private StatisticService service;

    @Override
    public void mvcGroupInit(@Nonnull Map<String, Object> args) {
        runInsideUIAsync(() -> {
            try{
                List<Video> list = dbService.videoDAO.findAll();
                model.videos.addAll(list);
                getApplication().getEventRouter().publishEventAsync("status.info", Arrays.asList("db.video.fetch.success"));
            } catch (Exception e){
                getApplication().getEventRouter().publishEventAsync("status.error", Arrays.asList("db.video.fetch.error"));
            }
        });

        getApplication().getEventRouter().addEventListener(listeners());
    }

    @Override
    public void mvcGroupDestroy(){
        getApplication().getEventRouter().publishEvent("mvc.clean", Arrays.asList("statistic_display"));
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    public void export(){
        try{
            String filename = exportService.export(service);
            getApplication().getEventRouter().publishEvent("status.success.parametrized", Arrays.asList("statistic.export.success", filename));
        } catch (Exception e) {
            getApplication().getEventRouter().publishEvent("status.error", Arrays.asList("statistic.export.error"));
        }
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_SYNC)
    public void compare(){
        getApplication().getEventRouter().publishEventOutsideUI("mvc.clean", Arrays.asList("statistic_display"));

        model.reset();
        view.reset();

        List<Video> videos = model.videos.stream().filter(v -> v.getSelected()).collect(Collectors.toList());

        if(videos.size() != 2 || videos.get(0).getCollection().equals(videos.get(1).getCollection()) == false){
            getApplication().getEventRouter().publishEvent("status.error", Arrays.asList("statistic.video.selection.error"));
            return;
        }

        try {
            service = new StatisticService();
            service.execute(videos, model.durationStep.get());

            runInsideUISync(() -> {
                model.tarjans.addAll(service.getTarjanDiff());
                model.videoName.set(service.getVideo1().pathToName());
                model.user1.set(String.format("1: %s", service.getVideo1().getUser().toString()));
                model.user2.set(String.format("2: %s", service.getVideo2().getUser().toString()));
                model.collection.set(service.getVideo1().getCollection().toString());
                model.duration.set(service.getVideo1().getDurationFormatted());
            });

            getApplication().getEventRouter()
                    .publishEvent("status.info.parametrized",
                    Arrays.asList("statistic.video.selection.success",
                            String.format("%s - step: %d", service.getVideo2().pathToName(), model.durationStep.get())));

        }catch (Exception e){
            e.printStackTrace();
            getApplication().getEventRouter().publishEvent("status.error", Arrays.asList("statistic.video.selection.error"));
        }
    }

    private Map<String, RunnableWithArgs> listeners(){
        Map<String, RunnableWithArgs> list = new HashMap<>();

        return list;
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    public void displayMatchedPoint(MatchedPoint mp) {
        try {
            Object statistic_display = getApplication().getWindowManager().findWindow("statistic_display");

            File file = new File(mp.getPoint().getVideo().getPath());

            if (file.exists() == false && statistic_display == null) {
                getApplication().getEventRouter().publishEvent("status.error", Arrays.asList("video.edit.file_not_found"));
                return;
            }

            if (statistic_display == null) {
                Map<String, Object> args = new HashMap<>();
                args.put("matchedPoint", mp);
                createMVCGroup("statistic_display", args);
            } else {
                getApplication().getEventRouter().publishEventOutsideUI("statistic.mapped_point.display", Arrays.asList(mp));
            }
        }catch (Exception e){

        }
    }
}