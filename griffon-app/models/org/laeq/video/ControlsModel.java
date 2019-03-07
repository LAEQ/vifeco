package org.laeq.video;

import griffon.core.artifact.GriffonModel;
import griffon.metadata.ArtifactProviderFor;
import javafx.beans.property.SimpleDoubleProperty;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonModel;

import java.math.BigDecimal;
import java.math.RoundingMode;

@ArtifactProviderFor(GriffonModel.class)
public class ControlsModel extends AbstractGriffonModel {
    private SimpleDoubleProperty rate;
    private SimpleDoubleProperty volume;
    private SimpleDoubleProperty size;
    private SimpleDoubleProperty duration;
    private SimpleDoubleProperty opacity;

    public ControlsModel(){
        this.rate = new SimpleDoubleProperty(this, "rate", ControlsDefault.rate);
        this.volume = new SimpleDoubleProperty(this, "volume", ControlsDefault.volume);
        this.size = new SimpleDoubleProperty(this, "size", ControlsDefault.size);
        this.duration = new SimpleDoubleProperty(this, "duration", ControlsDefault.duration);
        this.opacity = new SimpleDoubleProperty(this, "opacity", ControlsDefault.opacity);
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
    public void setVolume(double volume) {
        this.volume.set(volume);
    }


    public SimpleDoubleProperty sizeProperty() {
        return size;
    }
    public double getSize() {
        return size.get();
    }
    public void setSize(double size) {
        this.size.set(size);
    }

    public SimpleDoubleProperty opacityProperty() {
        return opacity;
    }
    public void setOpacity(double opacity) {
        this.opacity.set(opacity);
    }
    public double getOpacity() {
        return opacity.get();
    }

    public SimpleDoubleProperty durationProperty() {
        return duration;
    }
    public double getDuration() {
        return duration.get();
    }
    public void setDuration(double duration) {
        this.duration.set(duration);
    }
    public Double getDefaultRate() {
        return ControlsDefault.rate;
    }
    public Double getDefaultVolume() {
        return ControlsDefault.volume;
    }
    public Double getDefaultSize() {
        return ControlsDefault.size;
    }
    public Double getDefaultDuration() {
        return ControlsDefault.duration;
    }
    public Double getDefaultOpacity() {
        return ControlsDefault.opacity;
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

    public Number setDefaultRate() {
        setRate(getDefaultRate());
        return getDefaultRate();
    }

    public Number setDefaultVolume() {
        setVolume(getDefaultVolume());
        return getDefaultVolume();
    }

    public Number setDefaultOpacity() {
        setOpacity(getDefaultOpacity());
        return getDefaultOpacity();
    }

    public Number setDefaultSize() {
        setSize(getDefaultSize());
        return getDefaultSize();
    }

    public Number setDefaultDuration() {
        setDuration(getDefaultDuration());
        return getDefaultDuration();
    }
}