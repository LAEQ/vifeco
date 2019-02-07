package org.laeq.db;

import griffon.core.RunnableWithArgs;
import griffon.core.artifact.GriffonController;
import griffon.core.controller.ControllerAction;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;

import griffon.transform.Threading;
import org.laeq.model.Point;
import org.laeq.model.Video;
import org.laeq.model.VideoUser;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;

@ArtifactProviderFor(GriffonController.class)
public class DatabaseController extends AbstractGriffonController {

    @Inject private DatabaseService service;

    private ProcessBuilder builder;
    private Process dbProcess;


    public void mvcGroupInit(@Nonnull Map<String, Object> args) {
        try {
            builder = createProcess();
            dbProcess = builder.start();

            service.init();
        } catch (Exception e) {
            getLog().error("Cannot create the database process." + e.getMessage());
        }

        getApplication().getEventRouter().addEventListener(listeners());


//        getApplication().getEventRouter().addEventListener("database.model.created", objects -> {
//            Video video = (Video) objects[0];
//
//            try {
//                service.create(video);
//                getApplication().getEventRouter().publishEventAsync("database.video.created", Arrays.asList(video));
//            } catch (Exception e) {
//                getLog().error(e.getMessage());
//            }
//        });
    }

    @Override
    public void mvcGroupDestroy() {
        System.out.println("Database controller destruction");
        dbProcess.destroy();
    }

    private ProcessBuilder createProcess() throws URISyntaxException {
        String javaHome = System.getProperty("java.home");
        String javaBin = javaHome + File.separator + "bin" + File.separator + "java";

        URL hslqdbPath = getClass().getClassLoader().getResource("db/lib/hsqldb.jar");

        ProcessBuilder builder = new ProcessBuilder(javaBin, "-classpath",  hslqdbPath.toExternalForm(),
                "org.hsqldb.server.Server", "--database.1",  "file:hsqldb/vifecodb",  "--dbname.1", " vifecodb");
        builder.redirectErrorStream(true);

        return builder;
    }

    private Map<String, RunnableWithArgs> listeners(){
        Map<String, RunnableWithArgs> list = new HashMap<>();

        list.put("video.load", objects -> {
            VideoUser videoUser = (VideoUser) objects[0];
            SortedSet<Point> result = service.findByVideoUser(videoUser);

            getApplication().getEventRouter().publishEventAsync("video.load.points", Arrays.asList(result));
        });

        return list;
    }
}
