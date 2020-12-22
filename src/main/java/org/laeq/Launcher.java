package org.laeq;

import griffon.javafx.JavaFXGriffonApplication;
import org.laeq.settings.Settings;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Launcher {
    public static void main(String[] args) throws Exception {
        try{
            setUp();
            JavaFXGriffonApplication.main(args);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

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