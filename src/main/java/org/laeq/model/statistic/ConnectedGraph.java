package org.laeq.model.statistic;

import java.util.*;

public class ConnectedGraph {
    private LinkedList<Edge> edges;
    private List<Vertex> ends;
    private List<Vertex> starts;
    private Stack<Edge> stack;
    private List<Edge> result;
    private int total;

    public ConnectedGraph(LinkedList<Edge> edges, int total) {
        this.edges = edges;
        this.total = total;
        this.starts = new ArrayList<>();
        this.ends = new ArrayList<>();
        this.result = new ArrayList<>();
        stack = new Stack<>();
    }

    public List<Edge> execute(){
        Iterator<Edge> it = this.edges.iterator();

        Edge edge;

        if(it.hasNext()){
            edge = it.next();
            result.add(edge);
            stack.push(edge);
            ends.add(edge.end);
            starts.add(edge.start);
        }

        edge = null;

        while(result.size() < total) {
            if(it.hasNext()){
                edge = it.next();
            }

            if(edge == null){
                it = getIterator();
            } else if(starts.contains(edge.start) || ends.contains(edge.end)) {
                edge = null;
            } else if(ends.contains(edge.end)){

            } else {
                result.add(edge);
                stack.push(edge);
                ends.add(edge.end);
                starts.add(edge.start);
            }
        }

        return result;
    }

    private ListIterator<Edge> getIterator(){
        Edge edge = stack.pop();
        result.remove(edge);
        starts.remove(edge.start);
        ends.remove(edge.end);

        return edges.listIterator(this.edges.indexOf(edge) + 1);
    }

    public List<Edge> getResult() {
        return result;
    }
}
