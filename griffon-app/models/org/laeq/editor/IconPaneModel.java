package org.laeq.editor;

import griffon.core.artifact.GriffonModel;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.util.Duration;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonModel;
import org.laeq.model.Point;
import org.laeq.model.Video;
import org.laeq.model.icon.IconPointColorized;
import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.NavigableSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

@ArtifactProviderFor(GriffonModel.class)
final public class IconPaneModel extends AbstractGriffonModel {
    @MVCMember @Nonnull private Video video;

    private Duration currentTime = Duration.ZERO;

    private SimpleDoubleProperty width = new SimpleDoubleProperty(0);
    private SimpleDoubleProperty height = new SimpleDoubleProperty(0);

    private Controls controls = new Controls();

    private NavigableSet<Point> points = new TreeSet<>();

    public void setVideo(@Nonnull Video video){
        this.video = video;

        points.addAll(this.video.getPoints());
    }

    public void setWidth(double width) {
        this.width.set(width);
    }

    public void setHeight(double height) {
        this.height.set(height);
    }

    public Duration getCurrentTime() {
        return currentTime;
    }

    public void addPoint(Point point) {
        points.add(point);
    }

    public void removePoint(Point point) {
        points.remove(point);
    }

    public Collection<Point> subList(Point start, Point end) {
        return points.subSet(start, true, end, false);
    }

    public Collection<IconPointColorized> setCurrentTime(Duration now){
        this.currentTime = now;

        Point start = new Point();
        start.setStart(now.subtract(controls.display()));

        Point end = new Point();
        end.setStart(now.add(Duration.millis(1d)));

        return this.subList(start, end).stream().map(point -> {
            IconPointColorized icon = point.getIconPoint();
            icon.setScaleX(controls.scale());
            icon.setScaleY(controls.scale());
            icon.setLayoutX(point.getX() * width.doubleValue());
            icon.setLayoutY(point.getY() * height.doubleValue());
            icon.setOpacity(controls.opacity.getValue());
            return icon;
        }).collect(Collectors.toSet());
    }

//    public IconPointColorized getIcon(String key) {
//        Point point = collection.getIcon(key);
//        IconPointColorized icon = point.getIconPoint();
//        icon.setScaleX(controls.scale());
//        icon.setScaleY(controls.scale());
//        icon.setLayoutX(point.getX() * width.doubleValue());
//        icon.setLayoutY(point.getY() * height.doubleValue());
//        icon.setOpacity(controls.opacity.getValue());
//
//        return icon;
//    }
}