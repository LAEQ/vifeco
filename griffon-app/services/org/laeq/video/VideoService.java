package org.laeq.video;

import griffon.core.artifact.GriffonService;
import griffon.metadata.ArtifactProviderFor;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonService;
import org.laeq.icon.IconService;
import org.laeq.model.VideoPoint;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.FileNotFoundException;
import java.util.Arrays;


@javax.inject.Singleton
@ArtifactProviderFor(GriffonService.class)
public final class VideoService extends AbstractGriffonService {
    @Inject private IconService iconService;

    private Pane pane;

    public void setUp(@Nonnull Pane pane){
        this.pane = pane;
    }

    public void init(){
        tearDown();

    }

    public void tearDown(){

        this.pane.getChildren().clear();
    }



    public void addVideoIcon(Point2D point, Duration start) throws FileNotFoundException {
        VideoPoint vp = iconService.generatePoint(point, start);

        getApplication().getEventRouter().publishEventAsync("video.point.create", Arrays.asList(vp));

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

    public void addVideoIconDebug(Point2D point, Duration start) throws FileNotFoundException {
        VideoPoint vp = iconService.generatePoint(point, start);

    }
}