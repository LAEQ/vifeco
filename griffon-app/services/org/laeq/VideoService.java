package org.laeq;

import griffon.core.artifact.GriffonService;
import griffon.metadata.ArtifactProviderFor;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonService;
import org.laeq.model.Category;
import org.laeq.model.Collection;
import org.laeq.model.User;
import org.laeq.model.Video;
import org.laeq.model.dao.*;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@javax.inject.Singleton
@ArtifactProviderFor(GriffonService.class)
public class VideoService extends AbstractGriffonService {

    @Inject private DatabaseService dbService;

    public void getVideoDuration(Video video){
        try {
            File file = new File(video.getPath());
            Media media = new Media(file.getCanonicalFile().toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(media);

            mediaPlayer.setOnError(() -> {
                System.out.println(mediaPlayer.getError());
            });

            mediaPlayer.setOnReady(()-> {
                video.setDuration(mediaPlayer.getTotalDuration());

                try {
                    dbService.videoDAO.create(video);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("Duration success");
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}