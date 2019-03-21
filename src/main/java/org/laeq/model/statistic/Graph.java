package org.laeq.model.statistic;

import org.laeq.model.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public class Graph {
    public HashMap<Vertex, List<Edge>> vertices;

    public Graph(){
        vertices = new HashMap<>();
    }

    public void addVertex(Point point){
        Vertex vertex = new Vertex(point);

        vertices.put(vertex, new ArrayList<>());
    }

    public void addSimpleEdge(Point start, Point end){
        Vertex vertex = new Vertex(start);

        if(! vertices.containsKey(vertex)){
            vertices.put(vertex, new ArrayList<>());
        }

        vertices.get(vertex).add(new Edge(start, end));
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

    public List<List<Vertex>> tarjan(){
        this.vertices.keySet().stream().forEach(vertex -> vertex.num = -1);

        int counter = 0;

        Stack<Vertex> stack = new Stack<>();
        List<List<Vertex>> result = new ArrayList<>();

        this.vertices.keySet().forEach(vertex -> {
            if(vertex.num == -1){
                visit(vertex, counter, stack,  result);
            }
        });

        return result;
    }

    private void visit(Vertex vertex, int counter, Stack<Vertex> stack, List<List<Vertex>> result) {
        vertex.num = counter++;
        vertex.min = vertex.num;

        stack.push(vertex);

        for (Edge edge: this.vertices.get(vertex)) {
            if(edge.end.num == -1){
                visit(edge.end, counter, stack, result);
                vertex.min = Math.min(vertex.min, edge.end.min);
            } else if(stack.contains(edge.end)){
                vertex.min = Math.min(vertex.min, edge.end.num);
            }
        }

        if(vertex.num == vertex.min){
            Vertex w = stack.pop();

            List<Vertex> c = new ArrayList<>();
            do{
                c.add(w);
                w = stack.pop();
            } while (vertex != w);

            result.add(c);
        }
    }
}
