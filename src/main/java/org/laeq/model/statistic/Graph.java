package org.laeq.model.statistic;

import org.laeq.model.Point;

import java.util.*;
import java.util.stream.Collectors;

public class Graph {
    public Map<Vertex, List<Edge>> edges;
    public Map<Point, Vertex> vertices;

    public Graph(){
        vertices = new HashMap<>();
        edges = new HashMap<>();
    }

    public void addVertex(Point point){
        Vertex vertex = new Vertex(point);
        vertices.put(point, vertex);

        edges.put(vertex, new ArrayList<>());
    }

    public void addVertices(Set<Point> points){
        points.forEach(point -> addVertex(point));
    }


    public void addEdges(Point start, Point end){
        edges.get(vertices.get(start)).add(new Edge(vertices.get(start), vertices.get(end)));
    }

    public List<List<Vertex>> tarjan(){
        this.edges.keySet().stream().forEach(vertex -> vertex.num = -1);

        int counter = 0;

        Stack<Vertex> stack = new Stack<>();
        List<List<Vertex>> result = new ArrayList<>();

        this.edges.keySet().forEach(vertex -> {
            if(vertex.num == -1){
                visit(vertex, counter, stack,  result);
            }
        });

        return result;
    }

    public Set<Edge> getDistinctEdges(){
        return edges.values().stream().flatMap(List::stream).collect(Collectors.toSet());
    }

    private void visit(Vertex vertex, int counter, Stack<Vertex> stack, List<List<Vertex>> result) {
        vertex.num = counter++;
        vertex.min = vertex.num;

        stack.push(vertex);

        for (Edge edge: this.edges.get(vertex)) {
            if(edge.end.num == -1){
                visit(edge.end, counter, stack, result);
                vertex.min = Math.min(vertex.min, edge.end.min);
            } else if(stack.contains(edge.end)){
                vertex.min = Math.min(vertex.min, edge.end.num);
            }
        }

        if(vertex.num == vertex.min){
            List<Vertex> c = new ArrayList<>();
            Vertex w;
            do{
                w = stack.pop();
                c.add(w);
            }while(! stack.empty() && vertex != w);

            result.add(c);
        }
    }
}
