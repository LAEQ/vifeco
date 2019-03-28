package org.laeq.video.player;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.laeq.db.DAOException;
import org.laeq.db.PointDAO;
import org.laeq.model.Category;
import org.laeq.model.Point;
import org.laeq.model.Video;
import org.laeq.model.icon.IconPointColorized;
import org.laeq.model.icon.IconSize;
import org.laeq.video.ControlsDefault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.stream.Collectors;

public class VideoEditor {
    private final static Logger logger = LoggerFactory.getLogger(VideoEditor.class.getName());
    private final Video video;
    private final BidiMap<Point, IconPointColorized> videoIconMap;
    private final BidiMap<Point, IconPointColorized> timelineIconMap;
    private final ObservableSet<Point> pointsToDisplay = FXCollections.observableSet();
    private final ObservableSet<IconPointColorized> timelinePane = FXCollections.observableSet();
    private final ObservableSet<IconPointColorized> videoPane = FXCollections.observableSet();


    private final SimpleBooleanProperty isPlaying = new SimpleBooleanProperty(false);

    private final Set<String> shortcuts;

    private double paneWidth;
    private double paneHeight;

    private final PointDAO pointDAO;
    private Double duration = ControlsDefault.duration;
    private File file;
    private Media media;
    private MediaPlayer mediaPlayer;

    public VideoEditor(Video video, PointDAO pointDAO) throws IOException {
        this.video = video;
        this.pointDAO = pointDAO;
        this.shortcuts = video.getCollection().getCategorySet().parallelStream().map(category -> category.getShortcut()).collect(Collectors.toSet());

        videoIconMap = new DualHashBidiMap<>();
        timelineIconMap = new DualHashBidiMap<>();

        video.getPointSet().stream().forEach(point -> {
            IconPointColorized iconVideo = createIcon(point.getCategory(), 100);
            iconVideo.decorate();
            iconVideo.setLayoutX(point.getX());
            iconVideo.setLayoutY(point.getY());
            videoIconMap.put(point, iconVideo);

            IconPointColorized iconTime = createIcon(point.getCategory(), 20);
            iconTime.decorate();
            iconTime.setLayoutX(point.getStart().toSeconds());
            iconTime.setLayoutY(0);

            timelineIconMap.put(point, iconTime);
            timelinePane.add(iconTime);
        });


        file = new File(video.getPath());
        media = new Media(file.getCanonicalFile().toURI().toString());
        mediaPlayer = new MediaPlayer(media);

        pointsToDisplay.addListener((SetChangeListener<Point>) change -> {
            if(change.wasAdded()){
                Point pt = change.getElementAdded();
                IconPointColorized icon = videoIconMap.get(pt);
                icon.setLayoutX(pt.getX() * paneWidth);
                icon.setLayoutY(pt.getY() * paneHeight);
                videoPane.add(icon);
            } else if(change.wasRemoved()){
                IconPointColorized icon = videoIconMap.get(change.getElementRemoved());
                videoPane.remove(icon);
            }
        });
    }

    private void createPoint(Point point){
        IconPointColorized iconVideo = createIcon(point.getCategory(), 100);
        iconVideo.setLayoutX(point.getX() * paneWidth);
        iconVideo.setLayoutY(point.getY() * paneHeight);
        iconVideo.decorate();
        videoIconMap.put(point, iconVideo);

        videoPane.add(iconVideo);

        IconPointColorized iconTime = createIcon(point.getCategory(), 20);
        iconTime.decorate();
        iconTime.setLayoutX(point.getStart().toSeconds());
        iconTime.setLayoutY(0);

        timelineIconMap.put(point, iconTime);
        timelinePane.add(iconTime);
    }

    public ObservableSet<IconPointColorized> getTimelinePane() {
        return timelinePane;
    }
    public ObservableSet<IconPointColorized> getVideoPane() {
        return videoPane;
    }

    private IconPointColorized createIcon(Category category, int size){
        return new IconPointColorized(new IconSize(category, size));
    }

    public Duration getDuration() {
        return Duration.millis(video.getDuration());
    }

    public Set<IconPointColorized> getTimelineIconMap() {
        return timelineIconMap.values();
    }

