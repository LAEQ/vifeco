package org.laeq.video;

import griffon.core.RunnableWithArgs;
import griffon.core.artifact.GriffonModel;
import griffon.inject.MVCMember;
import griffon.metadata.ArtifactProviderFor;
import griffon.transform.Observable;
import javafx.beans.property.*;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonModel;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

@ArtifactProviderFor(GriffonModel.class)
public class ControlsModel extends AbstractGriffonModel {
    private SimpleDoubleProperty rate;
    private SimpleDoubleProperty volume;
    private SimpleIntegerProperty pointSize;
    private SimpleIntegerProperty pointDuration;
    private SimpleDoubleProperty pointOpacity;

    private final Integer VOLUME_MIN = 0;
    private final Integer VOLUME_MAX = 10;

    public ControlsModel(){
        this.rate = new SimpleDoubleProperty(this, "rate", 1.0);
        this.volume = new SimpleDoubleProperty(this, "volume", 1.0);
        this.pointSize = new SimpleIntegerProperty(this, "pointSize", 30);
        this.pointDuration = new SimpleIntegerProperty(this, "pointDuration", 5);
        this.pointOpacity = new SimpleDoubleProperty(this, "pointOpacity", 1);
    }

    @Nonnull
    public final SimpleDoubleProperty rateProperty() {return rate;}
    public Double getRate() {return rate.get(); }
    public void setRate(Double rate) {
        this.rate.set(rate);
    }

    @Nonnull
    public SimpleDoubleProperty volumeProperty() {
        return volume;
    }
    public double getVolume() {
        System.out.println("Get volume");
        return volume.get();
    }
    public void setVolume(int volume) {
        this.volume.set(volume);
    }


    @Nonnull
    public SimpleIntegerProperty pointSizeProperty() {
        return pointSize;
    }
    public int getPointSize() {
        return pointSize.get();
    }
    public void setPointSize(int pointSize) {
        this.pointSize.set(pointSize);
    }

    @Nonnull
    public SimpleDoubleProperty pointOpacityProperty() {
        return pointOpacity;
    }
    public void setPointOpacity(double pointOpacity) {
        this.pointOpacity.set(pointOpacity);
    }
    public double getPointOpacity() {
        return pointOpacity.get();
    }

    public SimpleIntegerProperty pointDurationProperty() {
        return pointDuration;
    }
    public int getPointDuration() {
        return pointDuration.get();
    }
    public void setPointDuration(int pointDuration) {
        this.pointDuration.set(pointDuration);
    }

    public void increaseVolume(){
//        if(getVolume() < VOLUME_MAX){
//            setVolume(getVolume() + 1);
//        }
    }
    public void decreaseVolume(){
//        if(getVolume() > VOLUME_MIN){
//            setVolume(getVolume() - 1);
//        }
    }

    public void increaseRate() {
        if(getRate() < 10){
            BigDecimal bg = new BigDecimal(rate.add(0.1).doubleValue());
            setRate(bg.setScale(1, RoundingMode.HALF_EVEN).doubleValue());
        }
    }
    public void decreateRate() {
        if(getRate() > 0.1){
            BigDecimal bg = new BigDecimal(getRate() - 0.1);
            this.setRate(bg.setScale(1, RoundingMode.HALF_EVEN).doubleValue());
        }
    }

    public double getVolumeRatio() {
        return getVolume() / 10.0;
    }
}