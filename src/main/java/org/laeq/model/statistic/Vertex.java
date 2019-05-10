package org.laeq.model.statistic;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.laeq.model.Point;

import java.util.Objects;

@JsonIgnoreProperties({"num", "min", "totalEdges"})
public class Vertex implements Comparable<Vertex>{
    public Point point;
    public int num = -1;
    public int min = -1;
    public int totalEdges = 0;

    public Vertex(Point point){
        this.point = point;
    }

    public Point getPoint() {
        return point;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vertex vertex = (Vertex) o;
        return point == vertex.point;
    }

    @Override
    public int hashCode() {
        return Objects.hash(point);
    }

    @Override
    public String toString() {
        return "V(" + point +')';
    }

    @Override
    public int compareTo(Vertex o) {
        if(totalEdges < o.totalEdges){
            return -1;
        } else if(totalEdges > o.totalEdges){
            return 1;
        }

        return 0;
    }
}