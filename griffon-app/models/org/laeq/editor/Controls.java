package org.laeq.editor;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.util.Duration;

public class Controls {
//    public static final Double rate = 0.5;
//    public static final Double size = 60d;
//    public static final Double duration = 5d;
//    public static final Double opacity = 0.65;

    public Double[] speedValue = new Double[]{0.25, 2.0};
    public Double[] opacityValue = new Double[]{.1, 1.0};
    public Double[] sizeValue = new Double[]{10d, 80d};
    public Double[] durationValue = new Double[]{1d, 30d};

    public SimpleDoubleProperty duration = new SimpleDoubleProperty(10);
    public SimpleDoubleProperty speed = new SimpleDoubleProperty(1);
    public SimpleDoubleProperty size = new SimpleDoubleProperty(20);
    public SimpleDoubleProperty opacity = new SimpleDoubleProperty(.5);


    public Duration display(){
        return Duration.seconds(duration.get());
    }
//
//    public final SimpleDoubleProperty rateProperty() {return rate;}
//    public Double getRate() {return rate.get(); }
//    public void setRate(Double rate) {
//        this.rate.set(rate);
//    }
//
//    public SimpleDoubleProperty volumeProperty() {
//        return volume;
//    }
//    public double getVolume() {
//        return volume.get();
//    }
//    public void setVolume(double volume) {
//        this.volume.set(volume);
//    }
//
//
//    public SimpleDoubleProperty sizeProperty() {
//        return size;
//    }
//    public double getSize() {
//        return size.get();
//    }
//    public void setSize(double size) {
//        this.size.set(size);
//    }
//
//    public SimpleDoubleProperty opacityProperty() {
//        return opacity;
//    }
//    public void setOpacity(double opacity) {
//        this.opacity.set(opacity);
//    }
//    public double getOpacity() {
//        return opacity.get();
//    }
//
//    public SimpleDoubleProperty durationProperty() {
//        return duration;
//    }
//    public double getDuration() {
//        return duration.get();
//    }
//    public void setDuration(double duration) {
//        this.duration.set(duration);
//    }
//    public Double getDefaultRate() {
//        return ControlsDefault.rate;
//    }
//    public Double getDefaultVolume() {
//        return ControlsDefault.volume;
//    }
//    public Double getDefaultSize() {
//        return ControlsDefault.size;
//    }
//    public Double getDefaultDuration() {
//        return ControlsDefault.duration;
//    }
//    public Double getDefaultOpacity() {
//        return ControlsDefault.opacity;
//    }
//
//    public void increaseRate() {
//        if(getRate() < 4){
//            BigDecimal bg = new BigDecimal(rate.add(0.1).doubleValue());
//            this.setRate(bg.setScale(1, RoundingMode.HALF_EVEN).doubleValue());
//        }
//    }
//    public void decreateRate() {
//        if(getRate() > 0.1){
//            BigDecimal bg = new BigDecimal(getRate() - 0.1);
//            this.setRate(bg.setScale(1, RoundingMode.HALF_EVEN).doubleValue());
//        }
//    }
}
