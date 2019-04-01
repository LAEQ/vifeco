package org.laeq.video.player;

import griffon.core.artifact.GriffonView;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.beans.value.ChangeListener;
import javafx.collections.SetChangeListener;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;
import org.laeq.model.Point;
import org.laeq.model.icon.IconPointColorized;
import org.laeq.model.icon.IconPointPNG;

import javax.annotation.Nonnull;

@ArtifactProviderFor(GriffonView.class)
public class TimelineView extends AbstractJavaFXGriffonView {
    @MVCMember @Nonnull private TimelineController controller;
    @MVCMember @Nonnull private TimelineModel model;
    @MVCMember @Nonnull private ContainerView parentView;
    @MVCMember @Nonnull private VideoEditor editor;

    private final VideoTimeline timeline = new VideoTimeline();
    private final Line line = new Line(0,0,0,160);
    private final Line durationLine = new Line(0,0,0,160);

    private final Pane pane;
    private final ChangeListener<Number> widthPropertyListener;
    private final ChangeListener<Duration> currentTimeListener;
    private final SetChangeListener<IconPointPNG> timelinePaneListener;
    private final EventHandler<MouseEvent> mouseEnterListener;
    private final EventHandler<? super MouseEvent> mouseExitedListener;
    private final EventHandler<? super MouseEvent> mouseClickListener;

    public TimelineView(){
        pane = new Pane();

        widthPropertyListener = (observable, oldValue, newValue) -> {
            double x = newValue.doubleValue() / 2;
            double x1 = model.getLineDuration() * timeline.getRatio();

            line.setStartX(x);
            line.setEndX(x);
            durationLine.setStartX(x - x1);
            durationLine.setEndX(x - x1);
            timeline.setX(x);
        };
        currentTimeListener = (observable, oldValue, newValue) -> {
            timeline.translate(newValue);
        };
        mouseEnterListener = event -> {
            editor.reset();

            Node node = event.getPickResult().getIntersectedNode();

            if(node.getParent() instanceof IconPointPNG){
                editor.reset((IconPointPNG)node.getParent());
            }
        };
        mouseExitedListener = event -> editor.reset();
        mouseClickListener = event -> {
            Node node = (Node) event.getTarget();
            if(node.getParent() instanceof IconPointPNG){
                Point point = editor.deleteTimelineIcon((IconPointPNG) node.getParent());
                if(point != null){
                    controller.deletePoint(point);
                }
            }
        };
        timelinePaneListener = change -> {
            if (change.wasAdded()) {
                timeline.addIcon(change.getElementAdded());
            }

            if (change.wasRemoved()) {
                timeline.removeIcon(change.getElementRemoved());
            }
        };
    }

    @Override
    public void initUI() {
        AnchorPane.setTopAnchor(pane, 0d);
        AnchorPane.setBottomAnchor(pane, 0d);
        AnchorPane.setLeftAnchor(pane, 0d);
        AnchorPane.setRightAnchor(pane, 0d);

        line.setStroke(Color.RED);
        line.setStrokeWidth(1.0);
        durationLine.setStroke(Color.CADETBLUE);
        durationLine.setStrokeWidth(1.0);

        pane.getChildren().addAll(line, durationLine, timeline);
        pane.widthProperty().addListener(widthPropertyListener);
        editor.getMediaPlayer().currentTimeProperty().addListener(currentTimeListener);

        timeline.getGroup().setOnMouseEntered(mouseEnterListener);
        timeline.getGroup().setOnMouseExited(mouseExitedListener);
        timeline.getGroup().setOnMouseClicked(mouseClickListener);

        init();
        editor.play();
        editor.play();

        parentView.getTimelinePane().getChildren().add(pane);
    }

    public void init() {
        timeline.init(editor.getDuration());
        timeline.addIcons(editor.getTimelineIconMap());

        editor.getTimelinePane().addListener(timelinePaneListener);
    }

    public void updateDurationLine(double newValue) {
        durationLine.setStartX(pane.getWidth() / 2 - (newValue * timeline.getRatio()));
        durationLine.setEndX(pane.getWidth() / 2 - (newValue * timeline.getRatio()));

        model.setLineDuration(newValue);
    }

    public void destroy() {
        pane.widthProperty().removeListener(widthPropertyListener);
        editor.getMediaPlayer().currentTimeProperty().removeListener(currentTimeListener);
        editor.getTimelinePane().removeListener(timelinePaneListener);
        timeline.getGroup().removeEventHandler(MouseEvent.MOUSE_ENTERED, mouseEnterListener);
        timeline.getGroup().removeEventHandler(MouseEvent.MOUSE_EXITED, mouseExitedListener);
        timeline.getGroup().removeEventHandler(MouseEvent.MOUSE_CLICKED, mouseClickListener);
    }
}
