package org.laeq.statistic;

import griffon.core.RunnableWithArgs;
import griffon.core.artifact.GriffonController;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.stage.Stage;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;
import org.laeq.model.statistic.MatchedPoint;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

@ArtifactProviderFor(GriffonController.class)
public class DisplayController extends AbstractGriffonController {
    @MVCMember @Nonnull private DisplayModel model;
    @MVCMember @Nonnull private DisplayView view;

    @Override
    public void mvcGroupInit(@Nonnull Map<String, Object> args) {
        MatchedPoint mp = (MatchedPoint) args.get("matchedPoint");
        getApplication().getEventRouter().addEventListener(listeners());
    }

    @Override
    public void mvcGroupDestroy(){
        System.out.println("Display controller statistic destroyed");

        Stage statistic_display = (Stage) getApplication().getWindowManager().findWindow("statistic_display");
        if(statistic_display != null){
            statistic_display.close();
        }

        System.out.println("E: " + getApplication().getMvcGroupManager().getGroups().keySet());
        System.out.println("E: " + getApplication().getWindowManager().getWindowNames());
    }

    private Map<String, RunnableWithArgs> listeners(){
        Map<String, RunnableWithArgs> list = new HashMap<>();

        list.put("statistic.mapped_point.display", objects ->{
            MatchedPoint mp = (MatchedPoint) objects[0];
            runInsideUISync(() -> {
                view.displayPoints(mp);
            });
        });


        return list;
    }
}