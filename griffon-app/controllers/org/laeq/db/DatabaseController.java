package org.laeq.db;

import griffon.core.RunnableWithArgs;
import griffon.core.artifact.GriffonController;
import griffon.metadata.ArtifactProviderFor;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;
import org.laeq.service.MariaService;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

@ArtifactProviderFor(GriffonController.class)
public class DatabaseController extends AbstractGriffonController {
    @Inject private MariaService mariaService;

    public void mvcGroupInit(@Nonnull Map<String, Object> args) {
//        runOutsideUI(() ->{
//            try {
                mariaService.start();
//            } catch (ManagedProcessException e) {
//               getLog().error("MariaService start failed: " + e.getMessage());
//            }
//        });
    }

    @Override
    public void mvcGroupDestroy() {

    }

    private Map<String, RunnableWithArgs> listeners(){
        Map<String, RunnableWithArgs> list = new HashMap<>();

        return list;
    }
}