    public void display(){
        Duration currentTime = mediaPlayer.getCurrentTime();
        Duration startDuration = (currentTime.subtract(Duration.millis( this.duration * 1000)));
        Point start = new Point(Integer.MAX_VALUE, startDuration);
        Point end = new Point(Integer.MAX_VALUE, currentTime);
        end.setStart(currentTime);

        SortedSet<Point> points = video.getPointSet().subSet(start, end);
        pointsToDisplay.retainAll(points);

        points.forEach(point -> {
            if( ! pointsToDisplay.contains(point)){
                pointsToDisplay.add(point);
            }
        });
    }

    public void setPaneWidth(double doubleValue) {
        paneWidth = doubleValue;
        reposition();
    }
    public void setPaneHeight(double doubleValue) {
        paneHeight = doubleValue;

        reposition();
    }

    public Point deleteVideoIcon(IconPointColorized videoIcon) {
        Point point = videoIconMap.getKey(videoIcon);
        IconPointColorized timelineIcon = timelineIconMap.get(point);

        if(point != null){
            try {
                pointDAO.delete(point);
                pointsToDisplay.remove(point);
                pointsToDisplay.remove(point);
                videoIconMap.remove(point);
                timelineIconMap.remove(point);
                videoPane.remove(videoIcon);
                timelinePane.remove(timelineIcon);

                video.getPointSet().remove(point);

                return point;
            } catch (DAOException e) {
                logger.error("Failed to deleteVideoIcon point: " + e.getMessage());
            }
        }

        return null;
    }

    private void reposition() {
        videoPane.forEach(icon -> {
            Point pt = videoIconMap.getKey(icon);
            icon.setLayoutX(pt.getX() * paneWidth);

            icon.setLayoutY(pt.getY() * paneHeight);
        });
    }

    public Point deleteTimelineIcon(IconPointColorized icon) {
        Point point = timelineIconMap.getKey(icon);
        IconPointColorized videoIcon = videoIconMap.get(point);

        if(point != null){
            try {
                pointDAO.delete(point);
                pointsToDisplay.remove(point);
                videoIconMap.remove(point);
                timelineIconMap.remove(point);
                videoPane.remove(videoIcon);
                timelinePane.remove(icon);

                video.getPointSet().remove(point);

                return point;
            } catch (DAOException e) {
                logger.error("Failed to deleteVideoIcon point: " + e.getMessage());
            }
        }

        return null;
    }
    public void setDuration(Double value) {
        this.duration = value;
    }
    public void reset(IconPointColorized iconTimeline) {
        Point point = timelineIconMap.getKey(iconTimeline);

        if(point != null){
            videoIconMap.get(point).colorize();
        }
    }
    public void reset() {
        videoIconMap.values().stream().forEach(IconPointColorized::reset);
    }

    public boolean isValid() {
        return file.exists() && media != null && mediaPlayer != null;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public Video getVideo() {
        return video;
    }

    public void play() {
        if(isPlaying.getValue()){
            isPlaying.set(false);
            mediaPlayer.pause();
        } else {
            mediaPlayer.play();
            isPlaying.set(true);
        }
    }

    public Point addPoint(Point2D mousePosition, KeyEvent event) {
        if(this.shortcuts.contains(event.getCode().getName())){
            Optional<Category> optionalCategory = video.getCollection().getCategorySet().stream().filter(category -> category.getShortcut().equals(event.getCode().getName())).findFirst();
            Point point = new Point();
            point.setX(mousePosition.getX());
            point.setY(mousePosition.getY());
            point.setCategory(optionalCategory.get());

            try {
                point.setVideo(video);
                point.setStart(mediaPlayer.getCurrentTime());
                pointDAO.insert(point);
                video.getPointSet().add(point);
                createPoint(point);
                return point;
            } catch (DAOException e) {
                logger.error("Error saving new point: " + e.getMessage());
                return null;
            }
        }

        return null;
    }

    public void increaseRate() {
        changeRate(0.1);
    }

    public void decreateRate() {
        changeRate(-0.1);
    }

    public void changeRate(double change){
        Double rate = mediaPlayer.getRate();

        rate += change;

        if(rate >= 0.1 && rate <= 4){
            BigDecimal bg = new BigDecimal(rate);
            mediaPlayer.setRate(bg.setScale(1, RoundingMode.HALF_EVEN).doubleValue());
        }
    }

    public double getRate(){
        return mediaPlayer.getRate();
    }

    public Duration getTotalDuration() {
        return mediaPlayer.getTotalDuration();
    }
}
