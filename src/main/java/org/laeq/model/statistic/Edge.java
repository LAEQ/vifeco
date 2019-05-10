package org.laeq.model.statistic;

import java.util.Objects;
import java.util.UUID;

public class Edge implements Comparable<Edge>{
    public Vertex start;
    public Vertex end;
    private UUID uuid = UUID.randomUUID();

    public Edge(Vertex start, Vertex end){
        this.start = start;
        this.end = end;
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
        return Objects.hash(uuid);
    }

    @Override
    public int compareTo(Edge o) {
        if(uuid.equals(o.uuid)){
            return 0;
        }
        int diffTotalEdge = start.totalEdges - o.start.totalEdges;

        if(diffTotalEdge != 0){
            return diffTotalEdge;
        }

        if(getDeltaStart() <= o.getDeltaStart()){
            return -1;
        }

        return 1;
    }
}
