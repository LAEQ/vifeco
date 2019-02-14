package org.laeq.db;

import griffon.core.RunnableWithArgs;
import griffon.core.artifact.GriffonController;
import griffon.metadata.ArtifactProviderFor;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;

import org.laeq.model.Point;
import org.laeq.model.User;
import org.laeq.model.VideoUser;
import org.laeq.ui.DialogService;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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
        publishEvent("database.video_user.findAll", service.getVideoUserList());
        publishEvent("menu.user.init", new ArrayList<User>(service.getUserList()));
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

        list.put("database.video_user.load", objects -> {
            System.out.println("database.video_user.load");
            VideoUser videoUser = (VideoUser) objects[0];
            try {
                service.set(videoUser);
                publishEvent("player.video_user.load", videoUser);
                publishEvent("category.video_user.load", videoUser);
            } catch (SQLException e) {
                dialogService.dialog("DBController: Cannot retrieve datas for " + videoUser);
            }
        });

        list.put("database.user.active", objects -> {
            User user = (User) objects[0];
            try {
                service.setUserActive(user);
            } catch (Exception e) {
                getLog().error("DatabaseController: failed to set user %s active", user );
            }
        });

        list.put("database.point.new", objects -> {
            Point newPoint = (Point) objects[0];
            try {
                service.save(newPoint);
            } catch (DAOException e) {
                getLog().error("DB controller: cannot save new point %s", newPoint);
                publishEvent("player.point.not_created", Arrays.asList(newPoint));
            }
        });

        list.put("database.video.create", objects -> {
            try {
                VideoUser videoUser = service.createVideoUser((File) objects[0]);

                publishAsyncEvent("database.video_user.created", videoUser);
            } catch (Exception e) {
                getLog().error("DB controller: error while creating video user: %s", e.getMessage());
            }
        });

        return list;
    }

    private void publishAsyncEvent(String eventName, Object object){
        getApplication().getEventRouter().publishEventAsync(eventName, Arrays.asList(object));
    }

    private void publishEvent(String eventName, Object object){
        getApplication().getEventRouter().publishEvent(eventName, Arrays.asList(object));
    }
}
