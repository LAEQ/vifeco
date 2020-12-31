package org.laeq.model.statistic;

import javafx.util.Duration;
import org.laeq.model.Category;
import org.laeq.model.Point;
import org.laeq.model.Video;

import java.util.*;
import java.util.stream.Collectors;

public class Tarjan {
    public Category category;
    public List<List<Vertex>> vertices;
    public Video video1;
    public Video video2;
    public Map<Video, Result> summary = new HashMap<>();
    public List<MatchedPoints> matchedPoints = new ArrayList<>();
    public Duration step;

    public Tarjan(Video video1, Video video2, Duration step, Category category, List<List<Vertex>> vertices) {
        this.video1 = video1;
        this.video2 = video2;
        this.category = category;
        this.vertices = vertices;
        this.step = step;

        this.generateSummary();
    }

    public Result getSummaryVideo1() {
        return summary.get(video1);
    }

    public Result getSummaryVideo2() {
        return summary.get(video2);
    }

    public List<MatchedPoints> getMatchedPoints() {
        return matchedPoints;
    }

    private void generateSummary(){
        summary.put(video1, new Result());
        summary.put(video2, new Result());
        vertices.stream().forEach( l -> {
            List<Vertex> video1List = l.stream().filter(vertex -> vertex.point.getVideo() == video1).collect(Collectors.toList());
            List<Vertex> video2List = l.stream().filter(vertex -> vertex.point.getVideo() == video2).collect(Collectors.toList());

            Integer totalA = video1List.size();
            Integer totalB = video2List.size();

            summary.get(video1).addMatched(Math.min(totalA, totalB));
            summary.get(video2).addMatched(Math.min(totalA, totalB));

            summary.get(video1).addLonely(Math.max(totalA - totalB, 0));
            summary.get(video2).addLonely(Math.max(totalB - totalA, 0));

            List<Point> list1 = video1List.stream().map(v -> v.point).sorted().collect(Collectors.toList());
            List<Point> list2 = video2List.stream().map(v -> v.point).sorted().collect(Collectors.toList());

            Queue<Point> queue1 = new LinkedList<>(list1);
            Queue<Point> queue2 = new LinkedList<>(list2);

            if(queue1.size() < queue2.size()){
                this.matchedPoints(queue1, queue2);
            } else {
                this.matchedPoints(queue2, queue1);
            }

        });
    }

    private Duration getDiffDuration(Duration d1, Duration d2){
        if(d1.lessThanOrEqualTo(d2)){
            return d2.subtract(d1);
        } else {
            return d1.subtract(d2);
        }
    }

    private void matchedPoints(Queue<Point> q1, Queue<Point> q2){
        Point pt1;
        Point pt2;

        while(! q1.isEmpty() && ! q2.isEmpty()){
            pt1 = q1.remove();
            pt2 = q2.remove();
            Duration diff = getDiffDuration(pt1.getStart(), pt2.getStart());

            while(diff.greaterThan(step)) {
                matchedPoints.add(new MatchedPoints(null, pt2));
                pt2 = q2.remove();
                diff = getDiffDuration(pt1.getStart(), pt2.getStart());
            }

            matchedPoints.add(new MatchedPoints(pt1, pt2));
        }

        while(! q1.isEmpty()){
            pt1 = q1.remove();
            matchedPoints.add(new MatchedPoints(pt1, null));
        }

        while(! q2.isEmpty()){
            pt2 = q2.remove();
            matchedPoints.add(new MatchedPoints(null, pt2));
        }
    }
}
