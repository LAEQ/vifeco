package org.laeq.editor;

import griffon.core.artifact.GriffonModel;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.geometry.Point2D;
import javafx.util.Duration;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonModel;
import org.laeq.model.Category;
import org.laeq.model.CategoryCount;
import org.laeq.model.Point;
import org.laeq.model.Video;
import org.laeq.model.icon.IconPointColorized;

import javax.annotation.Nonnull;
import java.util.*;


@ArtifactProviderFor(GriffonModel.class)
final public class PlayerModel extends AbstractGriffonModel {

    @MVCMember @Nonnull private Video video;

    //Video controls
    final public Controls controls = new Controls();

    final public SimpleBooleanProperty isPlaying = new SimpleBooleanProperty(false);

    //List for icon panel
    final public ObservableSet<Point> displayed = FXCollections.observableSet();

    //List for summary table
    final public ObservableList<Point> points = FXCollections.observableArrayList();

    //List for displaying between two durations
    final public NavigableSet<Point> sortedPoints = new TreeSet<>();

    //List for the category table
    final public ObservableList<CategoryCount> summary = FXCollections.observableArrayList();

    //Property for normalizing the icon position
    final public SimpleDoubleProperty width = new SimpleDoubleProperty(1);
    final public SimpleDoubleProperty height = new SimpleDoubleProperty(1);
    final private Map<String, Category> shortcutMap= new HashMap();
    final public SimpleBooleanProperty isReady = new SimpleBooleanProperty(Boolean.FALSE);
    final public double[] mousePosition = new double[]{0,0};

    public Boolean enabled = new Boolean(false);

    public void setVideo(@Nonnull Video video){
        this.video = video;

        //Initialize icon settings
        video.getPoints().parallelStream().forEach(p -> {
            IconPointColorized icon = p.getIconPoint();
            Double x = p.getX() * width.doubleValue();
            Double y = p.getY() * height.doubleValue();
            icon.setLayoutX(x);
            icon.setLayoutY(y);
            icon.setScaleX(controls.scale());
            icon.setScaleY(controls.scale());
            icon.setOpacity(controls.opacity.getValue());
        });

        sortedPoints.addAll(video.getPoints());
        points.addAll(video.getPoints());

        summary.addAll(video.getCategoryCount());
        video.getCollection().getCategories().forEach(c -> shortcutMap.put(c.getShortcut(), c));
    }

    public Point2D normalPosition() {
        return new Point2D(mousePosition[0] / width.doubleValue(), mousePosition[1] / height.doubleValue());
    }

    private Category getCategoryByShortcut(String shortcut){
        return shortcutMap.get(shortcut);
    }

    public Boolean hasShortcut(String shortcut){
        return shortcutMap.entrySet().contains(shortcut);
    }

    public Point generatePoint(String code, Duration currentTime) {
        Category category = getCategoryByShortcut(code);

        if(category != null){
            Point point = new Point();
            point.setVideo(video);
            point.setCategory(getCategoryByShortcut(code));
            point.setStart(currentTime);
            point.setX(mousePosition[0] / Math.max(width.doubleValue(), 1));
            point.setY(mousePosition[1] / Math.max(height.doubleValue(), 1));

            return point;
        }

        return null;
    }

    public void addPoint(Point point) {
        points.add(point);
        points.sort((o1, o2) -> o1.getStart().lessThan(o2.getStart()) ? -1 : 1);
        sortedPoints.add(point);
        video.addPoint(point);
        displayed.add(point);
        summary.stream().filter(c -> c.category.equals(point.getCategory())).findFirst().get().increment();
    }

    public void removePoint(Point point) {
        points.remove(point);
        sortedPoints.remove(point);
        video.removePoint(point);
        displayed.remove(point);
        summary.stream().filter(c -> c.category.equals(point.getCategory())).findFirst().get().decrement();
    }

    public void setCurrentTime(Duration currentTime){
        Point start = new Point();
        start.setStart(currentTime.subtract(controls.display()));

        Point end = new Point();
        end.setStart(currentTime);

        final SortedSet<Point> points = sortedPoints.subSet(start, true, end, true);

        // Remove obsolete previous subset
        displayed.removeIf(point -> points.contains(point) == false);
        displayed.addAll(points);
    }

    public Optional<Point> getPointFromIcon(IconPointColorized icon) {
        return sortedPoints.stream().filter(point -> point.getIconPoint().equals(icon)).findFirst();
    }

    public void refreshIcon() {
        displayed.forEach( p -> {
            IconPointColorized icon = p.getIconPoint();

            icon.setScaleX(controls.scale());
            icon.setScaleY(controls.scale());
            icon.setOpacity(controls.opacity.getValue());
        });
    }
}