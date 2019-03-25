package org.laeq.model.statistic;

import java.util.Objects;

public class Edge{
    public Vertex start;
    public Vertex end;

    public Edge(Vertex start, Vertex end){
        this.start = start;
        this.end = end;
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
}
