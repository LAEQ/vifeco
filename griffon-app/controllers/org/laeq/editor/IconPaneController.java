package org.laeq.editor;

import griffon.core.RunnableWithArgs;
import griffon.core.artifact.GriffonController;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.util.Duration;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;
import org.laeq.model.Point;
import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

@ArtifactProviderFor(GriffonController.class)
public class IconPaneController extends AbstractGriffonController {
    @MVCMember @Nonnull private IconPaneModel model;
    @MVCMember @Nonnull private IconPaneView view;

    @Override
    public void mvcGroupInit(@Nonnull Map<String, Object> args) {
        getApplication().getEventRouter().addEventListener(listeners());
    }

    @Override
    public void mvcGroupDestroy(){

    }

    private Map<String, RunnableWithArgs> listeners(){
        Map<String, RunnableWithArgs> list = new HashMap<>();

        list.put("point.added", objects ->{
            model.addPoint((Point) objects[0]);
        });

        list.put("point.removed", objects ->{
            model.removePoint((Point) objects[0]);
        });

        list.put("currentTime.update", objects -> view.updateCurrentTime((Duration) objects[0]));

        return list;
    }
}