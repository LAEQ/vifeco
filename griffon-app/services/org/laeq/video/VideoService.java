package org.laeq.video;

import griffon.core.artifact.GriffonService;
import griffon.metadata.ArtifactProviderFor;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonService;
import org.laeq.icon.IconService;
import org.laeq.model.Category;
import org.laeq.model.VideoPoint;
import org.laeq.model.VideoPointList;

import javax.inject.Inject;
import java.io.FileNotFoundException;


@javax.inject.Singleton
@ArtifactProviderFor(GriffonService.class)
public final class VideoService extends AbstractGriffonService {
    @Inject private IconService iconService;

    private VideoPointList videoPointList;
    private Pane pane;

    public void setUp(Pane pane){
        this.pane = pane;
    }

    public void init(){
        tearDown();
        videoPointList = new VideoPointList();
        videoPointList.init(pane);
    }

    public void tearDown(){
        videoPointList = null;
        this.pane.getChildren().clear();
    }

    public void update(Duration now){
        videoPointList.update(now);
    }

    public void addVideoIcon(MouseEvent event, Duration start) throws FileNotFoundException {
        Group icon = iconService.generateIcon(event);

        VideoPoint vp = new VideoPoint(event.getX(), event.getY(), 100, 10, start, new Category(), icon);

        videoPointList.addVideoPoint(vp);
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