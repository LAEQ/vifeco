package org.laeq;

import griffon.javafx.JavaFXGriffonApplication;
import org.laeq.settings.Settings;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;

public class Launcher {
    public static void main(String[] args) throws Exception {
        Locale.setDefault(new Locale("en"));

        try{
            setUp();
            JavaFXGriffonApplication.main(args);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Create the application vifeco structure in $HOME.
     * @throws Exception
     */
    private static void setUp() throws Exception{
        String[] paths = Settings.paths;
        for(int i = 0; i < paths.length; i++) {
            String pathStr = paths[i];
            Path path = Paths.get(pathStr);
            if(! Files.exists(path)){
                Files.createDirectories(path);
            }
        }
    }
}