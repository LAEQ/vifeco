package org.laeq.editor;

import griffon.core.RunnableWithArgs;
import griffon.core.artifact.GriffonController;
import griffon.core.controller.ControllerAction;
import griffon.core.mvc.MVCGroup;
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
            String name = "controls";
            Stage display = (Stage) getApplication().getWindowManager().findWindow(name);
            MVCGroup group = getApplication().getMvcGroupManager().findGroup(name);
            if(display != null){
                return;
            }

            Map<String, Object> args = new HashMap<>();
            args.put("controls", new Controls());
            try{
                if(group != null){
                    getApplication().getMvcGroupManager().destroyMVCGroup(name);
                }
                createMVCGroup(name, args);
            }catch (Exception e){
                getApplication().getLog().error(e.getMessage());
            }
        });

        list.put("tools.image_controls", objects -> {
            String name = "image_controls";
            Stage display = (Stage) getApplication().getWindowManager().findWindow(name);
            MVCGroup group = getApplication().getMvcGroupManager().findGroup(name);
            if(display != null){
                return;
            }

            Map<String, Object> args = new HashMap<>();
            args.put("controls", new ImageControls());

            try{
                if(group != null){
                    getApplication().getMvcGroupManager().destroyMVCGroup(name);
                }
                createMVCGroup(name, args);
            }catch (Exception e){
                getApplication().getLog().error(e.getMessage());
            }
        });

        list.put("tools.drawing", objects -> {
            String name = "drawing";
            Stage display = (Stage) getApplication().getWindowManager().findWindow(name);
            MVCGroup group = getApplication().getMvcGroupManager().findGroup(name);
            if(display != null){
                return;
            }

            try{
                if(group != null){
                    getApplication().getMvcGroupManager().destroyMVCGroup(name);
                }
                createMVCGroup(name);
            }catch (Exception e){
                getApplication().getLog().error(e.getMessage());
            }
        });

        list.put("tools.zoom", args -> {
            String name = "zoom";
            Stage display = (Stage) getApplication().getWindowManager().findWindow(name);
            MVCGroup group = getApplication().getMvcGroupManager().findGroup(name);
            if(display != null){
                return;
            }

            try{
                if(group != null){
                    getApplication().getMvcGroupManager().destroyMVCGroup(name);
                }
                createMVCGroup(name);
            }catch (Exception e){
                getApplication().getLog().error(e.getMessage());
            }
        });

        return list;
    }
}