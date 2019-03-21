package org.laeq.model.statistic;

import org.laeq.model.Point;

public class Edge{
    public Vertex start;
    public Vertex end;

    public Edge(Point start, Point end){
        this.start = new Vertex(start);
        this.end = new Vertex(end);
    }
}
