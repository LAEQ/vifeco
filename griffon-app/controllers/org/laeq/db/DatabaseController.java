package org.laeq.db;

import griffon.core.artifact.GriffonController;
import griffon.core.controller.ControllerAction;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonController;

import griffon.transform.Threading;
import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;

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
}
