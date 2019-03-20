package org.laeq.video.player;

import griffon.core.artifact.GriffonController;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import griffon.transform.Threading;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

@ArtifactProviderFor(GriffonController.class)
public class ContainerController extends AbstractGriffonController {
    @MVCMember @Nonnull private VideoEditor editor;

    @Override
    public void mvcGroupInit(@Nonnull Map<String, Object> args) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("editor", editor);

        runInsideUISync(() ->{
            createGroup("controls", parameters);
            createGroup("category", parameters);
            createGroup("video_player", parameters);
            createGroup("timeline", parameters);
        });
    }

    @Override
    public void mvcGroupDestroy(){
        destroyMVCGroup("controls");
        destroyMVCGroup("category");
        destroyMVCGroup("video_player");
        destroyMVCGroup("timeline");
    }

    @Threading(Threading.Policy.INSIDE_UITHREAD_SYNC)
    public void createGroup(String groupName, Map<String, Object> args){
        try{
            createMVCGroup(groupName, args);
        } catch (Exception e){
            getLog().info(String.format("CreateMVCGroup: %s - %s", groupName, e.getMessage()));
        }
    }
}