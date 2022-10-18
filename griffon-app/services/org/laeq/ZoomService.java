package org.laeq;

import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonService;

public class ZoomService extends AbstractGriffonService {

    public Integer mediaWidth = 1;
    public Integer mediaHeight = 1;

    private Double zoom = 1d;

    private Double width = 1d;
    private Double height = 1d;

    private Double zoomFactor = 2d;

    private Double bestRatio = mediaWidth / width;

    public ZoomService(){

    }

    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
        this.bestRatio = this.getRatio();
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
        this.bestRatio = this.getRatio();
    }

    public double getRatio(){
        Double widthRatio = width.doubleValue() / mediaWidth;
        Double heightRatio = height.doubleValue() / mediaHeight;

        return Math.min(widthRatio, heightRatio);
    }

    public double getMaskWidth(){
        return mediaWidth * this.bestRatio;
    }

    public double getMaskHeight(){
        return mediaHeight * this.bestRatio;
    }

    public double getImageWidth(){
        return this.getMaskWidth() * this.zoom;
    }

    public double getImageHeight(){
        return this.getMaskHeight() * this.zoom;
    }

    public void setZoom(Double zoom) {
        this.zoom = zoom;
    }
}
