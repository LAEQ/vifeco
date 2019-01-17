package org.laeq.video;

import griffon.core.artifact.GriffonService;
import griffon.metadata.ArtifactProviderFor;
import javafx.scene.media.MediaPlayer;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonService;

@javax.inject.Singleton
@ArtifactProviderFor(GriffonService.class)
public class VideoCalculatorService extends AbstractGriffonService {

    public double getPosition(MediaPlayer mediaPlayer){
        Double result = 0.0d;

        return result;
    }

}