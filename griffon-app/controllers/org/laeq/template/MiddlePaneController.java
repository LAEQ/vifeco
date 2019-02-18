package org.laeq.template;

import griffon.core.RunnableWithArgs;
import griffon.core.artifact.GriffonController;
import griffon.core.controller.ControllerAction;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;

import griffon.transform.Threading;
import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

@ArtifactProviderFor(GriffonController.class)
public class MiddlePaneController extends AbstractGriffonController {
    @MVCMember @Nonnull private MiddlePaneModel model;

    @Override
    public void mvcGroupInit(@Nonnull Map<String, Object> args) {
        getApplication().getEventRouter().addEventListener(listenerList());
        getApplication().getEventRouter().publishEvent("database.user.list");
    }

    private Map<String, RunnableWithArgs> listenerList(){
        Map<String, RunnableWithArgs> list = new HashMap<>();

        list.put("user.section", objects -> {
            createGroup("user_container");
        });

        return list;
    }

    public void createGroup(String groupName){
        runInsideUISync(() -> {
            try{
                createMVCGroup(groupName);
            } catch (Exception e){
                getLog().info(String.format("CreateMVCGroup: %s - %s", groupName, e.getMessage()));
            }
        });
    }
}