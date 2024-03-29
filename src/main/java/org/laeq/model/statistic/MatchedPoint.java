package org.laeq.model.statistic;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import javafx.util.Duration;
import org.laeq.model.Point;
import org.laeq.model.Video;
import org.laeq.model.serializer.PointStatisticSerializer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Matching point generated by Tarjan algorithm.
 *
 * If one point is @Null
 *
 */
public class MatchedPoint {
    @JsonSerialize(using = PointStatisticSerializer.class)
    public Point pt1;

    @JsonSerialize(using = PointStatisticSerializer.class)
    public Point pt2;

    public MatchedPoint(){}

    /**
     * Get 2 matching points
     *
     * @return Boolean
     */
    @JsonIgnore
    public Boolean matched(){
        if(this.pt1 != null && this.pt2 != null){
            return Boolean.TRUE;
        }

        return Boolean.FALSE;
    }

    @JsonIgnore
    public List<Duration> getStarts(){
        List<Duration> result = new ArrayList<>();
        if(matched()){
            result.add(pt1.getStart());
            result.add(pt2.getStart());
        } else {
            result.add(getPoint().getStart());
        }

        return result.stream().sorted().collect(Collectors.toList());
    }

    /**
     * Get unmatched point
     * @return
     */
    @JsonIgnore
    public Boolean unmatchedVideo1(){
        return ! matched() && pt1 != null;
    }

    /**
     * Get unmatched point
     * @return
     */
    @JsonIgnore
    public Boolean unmatchedVideo2(){
        return ! matched() && pt2 != null;
    }

    @JsonIgnore
    public String getPt1Formatted(){
        return pt1 != null ? pt1.getStartFormatted2() : "";
    }
    @JsonIgnore
    public String getPt2Formatted(){
        return pt2 != null ? pt2.getStartFormatted2() : "";
    }

    /**
     * Get non null point
     * @return Point
     */
    @JsonIgnore
    public Point getPoint() {
        return pt1 != null ? pt1 : pt2;
    }

    @JsonIgnore
    public Video getVideo() {
        return pt1 != null ? pt1.getVideo() : pt2.getVideo();
    }

    @JsonIgnore
    public List<Point> getPoints() {
        List<Point> points = new ArrayList<>();

        if(matched()){
            points.add(pt1);
            points.add(pt2);
        }else{
            points.add(getPoint());
        }

        return points;
    }
}
