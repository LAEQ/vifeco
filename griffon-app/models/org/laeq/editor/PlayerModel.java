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
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;


@ArtifactProviderFor(GriffonModel.class)
public class PlayerModel extends AbstractGriffonModel {
    @MVCMember @Nonnull private Video video;

    //Video controls
    public Controls controls = new Controls();

    //List for icon panel
    public ObservableSet<Point> displayed = FXCollections.observableSet();

    //List for summary table
    public ObservableList<Point> points = FXCollections.observableArrayList();

    //List for the category table
    public ObservableList<CategoryCount> summary = FXCollections.observableArrayList();

    //Property for normalizing the icon position
    public SimpleDoubleProperty width = new SimpleDoubleProperty(1);
    public SimpleDoubleProperty height = new SimpleDoubleProperty(1);

    public Boolean enabled = Boolean.FALSE;
    private Map<String, Category> shortcutMap= new HashMap();

    public SimpleBooleanProperty isReady = new SimpleBooleanProperty(Boolean.FALSE);

    public double[] mousePosition = new double[]{0,0};

    public void setVideo(@Nonnull Video video){
        this.video = video;

        points.addAll(video.getPoints());
        displayed.addAll(points);
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
        video.addPoint(point);
        displayed.add(point);
        summary.stream().filter(c -> c.category.equals(point.getCategory())).findFirst().get().increment();
    }

    public void removePoint(Point point) {
        points.remove(point);
        video.removePoint(point);
        summary.stream().filter(c -> c.category.equals(point.getCategory())).findFirst().get().decrement();
    }

    public void setCurrentTime(Duration currentTime){
        Duration startDuration = currentTime.subtract(controls.display());
        Predicate<Point> predicateFalse = pt -> ! pt.getStart().greaterThanOrEqualTo(startDuration) || ! pt.getStart().lessThanOrEqualTo(currentTime);
        Predicate<Point> predicate = pt -> pt.getStart().greaterThanOrEqualTo(startDuration) && pt.getStart().lessThanOrEqualTo(currentTime);

        if(displayed.size() > 0){
            displayed.removeAll(displayed.stream().filter(predicateFalse).collect(Collectors.toList()));
        }

        displayed.addAll(points.stream().filter(predicate).collect(Collectors.toSet()));
    }

    public Optional<Point> getPointFromIcon(IconPointColorized icon) {
        return points.stream().filter(point -> point.getIconPoint().equals(icon)).findFirst();
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