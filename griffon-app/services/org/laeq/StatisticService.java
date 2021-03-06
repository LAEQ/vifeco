package org.laeq;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import javafx.util.Duration;
import org.laeq.model.Category;
import org.laeq.model.Point;
import org.laeq.model.Video;
import org.laeq.model.serializer.StatisticSerializer;
import org.laeq.model.statistic.Edge;
import org.laeq.model.statistic.Graph;
import org.laeq.model.statistic.Tarjan;
import org.laeq.model.statistic.Vertex;
import org.laeq.statistic.StatisticTimeline;

import java.util.*;

@JsonSerialize(using = StatisticSerializer.class)
public class StatisticService {
    private Video video1;
    private Video video2;
    private Duration step;

    public HashMap<Category, Graph> graphs = new HashMap<>();
    public HashMap<Category, Set<Point>> video1CategoryMap = new HashMap<>();
    public HashMap<Category, Set<Point>> video2CategoryMap = new HashMap<>();

    private Map<Category, List<List<Vertex>>> tarjans = new HashMap<>();
    private Map<Category, List<Edge>> tarjanEdges = new HashMap<>();
    private Map<Category, List<Vertex>> lonelyPoints = new HashMap<>();
    private Map<Category, List<Vertex>> matchedPoints = new HashMap<>();

    public Duration getStep() {
        return step;
    }

    public Map<Category, List<List<Vertex>>> getTarjans() {
        return tarjans;
    }
    public Map<Category, List<Edge>> getTarjanEdges() {
        return tarjanEdges;
    }
    public Map<Category, List<Vertex>> getLonelyPoints() {
        return lonelyPoints;
    }

    public void execute(List<Video> filteredList, Integer step) {
        this.video1 = filteredList.get(0);
        this.video2 = filteredList.get(1);
        this.step = Duration.seconds(step);

        init();
        generateGraphs();
        tarjan();
    }

    public void init() {
        video1.getCollection().getCategories().forEach(category -> {
            video1CategoryMap.put(category, new HashSet<>());
            video2CategoryMap.put(category, new HashSet<>());
        });

        video1.getPoints().stream().forEach(point -> {
            video1CategoryMap.get(point.getCategory()).add(point);
        });
        video2.getPoints().stream().forEach(point -> {
            video2CategoryMap.get(point.getCategory()).add(point);
        });
    }

    public void generateGraphs() {
        this.video1CategoryMap.entrySet().forEach(e -> {
            Graph graph = new Graph();
            graph.addVertices(e.getValue());
            graph.addVertices(this.video2CategoryMap.get(e.getKey()));
            graphs.put(e.getKey(), graph);
        });

        this.video1CategoryMap.entrySet().forEach(e -> {
            Set<Point> points = this.video2CategoryMap.get(e.getKey());

            e.getValue().stream().forEach( p1 -> {
                points.stream().forEach(p2 -> {
                    double diff = p1.getStart().subtract(p2.getStart()).toSeconds();
                    if(Math.abs(diff) <= this.step.toSeconds()){
                        graphs.get(e.getKey()).addEdges(p1, p2);
                        graphs.get(e.getKey()).addEdges(p2, p1);
                    }
                });
            });
        });
    }


    private void tarjan(){
        graphs.entrySet().forEach(entry -> {
            tarjans.put(entry.getKey(), entry.getValue().tarjan());
        });
    }


    public List<Tarjan> getTarjanDiff() {
        List<Tarjan> result = new ArrayList<>();
        tarjans.entrySet().forEach(e -> {
            result.add(new Tarjan(video1, video2, step, e.getKey(), e.getValue()));
        });

        return result;
    }

    public StatisticTimeline getStatisticTimeline(Category category){
        StatisticTimeline timeline = new StatisticTimeline();
        timeline.init(video1, video2);
        timeline.setGraphs(this.graphs);
        timeline.setLayoutY(100);
        timeline.setLayoutX(5);

        timeline.display(category);

        return timeline;
    }

    public HashMap<Category, Set<Point>> getVideo1CategoryMap() {
        return video1CategoryMap;
    }
    public HashMap<Category, Set<Point>> getVideo2CategoryMap() {
        return video2CategoryMap;
    }
    public Graph getGraphByCategory(Category category){
        return graphs.get(category);
    }
    public HashMap<Category, Graph> getGraphs() {
        return graphs;
    }
    public Video getVideo1() {
        return video1;
    }
    public Video getVideo2() {
        return video2;
    }
}