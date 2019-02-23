package org.laeq.video;

import griffon.core.artifact.GriffonModel;
import griffon.metadata.ArtifactProviderFor;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonModel;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.math.RoundingMode;

@ArtifactProviderFor(GriffonModel.class)
public class ControlsModel extends AbstractGriffonModel {
    private SimpleDoubleProperty rate;
    private SimpleDoubleProperty volume;
    private SimpleIntegerProperty pointSize;
    private SimpleIntegerProperty pointDuration;
    private SimpleDoubleProperty pointOpacity;

    private Double defaultRate = 1.0;
    private Double defaultVolume = 1.0;
    private Integer defaultSize = 60;
    private Integer defaultDuration = 5;
    private Double defaultOpacity = 0.65;

    public ControlsModel(){
        this.rate = new SimpleDoubleProperty(this, "rate", defaultRate);
        this.volume = new SimpleDoubleProperty(this, "volume", defaultVolume);
        this.pointSize = new SimpleIntegerProperty(this, "pointSize", defaultSize);
        this.pointDuration = new SimpleIntegerProperty(this, "pointDuration", defaultDuration);
        this.pointOpacity = new SimpleDoubleProperty(this, "pointOpacity", defaultOpacity);
    }

    public final SimpleDoubleProperty rateProperty() {return rate;}
    public Double getRate() {return rate.get(); }
    public void setRate(Double rate) {
        this.rate.set(rate);
    }

    public SimpleDoubleProperty volumeProperty() {
        return volume;
    }
    public double getVolume() {
        return volume.get();
    }
    public void setVolume(int volume) {
        this.volume.set(volume);
    }


    public SimpleIntegerProperty pointSizeProperty() {
        return pointSize;
    }
    public int getPointSize() {
        return pointSize.get();
    }
    public void setPointSize(int pointSize) {
        this.pointSize.set(pointSize);
    }

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

    public Double getDefaultRate() {
        return defaultRate;
    }
    public Double getDefaultVolume() {
        return defaultVolume;
    }
    public Integer getDefaultSize() {
        return defaultSize;
    }
    public Integer getDefaultDuration() {
        return defaultDuration;
    }
    public Double getDefaultOpacity() {
        return defaultOpacity;
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
            this.setRate(bg.setScale(1, RoundingMode.HALF_EVEN).doubleValue());
        }
    }
    public void decreateRate() {
        if(getRate() > 0.1){
            BigDecimal bg = new BigDecimal(getRate() - 0.1);
            this.setRate(bg.setScale(1, RoundingMode.HALF_EVEN).doubleValue());
        }
    }
}