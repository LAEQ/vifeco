package org.laeq.video.player;

import griffon.core.artifact.GriffonView;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.collections.SetChangeListener;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;
import org.laeq.model.Point;
import org.laeq.model.Video;
import org.laeq.model.icon.IconPointColorized;

import javax.annotation.Nonnull;
import java.util.HashMap;

@ArtifactProviderFor(GriffonView.class)
public class TimelineView extends AbstractJavaFXGriffonView {
    @MVCMember @Nonnull private TimelineController controller;
    @MVCMember @Nonnull private TimelineModel model;
    @MVCMember @Nonnull private ContainerView parentView;
    @MVCMember @Nonnull private Video video;
    @MVCMember @Nonnull private VideoEditor editor;

    private final HashMap<Point, IconPointColorized> icons = new HashMap<>();

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
            editor.reset();

            Node node = event.getPickResult().getIntersectedNode();

            if(node.getParent() instanceof IconPointColorized){
                editor.reset((IconPointColorized)node.getParent());
            }
        });

        timeline.getGroup().setOnMouseExited(event -> {
            editor.reset();
        });

        timeline.getGroup().setOnMouseClicked(event -> {
            Node node = (Node) event.getTarget();
            if(node.getParent() instanceof IconPointColorized){
                Point point = editor.deleteTimelineIcon((IconPointColorized) node.getParent());
                if(point != null){
                    controller.deletePoint(point);
                }
            }
        });

        parentView.getTimelinePane().getChildren().add(pane);
    }

    public void init() {
        timeline.init(model.getDuration());
        timeline.addIcons(editor.getTimelineIconMap());

        editor.getTimelinePane().addListener((SetChangeListener<IconPointColorized>) change -> {
            if (change.wasAdded()) {
                timeline.addIcon(change.getElementAdded());
            }

            if (change.wasRemoved()) {
                timeline.removeIcon(change.getElementRemoved());
            }
        });
    }

    public void play() {
        timeline.tooglePlay();
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
