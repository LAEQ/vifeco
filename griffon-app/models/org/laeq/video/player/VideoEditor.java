package org.laeq.video.player;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.laeq.db.PointDAO;
import org.laeq.model.Category;
import org.laeq.model.Point;
import org.laeq.model.Video;
import org.laeq.model.icon.IconPointColorized;
import org.laeq.model.icon.IconPointPNG;
import org.laeq.model.icon.IconSize;
import org.laeq.video.ControlsDefault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class VideoEditor {
    private final static Logger logger = LoggerFactory.getLogger(VideoEditor.class.getName());
    private final Video video;
    private final BidiMap<Point, IconPointPNG> videoIconMap;
    private final BidiMap<Point, IconPointPNG> timelineIconMap;
    private final ObservableSet<Point> pointsToTimeline = FXCollections.observableSet();
    private final ObservableSet<Point> pointsToVideo = FXCollections.observableSet();
    private final ObservableSet<IconPointPNG> timelinePane = FXCollections.observableSet();
    private final ObservableSet<IconPointPNG> videoPane = FXCollections.observableSet();

    private Map<Category, Image[]> imageViewMap;
    private final SimpleBooleanProperty isPlaying = new SimpleBooleanProperty(false);
    private final Set<String> shortcuts;
    private double paneWidth;
    private double paneHeight;
    private final PointDAO pointDAO;
    private Double duration = ControlsDefault.duration;
    private File file;
    private Media media;
    private MediaPlayer mediaPlayer;
    private Duration timelineIconDuration = Duration.seconds(20);

    public VideoEditor(Video video, PointDAO pointDAO) throws IOException {
        this.video = video;
        this.pointDAO = pointDAO;
        this.shortcuts = video.getCollection().getCategories().parallelStream().map(category -> category.getShortcut()).collect(Collectors.toSet());

        videoIconMap = new DualHashBidiMap<>();
        timelineIconMap = new DualHashBidiMap<>();

        file = new File(video.getPath());
        media = new Media(file.getCanonicalFile().toURI().toString());
        mediaPlayer = new MediaPlayer(media);

//        pointsToTimeline.addListener((SetChangeListener<Point>) change -> {
//            if(change.wasAdded()){
//                Point point = change.getElementAdded();
//                ImageView image1 = new ImageView(imageViewMap.get(point.getCategory())[2]);
//                ImageView image2 = new ImageView(imageViewMap.get(point.getCategory())[3]);
//                image1.setCache(true);
//                image1.setCacheHint(CacheHint.SPEED);
//                image2.setCache(true);
//                image2.setCacheHint(CacheHint.SPEED);
//
//                IconPointPNG icon = new IconPointPNG(image1, image2);
//                icon.setLayoutX(point.getStart().toSeconds());
//                icon.setLayoutY(0);
//                timelineIconMap.putIfAbsent(point, icon);
//                timelinePane.add(icon);
//            } else if(change.wasRemoved()){
//                IconPointPNG icon = timelineIconMap.remove(change.getElementRemoved());
//                timelinePane.remove(icon);
//            }
//        });

        pointsToVideo.addListener((SetChangeListener<Point>) change -> {
//            if(change.wasAdded()){
//                Point point = change.getElementAdded();
//                ImageView image1 = new ImageView(imageViewMap.get(point.getCategory())[0]);
//                ImageView image2 = new ImageView(imageViewMap.get(point.getCategory())[1]);
//
//                image1.setCache(true);
//                image1.setCacheHint(CacheHint.SPEED);
//                image2.setCache(true);
//                image2.setCacheHint(CacheHint.SPEED);
//
//                IconPointPNG iconVideo = new IconPointPNG(image1, image2);
//                iconVideo.setLayoutX(point.getX() * paneWidth);
//                iconVideo.setLayoutY(point.getY() * paneHeight);
//                videoIconMap.putIfAbsent(point, iconVideo);
//                videoPane.add(iconVideo);
//
//            } else if(change.wasRemoved()){
//                IconPointPNG icon = videoIconMap.remove(change.getElementRemoved());
//                videoPane.remove(icon);
//            }
        });
    }

//    private IconPointColorized generateIconTimeline(Point point){
//            IconPointColorized icon = createIcon(point.getCategory(), 20);
//            icon.decorate();
//            icon.setLayoutX(point.getStart().toSeconds());
//            icon.setLayoutY(0);
//
//            return icon;
//    }

    public ObservableSet<IconPointPNG> getTimelinePane() {
        return timelinePane;
    }
    public ObservableSet<IconPointPNG> getVideoPane() {
        return videoPane;
    }

    private IconPointColorized createIcon(Category category, int size){
        return new IconPointColorized(new IconSize(category, size));
    }
    public Duration getDuration() {
        return video.getDuration();
    }

    public Set<IconPointPNG> getTimelineIconMap() {
        return timelineIconMap.values();
    }

    public void display(){
//        Duration currentTime = mediaPlayer.getCurrentTime();
//
//        Duration startDuration = currentTime.subtract(timelineIconDuration);
//        Duration endDuration = currentTime.add(timelineIconDuration);
//        Point start = new Point(0, startDuration);
//        Point end = new Point(0, endDuration);
//
//        SortedSet<Point> points = video.getPointSet().subSet(start, end);
//        pointsToTimeline.retainAll(points);
//        points.forEach(point -> {
//            if( ! pointsToTimeline.contains(point)){
//                pointsToTimeline.add(point);
//            }
//        });
//
//        startDuration = (currentTime.subtract(Duration.millis( this.duration * 1000)));
//        start = new Point(0, startDuration);
//        end = new Point(0, currentTime.add(Duration.millis(1)));
//
//        points = video.getPointSet().subSet(start, end);
//
//        pointsToVideo.retainAll(points);
//        points.forEach(point -> {
//            if( ! pointsToVideo.contains(point)){
//                pointsToVideo.add(point);
//            }
//        });
    }

    public void setPaneWidth(double doubleValue) {
        paneWidth = doubleValue;
        timelineIconDuration = Duration.seconds(paneWidth / 50 + 1);
        reposition();
    }
    public void setPaneHeight(double doubleValue) {
        paneHeight = doubleValue;

        reposition();
    }

    public Point deleteVideoIcon(IconPointPNG videoIcon) {
        Point point = videoIconMap.getKey(videoIcon);
        IconPointPNG timelineIcon = timelineIconMap.get(point);

//        if(point != null){
//            try {
//                pointDAO.delete(point);
//                pointsToTimeline.remove(point);
//                videoIconMap.remove(point);
//                timelineIconMap.remove(point);
//                videoPane.remove(videoIcon);
//                timelinePane.remove(timelineIcon);
//
//                video.getPointSet().remove(point);
//
//                return point;
//            } catch (DAOException e) {
//                logger.error("Failed to delete the point: " + e.getMessage());
//            }
//        }

        return null;
    }
    private void reposition() {
//        videoPane.forEach(icon -> {
//            Point pt = videoIconMap.getKey(icon);
//            icon.setLayoutX(pt.getX() * paneWidth);
//
//            icon.setLayoutY(pt.getY() * paneHeight);
//        });
    }

    public Point deleteTimelineIcon(IconPointPNG icon) {
        Point point = timelineIconMap.getKey(icon);
        IconPointPNG videoIcon = videoIconMap.get(point);

//        if(point != null){
//            try {
//                pointDAO.delete(point);
//                pointsToTimeline.remove(point);
//                videoIconMap.remove(point);
//                timelineIconMap.remove(point);
//                videoPane.remove(videoIcon);
//                timelinePane.remove(icon);
//
////                video.getPointSet().remove(point);
//
//                return point;
//            } catch (DAOException e) {
//                logger.error("Failed to deleteVideoIcon point: " + e.getMessage());
//            }
//        }

        return null;
    }
    public void setDuration(Double value) {
        this.duration = value;
    }
    public void reset(IconPointPNG iconTimeline) {
        Point point = timelineIconMap.getKey(iconTimeline);

        if(point != null && videoIconMap.containsKey(point)){
            videoIconMap.get(point).colorize();
        }
    }
    public void reset() {
        videoIconMap.values().stream().forEach(IconPointPNG::reset);
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
//        if(this.shortcuts.contains(event.getCode().getName())){
//            Optional<Category> optionalCategory = video.getCollection().getCategorySet().stream().filter(category -> category.getShortcut().equals(event.getCode().getName())).findFirst();
//            Point point = new Point();
//            point.setX(mousePosition.getX());
//            point.setY(mousePosition.getY());
//            point.setCategory(optionalCategory.get());
//
//            try {
//                point.setVideo(video);
//                point.setStart(mediaPlayer.getCurrentTime());
//                pointDAO.insert(point);
//                video.getPointSet().add(point);
//                display();
//
//                return point;
//            } catch (DAOException e) {
//                logger.error("Error saving new point: " + e.getMessage());
//                return null;
//            }
//        }

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

    public void setImageViewMap(Map<Category, Image[]> imageViewMap) {
        this.imageViewMap = imageViewMap;
    }

    public SimpleBooleanProperty isPlayingProperty() {
        return isPlaying;
    }

    public void seekPlus(double time) {
        Duration currentTime = mediaPlayer.getCurrentTime().add(Duration.millis(time));

        if(currentTime.lessThan(mediaPlayer.getTotalDuration())){
            mediaPlayer.seek(currentTime);
        } else {
            mediaPlayer.seek(mediaPlayer.getTotalDuration());
        }

    }

    public void seekMinus(double time) {
        Duration currentTime = mediaPlayer.getCurrentTime().subtract(Duration.millis(time));

        if(currentTime.greaterThan(mediaPlayer.getStartTime())){
            mediaPlayer.seek(currentTime);
        } else {
            mediaPlayer.seek(mediaPlayer.getStartTime());
        }
    }

    public void reload() {
        mediaPlayer.seek(mediaPlayer.getStartTime());
    }

    public void previousPoint(Category category) {
//        Point pt = new Point();
//        pt.setVideo(this.video);
//        pt.setStart(mediaPlayer.getCurrentTime().subtract(Duration.millis(1)));
//        pt.setCategory(category);
//
//        Point previousPoint = pointDAO.findPrevious(pt);
//
//        if(previousPoint != null){
//            mediaPlayer.seek(previousPoint.getStart());
//        }
    }

    public void nextPoint(Category category) {
//        Point pt = new Point();
//        pt.setVideo(this.video);
//        pt.setStart(mediaPlayer.getCurrentTime().add(Duration.millis(1)));
//        pt.setCategory(category);
//
//        Point nextPoint = pointDAO.findNext(pt);
//
//        if(nextPoint != null){
//            mediaPlayer.seek(nextPoint.getStart());
//        }
    }
}
