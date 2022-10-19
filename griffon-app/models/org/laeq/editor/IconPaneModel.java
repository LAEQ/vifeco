package org.laeq.editor;

import griffon.core.artifact.GriffonModel;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point2D;
import javafx.util.Duration;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonModel;
import org.laeq.model.Category;
import org.laeq.model.Point;
import org.laeq.model.Video;
import org.laeq.model.icon.IconPointColorized;
import javax.annotation.Nonnull;
import java.util.*;
import java.util.stream.Collectors;

@ArtifactProviderFor(GriffonModel.class)
final public class IconPaneModel extends AbstractGriffonModel {
    @MVCMember @Nonnull private Video video;

    private Duration currentTime = Duration.ZERO;

    final private SimpleDoubleProperty width = new SimpleDoubleProperty(0);
    final private SimpleDoubleProperty height = new SimpleDoubleProperty(0);

    final public Controls controls = new Controls();

    final private NavigableSet<Point> points = new TreeSet<>();

    final private Map<String, Category> shortcutMap= new HashMap();

    public void setVideo(@Nonnull Video video){
        this.video = video;

        points.addAll(this.video.getPoints());

        video.getCollection().getCategories().forEach(c -> shortcutMap.put(c.getShortcut(), c));
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

    public Point generatePoint(String code, Duration currentTime, Point2D mousePosition) {
        Category category = getCategoryByShortcut(code);

        if(category != null && mousePosition != null){
            Point point = new Point();
            point.setVideo(video);
            video.addPoint(point);
            point.setCategory(getCategoryByShortcut(code));
            point.setStart(currentTime);
            point.setX(mousePosition.getX() / width.doubleValue());
            point.setY(mousePosition.getY() / height.doubleValue());

            return point;
        }

        return null;
    }

    private Category getCategoryByShortcut(String shortcut){
        return shortcutMap.get(shortcut);
    }

    public IconPointColorized getIcon(Point point) {
        IconPointColorized icon = point.getIconPoint();
        icon.setScaleX(controls.scale());
        icon.setScaleY(controls.scale());
        icon.setLayoutX(point.getX() * width.doubleValue());
        icon.setLayoutY(point.getY() * height.doubleValue());
        icon.setOpacity(controls.opacity.getValue());

        return icon;
    }
}