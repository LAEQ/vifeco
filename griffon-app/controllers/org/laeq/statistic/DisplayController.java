package org.laeq.statistic;

import griffon.core.RunnableWithArgs;
import griffon.core.artifact.GriffonController;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
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
        System.out.println("destroying player");
//        getApplication().getWindowManager().detach("statistic_display");
//        getApplication().getMvcGroupManager().getGroups().get("statistic_display").destroy();
    }

    private Map<String, RunnableWithArgs> listeners(){
        Map<String, RunnableWithArgs> list = new HashMap<>();




        return list;
    }
}