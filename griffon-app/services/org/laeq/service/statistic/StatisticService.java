package org.laeq.service.statistic;

import griffon.core.artifact.GriffonService;
import griffon.metadata.ArtifactProviderFor;
import javafx.util.Duration;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonService;
import org.laeq.model.Category;
import org.laeq.model.Point;
import org.laeq.model.Video;
import org.laeq.model.statistic.Graph;
import org.laeq.model.statistic.Vertex;

import java.util.*;

@javax.inject.Singleton
@ArtifactProviderFor(GriffonService.class)
public class StatisticService extends AbstractGriffonService {
    private Video video1;
    private Video video2;
    private Duration step;

    public HashMap<Category, Graph> graphs = new HashMap<>();
    public HashMap<Category, Set<Point>> video1CategoryMap = new HashMap<>();
    public HashMap<Category, Set<Point>> video2CategoryMap = new HashMap<>();

    public void setDurationStep(Duration step){
        this.step = step;
    }

    public void setVideos(Video video1, Video video2) {
        this.video1 = video1;
        this.video2 = video2;
    }

    public void init() throws StatisticException {
        if(! this.video1.getCollection().equals(this.video2.getCollection())){
            throw new StatisticException("Videos must have the same collection");
        }

        if(this.step == null){
            throw new StatisticException("You must set the duration step tolerance to match 2 points together");
        }

        video1.getCollection().getCategorySet().forEach(category -> {
            video1CategoryMap.put(category, new HashSet<>());
            video2CategoryMap.put(category, new HashSet<>());
        });

        video1.getPointSet().stream().forEach(point -> {
            video1CategoryMap.get(point.getCategory()).add(point);
        });
        video2.getPointSet().stream().forEach(point -> {
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

    public Map<Category, List<List<Vertex>>> execute() throws StatisticException {
        init();
        generateGraphs();

        Map<Category, List<List<Vertex>>> result = new HashMap<>();

        graphs.entrySet().forEach(entry -> {
            List<List<Vertex>> tarjan = entry.getValue().tarjan();
            result.put(entry.getKey(), tarjan);
        });

        return result;
    }


    public Map<Category, Map<Video, Long>> analyse() throws StatisticException {
        Map<Category, List<List<Vertex>>> result = execute();

        Map<Category, Map<Video, Long>> totalDiff = new HashMap<>();

        result.entrySet().forEach(entry -> {
            List<List<Vertex>> list = entry.getValue();

            Map<Video, Long> datas = new HashMap<>();
            datas.put(video1, 0L);
            datas.put(video2, 0L);

            totalDiff.put(entry.getKey(), datas);

            list.forEach( l -> {
                long totalA = l.stream().filter(vertex -> vertex.point.getVideo() == video1).count();
                long totalB = l.stream().filter(vertex -> vertex.point.getVideo() == video2).count();

                Long newValueA = datas.get(video1) + Math.max(totalA - totalB, 0);
                Long newValueB = datas.get(video2) + Math.max(totalB - totalA, 0);


                datas.put(video1, newValueA);
                datas.put(video2, newValueB);
            });
        });

        return totalDiff;
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
}