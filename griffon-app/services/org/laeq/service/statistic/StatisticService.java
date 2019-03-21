package org.laeq.service.statistic;

import griffon.core.artifact.GriffonService;
import griffon.metadata.ArtifactProviderFor;
import javafx.util.Duration;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonService;
import org.laeq.model.Category;
import org.laeq.model.statistic.Graph;
import org.laeq.model.Point;
import org.laeq.model.Video;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

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

    public void execute() throws StatisticException {
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

    public HashMap<Category, Set<Point>> getVideo1CategoryMap() {
        return video1CategoryMap;
    }

    public HashMap<Category, Set<Point>> getVideo2CategoryMap() {
        return video2CategoryMap;
    }

    public void generateGraphs() {
        this.video1CategoryMap.entrySet().forEach(e -> {
            graphs.put(e.getKey(), new Graph());
        });

        this.video1CategoryMap.entrySet().forEach(e -> {
            Set<Point> points = this.video2CategoryMap.get(e.getKey());

            e.getValue().stream().forEach( p1 -> {
                points.stream().forEach(p2 -> {
                    double diff = p1.getStart().subtract(p2.getStart()).toSeconds();

                    if(Math.abs(diff) <= this.step.toSeconds()){
                        graphs.get(e.getKey()).addEdges(p1, p2);
                    }
                });
            });
        });
    }

    public Graph getGraphByCategory(Category category){
        return graphs.get(category);
    }


}