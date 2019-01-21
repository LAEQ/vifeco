package org.laeq.video;

import griffon.core.RunnableWithArgs;
import griffon.core.artifact.GriffonController;
import griffon.core.controller.ControllerAction;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;

import griffon.transform.Threading;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

@ArtifactProviderFor(GriffonController.class)
public class VideoListController extends AbstractGriffonController {
    private VideoListModel model;

    @MVCMember
    public void setModel(@Nonnull VideoListModel model) {
        this.model = model;
    }

    @Override
    public void mvcGroupInit(@Nonnull Map<String, Object> args) {

        RunnableWithArgs runnable = new RunnableWithArgs() {
            @Override
            public void run(@Nullable Object... objects) {
                System.out.println("VideoListController ping runnable");
                System.out.println(objects);
            }
        };

        getApplication().getEventRouter().addEventListener("Ping", runnable);
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    public void click() {
        int count = Integer.parseInt(model.getClickCount());
        model.setClickCount(String.valueOf(count + 1));
    }
}