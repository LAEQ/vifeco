package org.laeq.model.statistic;

import org.laeq.model.Point;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Graph {
    HashMap<Vertex, List<Edge>> vertices;

    public Graph(){
        vertices = new HashMap<>();
    }

    public void addEdges(Point start, Point end){
        Vertex vertex = new Vertex(start);
        if(! vertices.containsKey(vertex)){
            vertices.put(vertex, new ArrayList<>());
        }

        vertices.get(vertex).add(new Edge(start, end));

        vertex = new Vertex(end);
        if(! vertices.containsKey(vertex)){
            vertices.put(vertex, new ArrayList<>());
        }

        vertices.get(vertex).add(new Edge(end, start));
    }
}
