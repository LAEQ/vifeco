package org.laeq.video;

import griffon.core.artifact.GriffonService;
import griffon.metadata.ArtifactProviderFor;
import javafx.util.Duration;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonService;


@javax.inject.Singleton
@ArtifactProviderFor(GriffonService.class)
public final class VideoService extends AbstractGriffonService {
    public String getDurationText(Duration now, Duration total){
        return String.format("%s / %s",
                formatDuration(now),
                formatDuration(total)
        );
    }

    public Double getPositionSecondsBefore(Duration totalDuration, Duration currentDuration, int rewindSeconds) {
        double totalSeconds = totalDuration.toSeconds();
        double actualSeconds = currentDuration.toSeconds();
        return Math.max(0.0, (actualSeconds - rewindSeconds) / totalSeconds * 100);
    }

    public String formatDuration(Duration duration) {
        Double seconds = duration.toSeconds();
        int hours = (int) (seconds / 3600);
        double rest = (seconds - (hours * 3600));
        int minutes = (int) ((rest) / 60);
        int sec = (int) (rest % 60);

        return String.format("%s:%s:%s", numberToString(hours), numberToString(minutes), numberToString(sec));
    }

    private String numberToString(int number){
        String value = String.format("%d", number);
        return (value.length() > 1)? value : String.format("0%s", value);
    }
}