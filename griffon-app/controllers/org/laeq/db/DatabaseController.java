package org.laeq.db;

import griffon.core.RunnableWithArgs;
import griffon.core.artifact.GriffonController;
import griffon.metadata.ArtifactProviderFor;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;
import org.laeq.model.*;
import org.laeq.ui.DialogService;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

@ArtifactProviderFor(GriffonController.class)
public class DatabaseController extends AbstractGriffonController {

    @Inject private DatabaseService service;
    @Inject private DialogService dialogService;

    private ProcessBuilder builder;
    private Process dbProcess;



    public void mvcGroupInit(@Nonnull Map<String, Object> args) {
        try {
//            builder = createProcess();
//            dbProcess = builder.start();
            service.init();
        } catch (Exception e) {
            getLog().error("Cannot create the database process." + e.getMessage());
        }

        getApplication().getEventRouter().addEventListener(listeners());
    }

    @Override
    public void mvcGroupDestroy() {
        System.out.println("Database controller destruction");
//        dbProcess.destroy();
    }

    private ProcessBuilder createProcess() throws URISyntaxException {
//        java -classpath lib/hsqldb.jar org.hsqldb.server.Server --database.0 file:hsqldb/vifecodb --dbname.0 vifecodb

        String javaHome = System.getProperty("java.home");
        String javaBin = javaHome + File.separator + "bin" + File.separator + "java";
        URL hslqdbPath = getClass().getClassLoader().getResource("db/lib/hsqldb.jar");


        ProcessBuilder builder = new ProcessBuilder(javaBin, "-classpath",  hslqdbPath.getFile(),
                "org.hsqldb.server.Server", "--database.0",  "file:hsqldb/vifecodb",  "--dbname.0", " vifecodb");
        builder.redirectErrorStream(true);

        return builder;
    }

    private Map<String, RunnableWithArgs> listeners(){
        Map<String, RunnableWithArgs> list = new HashMap<>();

        return list;
    }

    private void dialog(String message){
        runInsideUIAsync(() -> {
            dialogService.dialog(message);
        });
    }

    private void publishAsyncEvent(String eventName, Object object){
        getApplication().getEventRouter().publishEventAsync(eventName, Arrays.asList(object));
    }

    private void publishEvent(String eventName, Object object){
        getApplication().getEventRouter().publishEvent(eventName, Arrays.asList(object));
    }
    private void publishEvent(String eventName){
        getApplication().getEventRouter().publishEvent(eventName);
    }
}
