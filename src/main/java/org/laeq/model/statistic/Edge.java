package org.laeq.model.statistic;

import java.util.Objects;

public class Edge implements Comparable<Edge>{
    public Vertex start;
    public Vertex end;
    public double weight;

    public Edge(Vertex start, Vertex end){
        this.start = start;
        this.end = end;

        weight = Math.abs(start.point.getStartDouble() - end.point.getStartDouble());
    }

    public double getDeltaStart(){
        return Math.abs(this.start.point.getStartDouble() - this.end.point.getStartDouble());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return (Objects.equals(start, edge.start) && Objects.equals(end, edge.end) ||
                (Objects.equals(start, edge.end) && Objects.equals(end, edge.start))
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(start.point.hashCode() + end.point.hashCode());
    }

    @Override
    public int compareTo(Edge o) {
        if(getDeltaStart() <= o.getDeltaStart()){
            return -1;
        }

        return 1;
    }
}
