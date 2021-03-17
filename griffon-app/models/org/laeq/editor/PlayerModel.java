package org.laeq.editor;

import griffon.core.artifact.GriffonModel;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.util.Duration;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonModel;
import org.laeq.model.Category;
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

    //Media markers list
    final public PointCollection collection = new PointCollection();

    public Map<String, Point> points = new HashMap<>();
    public ObservableList<IconPointColorized> icons = FXCollections.emptyObservableList();
    public NavigableSet<Point> display = new TreeSet<>();

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
        collection.setPoints(video.getPoints());

        video.getCollection().getCategories().forEach(c -> shortcutMap.put(c.getShortcut(), c));
    }

    public Point2D normalPosition() {
        return new Point2D(mousePosition[0] / width.doubleValue(), mousePosition[1] / height.doubleValue());
    }

    public Point2D position(Point point){
        return new Point2D(point.getX() * width.doubleValue(), point.getY() * height.doubleValue());
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
//        sortedPoints.add(point);
//        displayed.add(point);
    }

    public void removePoint(Point point) {
//        sortedPoints.remove(point);
//        displayed.remove(point);
    }

    public Collection<IconPointColorized> setCurrentTime(Duration currentTime){
        Point start = new Point();
        start.setStart(currentTime.subtract(controls.display()));

        Point end = new Point();
        end.setStart(currentTime);

        return collection.subList(start, end);
    }

    public Optional<Point> getPointFromIcon(IconPointColorized icon) {
        return Optional.empty();
//        return sortedPoints.stream().filter(point -> point.getIconPoint().equals(icon)).findFirst();
    }

    public void refreshIcon() {
//        displayed.forEach( p -> {
//            IconPointColorized icon = p.getIconPoint();
//
//            icon.setScaleX(controls.scale());
//            icon.setScaleY(controls.scale());
//            icon.setOpacity(controls.opacity.getValue());
//        });
    }



    public IconPointColorized getIcon(String key) {
        Point point = collection.getIcon(key);
        IconPointColorized icon = point.getIconPoint();
        icon.setScaleX(controls.scale().doubleValue());
        icon.setScaleY(controls.scale().doubleValue());
        icon.setLayoutX(point.getX() * width.doubleValue());
        icon.setLayoutY(point.getY() * height.doubleValue());
        icon.setOpacity(controls.opacity.getValue());

        display.add(point);

        return icon;
    }

    public void slideCurrentTime(Duration now) {

    }
}