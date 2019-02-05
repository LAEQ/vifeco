package org.laeq.video;

import griffon.core.RunnableWithArgs;
import griffon.core.artifact.GriffonController;
import griffon.core.controller.ControllerAction;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import griffon.transform.Threading;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;
import org.laeq.db.DatabaseService;
import org.laeq.model.Video;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ArtifactProviderFor(GriffonController.class)
public class VideoListController extends AbstractGriffonController {
    @MVCMember @Nonnull private VideoListModel model;
    @MVCMember @Nonnull private VideoListView view;
    @Inject private DatabaseService service;

    @Override
    public void mvcGroupInit(@Nonnull Map<String, Object> args) {

        this.model.addVideos(service.getVideoUser());


//        this.model.addVideos(list);
//
//        RunnableWithArgs runnable = new RunnableWithArgs() {
//            @Override
//            public void run(@Nullable Object... objects) {
//                System.out.println("VideoListController ping runnable");
//                System.out.println(objects);
//            }
//        };
//
//        getApplication().getEventRouter().addEventListener("Ping", runnable);

        getApplication().getEventRouter().addEventListener("database.video.created", videos -> {
//            Video video = (Video) videos[0];

            runInsideUISync(() -> {
//                this.model.addVideo(video);
            });
        });


    }
}