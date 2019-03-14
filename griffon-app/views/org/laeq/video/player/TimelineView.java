package org.laeq.video.player;

import griffon.core.artifact.GriffonView;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.scene.Group;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;
import org.laeq.graphic.icon.TimelineIcon;
import org.laeq.model.Point;
import org.laeq.model.Video;
import org.laeq.model.icon.IconTimeline;

import javax.annotation.Nonnull;

@ArtifactProviderFor(GriffonView.class)
public class TimelineView extends AbstractJavaFXGriffonView {
    @MVCMember @Nonnull private TimelineController controller;
    @MVCMember @Nonnull private TimelineModel model;
    @MVCMember @Nonnull private ContainerView parentView;
    @MVCMember @Nonnull private Video video;

    private final VideoTimeline timeline = new VideoTimeline();

    private final Line line = new Line(0,0,0,160);
    private final Line durationLine = new Line(0,0,0,160);
    private Pane pane;

    @Override
    public void initUI() {
        pane = new Pane();

        AnchorPane.setTopAnchor(pane, 0d);
        AnchorPane.setBottomAnchor(pane, 0d);
        AnchorPane.setLeftAnchor(pane, 0d);
        AnchorPane.setRightAnchor(pane, 0d);

        line.setStroke(Color.RED);
        line.setStrokeWidth(1.0);
        durationLine.setStroke(Color.CADETBLUE);
        durationLine.setStrokeWidth(1.0);

        pane.getChildren().addAll(line, durationLine, timeline);
        pane.widthProperty().addListener((observable, oldValue, newValue) -> {
            double x= newValue.doubleValue() / 2;
            double x1 = model.getLineDuration() * timeline.getRatio();

            line.setStartX(x);
            line.setEndX(x);
            durationLine.setStartX(x - x1);
            durationLine.setEndX(x - x1);
            timeline.setX(x);
        });
        timeline.getGroup().setOnMouseEntered(event -> {
            if(event.getPickResult().getIntersectedNode() != null){
                try{
                    controller.highlightPoint((TimelineIcon) event.getPickResult().getIntersectedNode());
                } catch (ClassCastException e){
                    getLog().error(e.getMessage());
                }
            }
        });
        timeline.getGroup().setOnMouseExited(event -> controller.highlightPoint());
        timeline.getGroup().setOnMouseClicked(event -> {
            if(event.getPickResult().getIntersectedNode() != null){
                try{
                    TimelineIcon circle = (TimelineIcon) event.getPickResult().getIntersectedNode();
                    controller.deletePoint(circle.getIdentifier());
                    timeline.getGroup().getChildren().remove(circle);
                } catch (ClassCastException e){
                    getLog().error(e.getMessage());
                }
            }
        });
        parentView.getTimelinePane().getChildren().add(pane);
    }

    public void init(){
        timeline.init(model.getDuration());
        video.getPointSet().forEach(point -> timeline.addPoint(point));
    }

    public void play() {
        timeline.tooglePlay();
    }

    public void addPoint(Point point) {
        timeline.addPoint(point);
    }

    public void updatePosition(Duration position) {
        timeline.translate(position);
    }

    public void updateDurationLine(double newValue) {

        durationLine.setStartX(pane.getWidth() / 2 - (newValue * timeline.getRatio()));
        durationLine.setEndX(pane.getWidth() / 2 - (newValue * timeline.getRatio()));

        model.setLineDuration(newValue);
    }

    public void updateRate(double rate) {
        timeline.setRate(rate);
    }
}
