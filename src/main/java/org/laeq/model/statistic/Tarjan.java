package org.laeq.model.statistic;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javafx.util.Duration;
import org.laeq.model.Category;
import org.laeq.model.Point;
import org.laeq.model.Video;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

@JsonIgnoreProperties({"vertices", "video1", "video2", "step", "summary"})
public class Tarjan {
    public Category category;
    public List<List<Vertex>> vertices;
    public Video video1;
    public Video video2;
    public Map<Video, Result> summary = new HashMap<>();
    public List<MatchedPoint> matchedPoints = new ArrayList<>();
    public Duration step;

    public Tarjan(Video video1, Video video2, Duration step, Category category, List<List<Vertex>> vertices) {
        this.video1 = video1;
        this.video2 = video2;
        this.category = category;
        this.vertices = vertices;
        this.step = step;

        this.generateSummary();
    }
    @JsonIgnore
    public Result getSummaryVideo1() {
        return summary.get(video1);
    }
    @JsonIgnore
    public Result getSummaryVideo2() {
        return summary.get(video2);
    }

    public List<MatchedPoint> getMatchedPoints() {
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

    @JsonIgnore
    public LinkedHashMap<String, Integer> getSerieVideo1(double stepSize){
        Duration accumulator = Duration.seconds(stepSize);
        final Duration step = Duration.seconds(stepSize);

        LinkedHashMap<String, Integer> result = new LinkedHashMap<>();

        List<Point> tmp = matchedPoints.stream().filter(MatchedPoint::unmatchedVideo1)
                .map(MatchedPoint::getPoint).collect(Collectors.toList());

        while(accumulator.lessThan(video1.getDuration())){
            String key = String.format("%.0f", accumulator.toSeconds());
            final Duration limit = accumulator;
            Integer count = tmp.stream().filter(point -> {
                Duration diff = limit.subtract(point.getStart());
                return diff.lessThan(step) && diff.greaterThanOrEqualTo(Duration.ZERO);
            }).collect(Collectors.toList()).size();

            result.put(key, count);
            accumulator = accumulator.add(Duration.seconds(stepSize));
        }

        String key = String.format("%.0f", accumulator.toSeconds());
        final Duration limit = accumulator;
        Integer count = tmp.stream().filter(point -> {
            Duration diff = limit.subtract(point.getStart());
            return diff.lessThan(step) && diff.greaterThanOrEqualTo(Duration.ZERO);
        }).collect(Collectors.toList()).size();

        result.put(key, count);

        return result;
    }
    @JsonIgnore
    public LinkedHashMap<String, Integer> getSerieVideo2(double stepSize){
        Duration accumulator = Duration.seconds(stepSize);
        final Duration step = Duration.seconds(stepSize);

        LinkedHashMap<String, Integer> result = new LinkedHashMap<>();

        List<Point> tmp = matchedPoints.stream().filter(MatchedPoint::unmatchedVideo2)
                .map(MatchedPoint::getPoint).collect(Collectors.toList());

        while(accumulator.lessThan(video1.getDuration())){
            String key = String.format("%.0f", accumulator.toSeconds());
            final Duration limit = accumulator;
            Integer count = tmp.stream().filter(point -> {
                Duration diff = limit.subtract(point.getStart());
                return diff.lessThan(step) && diff.greaterThanOrEqualTo(Duration.ZERO);
            }).collect(Collectors.toList()).size();

            result.put(key, count);
            accumulator = accumulator.add(Duration.seconds(stepSize));
        }

        String key = String.format("%.0f", accumulator.toSeconds());
        final Duration limit = accumulator;
        Integer count = tmp.stream().filter(point -> {
            Duration diff = limit.subtract(point.getStart());
            return diff.lessThan(step) && diff.greaterThanOrEqualTo(Duration.ZERO);
        }).collect(Collectors.toList()).size();

        result.put(key, count);

        return result;
    }
    @JsonIgnore
    public LinkedHashMap<String, Integer> getSerieMatched(double stepSize){
        Duration accumulator = Duration.seconds(stepSize);
        final Duration step = Duration.seconds(stepSize);

        LinkedHashMap<String, Integer> result = new LinkedHashMap<>();
        List<Point> tmp = matchedPoints.stream().filter(MatchedPoint::matched)
                .map(MatchedPoint::getPoint).collect(Collectors.toList());

        while(accumulator.lessThan(video1.getDuration())){
            String key = String.format("%.0f", accumulator.toSeconds());
            final Duration limit = accumulator;
            Integer count = tmp.stream().filter(point -> {
                Duration diff = limit.subtract(point.getStart());
                return diff.lessThan(step) && diff.greaterThanOrEqualTo(Duration.ZERO);
            }).collect(Collectors.toList()).size();

            result.put(key, count);
            accumulator = accumulator.add(Duration.seconds(stepSize));
        }

        String key = String.format("%.0f", accumulator.toSeconds());
        final Duration limit = accumulator;
        Integer count = tmp.stream().filter(point -> {
            Duration diff = limit.subtract(point.getStart());
            return diff.lessThan(step) && diff.greaterThanOrEqualTo(Duration.ZERO);
        }).collect(Collectors.toList()).size();

        //Matched is double (one for each video);
        count *= 2;

        result.put(key, count);

        return result;
    }

    private Duration getDiffDuration(Duration d1, Duration d2){
        if(d1.lessThanOrEqualTo(d2)){
            return d2.subtract(d1);
        } else {
            return d1.subtract(d2);
        }
    }

    /**
     * Create a matched point with
     * @param pt1 a point from video 1
     * @param pt2 a point from video 2
     * @return
     */
    private MatchedPoint matchPointBuilder(@Nullable Point pt1, @Nullable Point pt2){
        MatchedPoint result = new MatchedPoint();

        if(pt1 != null){
            if(pt1.getVideo().equals(video1)){
                result.pt1 = pt1;
            } else {
                result.pt2 = pt1;
            }
        }

        if(pt2 != null){
            if(pt2.getVideo().equals(video2)){
                result.pt2 = pt2;
            } else {
                result.pt1 = pt2;
            }
        }

        return result;
    }

    /**
     * Loop through 2 queues of points resulted from the Tarjan algorithm
     *
     * This algorithm associate a point with the closest matching one:
     *  If it find one create a MatchedPoint with one point from video 1 and one from video 2
     *  otherwise it creates a MatchedPoint with a null point.
     *
     * @param q1 queue of points from video 1
     * @param q2 queue of points from video 2
     *
     */
    private void matchedPoints(Queue<Point> q1, Queue<Point> q2){
        Point pt1;
        Point pt2;

        //Empty the queues until one of them is empty.
        while(! q1.isEmpty() && ! q2.isEmpty()){
            pt1 = q1.remove();
            pt2 = q2.remove();
            Duration diff = getDiffDuration(pt1.getStart(), pt2.getStart());

            while(diff.greaterThan(step) && q2.isEmpty() == false) {
                matchedPoints.add(matchPointBuilder(null, pt2));
                pt2 = q2.remove();
                diff = getDiffDuration(pt1.getStart(), pt2.getStart());
            }

            matchedPoints.add(matchPointBuilder(pt1, pt2));
        }

        while(! q1.isEmpty()){
            pt1 = q1.remove();
            matchedPoints.add(matchPointBuilder(pt1, null));
        }

        while(! q2.isEmpty()){
            pt2 = q2.remove();
            matchedPoints.add(matchPointBuilder(null, pt2));
        }
    }
    @JsonIgnore
    public Integer getSummaryOverallMatched() {
        return summary.values().stream().map(result -> result.matched).mapToInt(Integer::intValue).sum();
    }
    @JsonIgnore
    public Integer getSummaryOverallUnMatched() {
        return summary.values().stream().map(result -> result.lonely).mapToInt(Integer::intValue).sum();
    }
    @JsonIgnore
    public Double getSummaryOverallConcordanceIndex() {
        Double result = Double.valueOf(getSummaryOverallMatched());
        return result / (result + getSummaryOverallUnMatched());
    }
}
