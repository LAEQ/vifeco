package org.laeq;

import griffon.core.RunnableWithArgs;
import griffon.core.artifact.GriffonController;
import griffon.core.controller.ControllerAction;
import griffon.core.mvc.MVCGroup;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import griffon.transform.Threading;
import javafx.stage.Stage;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

@ArtifactProviderFor(GriffonController.class)
public class VifecoController extends AbstractGriffonController {
    private VifecoModel model;

    @MVCMember
    public void setModel(@Nonnull VifecoModel model) {
        this.model = model;
    }

    @Override
    public void mvcGroupInit(@Nonnull Map<String, Object> args) {
        getApplication().getEventRouter().addEventListener(listeners());
    }

    private Map<String, RunnableWithArgs> listeners(){
        Map<String, RunnableWithArgs> list = new HashMap<>();

        list.put("user.section", objects -> createGroup("user"));
        list.put("category.section", objects -> createGroup("category"));
        list.put("collection.section", objects -> createGroup("collection"));
        list.put("video.section", objects -> createGroup("video"));
        list.put("statistic.section", objects -> createGroup("statistic"));
        list.put("about.section", objects -> createGroup("about"));

        return list;
    }

    @Threading(Threading.Policy.INSIDE_UITHREAD_SYNC)
    public void createGroup(String groupName, Map<String, Object> args){
        destroyMVC(model.currentGroup);
        destroyMVC(groupName);

        System.out.println("D: " + getApplication().getMvcGroupManager().getGroups().keySet());
        System.out.println("D: " + getApplication().getWindowManager().getWindowNames());

        try{
            createMVCGroup(groupName, args);
            model.currentGroup = groupName;
        } catch (Exception e){
            getLog().info(String.format("CreateMVCGroup: %s - %s", groupName, e.getMessage()));
        }
    }

    @ControllerAction
    @Threading(Threading.Policy.INSIDE_UITHREAD_SYNC)
    public void createGroup(String groupName){
        destroyMVC(model.currentGroup);
        destroyMVC(groupName);

        System.out.println("D: " + getApplication().getMvcGroupManager().getGroups().keySet());
        System.out.println("D: " + getApplication().getWindowManager().getWindowNames());

        try{
            createMVCGroup(groupName);
            model.currentGroup = groupName;
        } catch (Exception e){
            getLog().info(String.format("CreateMVCGroup: %s - %s", groupName, e.getMessage()));
        }
    }

    /**
     * Destroy obsolted MVC groups the application
     */
    private void clean() {
        getApplication().getMvcGroupManager().getGroups().keySet().forEach(name -> {
            if(model.mvcKeep.contains(name) == false){
                destroyMVC(name);
            }
        });
    }

    private void closeObsoleteWindows(){
        getApplication().getWindowManager().getStartingWindow();
        getApplication().getWindowManager().getWindows().forEach( window -> {
            closeWindow((Stage) window);
        });
    }

    private void closeWindow(Stage window){
        try{
            if(window != getApplication().getWindowManager().getStartingWindow()){

            }
        } catch (Exception e){

        }
    }

    private void destroyMVC(String name){
        try{
            MVCGroup group = getApplication().getMvcGroupManager().findGroup(name);
            if(group != null){
                group.destroy();
            }
        }catch (Exception e){

        }
    }
}