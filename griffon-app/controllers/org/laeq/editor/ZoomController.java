package org.laeq.editor;

import griffon.core.RunnableWithArgs;
import griffon.core.artifact.GriffonController;
import griffon.core.controller.ControllerAction;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import griffon.transform.Threading;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;
import org.laeq.ZoomService;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


@ArtifactProviderFor(GriffonController.class)
public class ZoomController extends AbstractGriffonController {
    @MVCMember @Nonnull private ZoomView view;
    @MVCMember @Nonnull private ZoomModel model;

    @MVCMember @Nonnull private Double mediaWidth;
    @MVCMember @Nonnull private Double mediaHeight;

    @Inject public ZoomService zoomService;

    @Override
    public void mvcGroupInit(@Nonnull Map<String, Object> args) {
        getApplication().getEventRouter().addEventListener(listeners());

        model.setMediaWidth(mediaWidth);
        model.setMediaHeight(mediaHeight);
        model.setFrameWidth(mediaWidth);
    }

    @Override
    public void mvcGroupDestroy(){

    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_SYNC)
    public void reset(){
        model.setZoom(model.getZoom() + .1);
        getApplication().getEventRouter().publishEvent("zoom.change", Arrays.asList(model.getZoom()));
    }


    private Map<String, RunnableWithArgs> listeners() {
        Map<String, RunnableWithArgs> list = new HashMap<>();

        return list;
    }

    public void dispatch(String s, Double valueOf) {

    }

    public void setZoomProperty() {
        getApplication().getEventRouter().publishEvent("zoom.change", Arrays.asList(model.getZoom()));
    }
}
