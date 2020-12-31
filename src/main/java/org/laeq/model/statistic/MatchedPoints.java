package org.laeq.model.statistic;

import org.laeq.model.Point;
import org.laeq.model.Video;

class MatchedPoints{
    public Point pt1;
    public Point pt2;

    public MatchedPoints(Point pt1, Point pt2){
        this.pt1 = pt1;
        this.pt2 = pt2;
    }

    public Point getPointByVideo(Video video){
        if(pt1.getVideo().equals(video)){
            return pt1;
        } else {
            return pt2;
        }
    }
}
