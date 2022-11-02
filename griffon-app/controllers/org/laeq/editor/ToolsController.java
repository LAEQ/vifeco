package org.laeq.editor;

import griffon.core.RunnableWithArgs;
import griffon.core.artifact.GriffonController;
import griffon.core.controller.ControllerAction;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import griffon.transform.Threading;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;
import org.laeq.DatabaseService;
import org.laeq.model.Point;
import org.laeq.model.Video;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@ArtifactProviderFor(GriffonController.class)
public class ToolsController extends AbstractGriffonController {

    @Override
    public void mvcGroupInit(@Nonnull Map<String, Object> args) {
        getApplication().getEventRouter().addEventListener(listeners());
    }

    @Override
    public void mvcGroupDestroy(){

    }

    private Map<String, RunnableWithArgs> listeners(){
        Map<String, RunnableWithArgs> list = new HashMap<>();

        list.put("tools.open", args -> {

        });

        list.put("tools.controls", objects -> {
            Stage display = (Stage) getApplication().getWindowManager().findWindow("controls");
            if(display != null){
                return;
            }

            Map<String, Object> args = new HashMap<>();
            args.put("controls", new Controls());
            createMVCGroup("controls", args);
        });

        list.put("tools.image_controls", objects -> {
            Stage display = (Stage) getApplication().getWindowManager().findWindow("image_controls");
            if(display != null){
                return;
            }

            Map<String, Object> args = new HashMap<>();
            args.put("controls", new ImageControls());
            createMVCGroup("image_controls", args);
        });

        list.put("tools.drawing", objects -> {
            Stage display = (Stage) getApplication().getWindowManager().findWindow("drawing");
            if(display != null){
                return;
            }

            createMVCGroup("drawing");
        });

        list.put("tools.zoom", args -> {
            Stage display = (Stage) getApplication().getWindowManager().findWindow("zoom");
            if(display != null){
                return;
            }

            createMVCGroup("zoom");
        });



        return list;
    }
}