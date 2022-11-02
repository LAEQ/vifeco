package org.laeq.editor;

import griffon.core.artifact.GriffonView;
import griffon.core.i18n.MessageSource;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;
import org.codehaus.griffon.runtime.javafx.artifact.AbstractJavaFXGriffonView;
import org.laeq.model.Video;
import javax.annotation.Nonnull;
import java.io.File;

@ArtifactProviderFor(GriffonView.class)
public class PlayerView extends AbstractJavaFXGriffonView implements PaneSizable {
    @MVCMember @Nonnull private PlayerController controller;
    @MVCMember @Nonnull Video video;
    @MVCMember @Nonnull EditorView parentView;
    @FXML private AnchorPane mediaAnchor;
    @FXML private Pane mediaPane;
    @FXML private Label elapsed;
    @FXML private Slider timeSlider;

    private int videoWidth = 960;
    private int videoHeight = 540;

    private MessageSource messageSource;
    private MediaView mediaView;
    private MediaPlayer mediaPlayer;

    private Duration duration;

    private InvalidationListener currentTimeListener;
    private InvalidationListener timeSliderListener;

    private ColorAdjust colorAdjust;

    @Override
    public void initUI() {
        Node node = loadFromFXML();
        parentView.playerPane.getChildren().add(node);
        connectMessageSource(node);
        connectActions(node, controller);

        messageSource = getApplication().getMessageSource();
        resizeListener();

        init();
    }

    @Override
    public void mvcGroupDestroy(){
        mediaPlayer.pause();
    }
    private void init(){
        try{
            duration = video.getDuration();

            final File file = new File(video.getPath());
            final Media media = new Media(file.getCanonicalFile().toURI().toString());

            mediaPlayer = new MediaPlayer(media);
            mediaView = new MediaView();
            mediaView.setMediaPlayer(mediaPlayer);
            mediaAnchor.getChildren().add(mediaView);

            colorAdjust = new ColorAdjust();
            mediaView.setEffect(colorAdjust);

            currentTimeListener = ov -> {
                timeSlider.setValue(mediaPlayer.getCurrentTime().divide(video.getDuration()).toMillis() * 100.0);
                updateValues();
            };

            timeSliderListener = ov -> {
                if (timeSlider.isValueChanging()) {
                    mediaPlayer.seek(video.getDuration().multiply(timeSlider.getValue() / 100.0));
                    updateValues();
                }
            };

            mediaPlayer.currentTimeProperty().addListener(currentTimeListener);

            timeSlider.setOnMousePressed(event -> {
                mediaPlayer.pause();
                mediaPlayer.currentTimeProperty().removeListener(currentTimeListener);
                timeSlider.valueProperty().addListener(timeSliderListener);
            });

            timeSlider.setOnMouseReleased(event -> {
                timeSlider.valueProperty().removeListener(timeSliderListener);
                mediaPlayer.currentTimeProperty().addListener(currentTimeListener);
            });

        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }

    }
    protected void updateValues() {
        Platform.runLater(() -> {
            Duration currentTime = mediaPlayer.getCurrentTime();
//            elapsed.setText(DurationFormatUtils.formatDuration((long)currentTime.toMillis(), "HH:mm:ss"));
            controller.updateCurrentTime(currentTime);
        });
    }
    @Override
    public void resizeListener() {
        mediaPane.layoutBoundsProperty().addListener((observable, oldValue, newValue) -> {
            final double widthRatio = newValue.getWidth() / videoWidth;
            final double heightRatio = newValue.getHeight() / videoHeight;
            final double bestRatio = Math.min(widthRatio, heightRatio);
            final double width = videoWidth * bestRatio;
            final double height = videoHeight * bestRatio;


            mediaView.setFitWidth(width);
            mediaView.setFitHeight(height);
        });
    }

    public void play() {
        mediaPlayer.play();;
    }
    public void pause() {
        mediaPlayer.pause();
    }

    public void speed(Double speed) {
        mediaPlayer.setRate(speed);
    }

    public void volume(Double volume) {
        mediaPlayer.setVolume(volume);
    }

    public void forward() {
        Duration now = mediaPlayer.getCurrentTime().add(Duration.seconds(30));
        if(now.greaterThan(video.getDuration())){
           now = video.getDuration();
        }

        Duration finalNow = now;
        mediaPlayer.seek(finalNow);
    }
    public void rewind() {
        Duration now = mediaPlayer.getCurrentTime().subtract(Duration.seconds(30));
        if(now.lessThan(Duration.ZERO)){
            now = Duration.ZERO;
        }

        Duration finalNow = now;
        mediaPlayer.seek(finalNow);

    }
    public void refreshBrightness(Double brightness){
        Platform.runLater(() -> {
            colorAdjust.setBrightness(brightness);
        });
    }
    public void refreshSaturation(Double brightness){
        Platform.runLater(() -> {
            colorAdjust.setSaturation(brightness);
        });
    }
    public void refreshContrast(Double contrast) {
        Platform.runLater(() -> {
            colorAdjust.setContrast(contrast);
        });
    }
    public void refreshHue(Double hue) {
        Platform.runLater(() -> {
            colorAdjust.setHue(hue);
        });
    }

    public void seek(Duration currentTime) {
        runInsideUIAsync(() -> {
            mediaPlayer.seek(currentTime);
        });
    }
}
