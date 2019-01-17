package org.laeq.video;

import griffon.core.artifact.GriffonService;
import griffon.metadata.ArtifactProviderFor;
import javafx.scene.media.MediaPlayer;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonService;

import java.time.Duration;

@javax.inject.Singleton
@ArtifactProviderFor(GriffonService.class)
public class VideoCalculatorService extends AbstractGriffonService {

    public Double getPositionSecondsBefore(Duration totalDuration, Duration currentDuration, int rewindSeconds) {
        double totalSeconds = totalDuration.getSeconds();
        double actualSeconds = currentDuration.getSeconds();
        return (actualSeconds - rewindSeconds) / totalSeconds * 100;
    }

}