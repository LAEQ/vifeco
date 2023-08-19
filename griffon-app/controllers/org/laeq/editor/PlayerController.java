package org.laeq.editor;

import griffon.core.RunnableWithArgs;
import griffon.core.artifact.GriffonController;
import griffon.core.mvc.MVCGroup;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.util.Duration;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;
import org.laeq.model.Video;
import javax.annotation.Nonnull;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


@ArtifactProviderFor(GriffonController.class)
public class PlayerController extends AbstractGriffonController {
    @MVCMember @Nonnull private PlayerModel model;
    @MVCMember @Nonnull private PlayerView view;
    @MVCMember @Nonnull private Video video;

    @Override
    public void mvcGroupInit(@Nonnull Map<String, Object> args) {
        getApplication().getEventRouter().addEventListener(listeners());
    }

    private Map<String, RunnableWithArgs> listeners(){
        Map<String, RunnableWithArgs> list = new HashMap<>();

        list.put("media.play", args -> view.play());
        list.put("media.toggle", args -> view.toggle());
        list.put("media.pause", args -> view.pause());
        list.put("speed.change", args -> view.speed((Double)args[0]));
        list.put("step.change", args -> view.setAltStepInterval((Double)args[0]));
        list.put("volume.change", args -> view.volume((Double) args[0]));
        list.put("media.forward", args -> view.forward(30));
        list.put("media.rewind", args -> view.rewind(30));
        list.put("brightness.change", objects -> view.refreshBrightness((Double) objects[0]));
        list.put("saturation.change", objects -> view.refreshSaturation((Double) objects[0]));
        list.put("constrast.change", objects -> view.refreshContrast((Double) objects[0]));
        list.put("hue.change", objects -> view.refreshHue((Double) objects[0]));
        list.put("row.currentTime", objects -> view.seek((Duration) objects[0]));
        list.put("image_controls.reset", objects -> view.imageControlsReset());
        list.put("media.rewind.alt", objects -> view.rewind(view.getAltStepInterval()));
        list.put("media.forward.alt", objects -> view.forward(view.getAltStepInterval()));
        list.put("media.rewind.5", objects -> view.rewind(5));
        list.put("media.forward.5", objects -> view.forward(5));
        list.put("display.add", objects -> {
            File selectedFile = (File) objects[0];

            Map<String, Object> args = new HashMap<>();
            args.put("file", selectedFile);
            args.put("controls", new Controls());
            args.put("currentTime", view.getCurrentTime());

            String name = "display";
            MVCGroup group = getApplication().getMvcGroupManager().findGroup(name);

            try{
                if(group != null){
                    getApplication().getMvcGroupManager().destroyMVCGroup(name);
                }
                createMVCGroup(name, args);
            }catch (Exception e){
                getApplication().getLog().error(e.getMessage());
            }
        });
        list.put("zoom.change", objects -> {
            Double zoom = (Double) objects[0];
            view.setZoom(zoom);
        });

        return list;
    }

    public PlayerController() {
        super();
    }

    public void updateCurrentTime(Duration currentTime) {
        getApplication().getEventRouter().publishEventAsync("currentTime.update", Arrays.asList(currentTime));
    }
}