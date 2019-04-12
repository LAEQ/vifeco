package org.laeq.service.statistic;

import griffon.core.artifact.GriffonService;
import griffon.metadata.ArtifactProviderFor;
import javafx.util.Duration;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonService;
import org.laeq.model.Category;
import org.laeq.model.Point;
import org.laeq.model.Video;
import org.laeq.model.statistic.Edge;
import org.laeq.model.statistic.Graph;
import org.laeq.model.statistic.Vertex;
import org.laeq.statistic.StatisticTimeline;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@javax.inject.Singleton
@ArtifactProviderFor(GriffonService.class)
public class StatisticService extends AbstractGriffonService {
    private Video video1;
    private Video video2;
    private Duration step;

    public HashMap<Category, Graph> graphs = new HashMap<>();
    public HashMap<Category, Set<Point>> video1CategoryMap = new HashMap<>();
    public HashMap<Category, Set<Point>> video2CategoryMap = new HashMap<>();

    private Map<Category, List<List<Vertex>>> tarjans = new HashMap<>();

    public Duration getStep() {
        return step;
    }

    public Map<Category, List<List<Vertex>>> getTarjans() {
        return tarjans;
    }

    public Map<Category, Map<Video, Long>> getTarjanDiffs() {
        return tarjanDiffs;
    }

    private Map<Category, Map<Video, Long>> tarjanDiffs = new HashMap<>();

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

        graphs.entrySet().forEach(entry -> {
            List<List<Vertex>> tarjan = entry.getValue().tarjan();
            tarjans.put(entry.getKey(), tarjan);
        });

        return tarjans;
    }

    public void test(){
        Map<Category, List<Edge>> result = new HashMap<>();

        tarjans.keySet().forEach(category -> {
            result.put(category, test1(category));
        });
    }

    public List<Edge> test1(Category category){
        List<List<Vertex>> tmp = tarjans.get(category);

        List<Edge> result = new ArrayList<>();

        tmp.forEach( vertices -> {
            result.addAll(test2(vertices));
        });

        return result;
    }

    public List<Edge> test2(List<Vertex> vertices){
        List<Edge> result = new ArrayList<>();

        // Pick the smallest list
        List<Vertex> videoA_vertices = vertices.parallelStream().filter(vertex -> vertex.getPoint().getVideo().equals(video1)).collect(Collectors.toList());
        List<Vertex> videoB_vertices = vertices.parallelStream().filter(vertex -> vertex.getPoint().getVideo().equals(video2)).collect(Collectors.toList());


        if(videoA_vertices.size() == 0 || videoB_vertices.size() == 0){
            return result;
        }

        List<Vertex> selected = (videoA_vertices.size() <= videoB_vertices.size()) ? videoA_vertices : videoB_vertices;

        int indexVertex = 0;
        int indexEdges = 0;

        Vertex v = selected.get(indexVertex++);

        List<Edge> edges = getEdges(v);
        while(result.size() !=  selected.size()){
            result.add(edges.get(indexEdges++));

            test3(selected, indexVertex, result);

            if(result.size() != selected.size()){
                result.remove(result.size() - 1);
            }
        }

        return result;
    }

    private void test3(List<Vertex> vertices, int indexVertex, List<Edge> result) {
        if(vertices.size() == indexVertex){
            return;
        }

        List<Vertex> endVertices = result.stream().map(e -> e.end).collect(Collectors.toList());
        Vertex v = vertices.get(indexVertex++);
        List<Edge> edges = getEdges(v).stream().filter(e -> ! endVertices.contains(e)).collect(Collectors.toList());

        if(edges.size() == 0){
            return;
        }

        int edgeIndex = 0;
        while(edgeIndex < edges.size() && vertices.size() != result.size()){
            result.add(edges.get(edgeIndex++));
            test3(vertices, indexVertex, result);

            if(result.size() != vertices.size()){
                result.remove(result.size() - 1);
            }
        }
    }

    private List<Edge> getEdges(Vertex v) {
        Graph graph = graphs.get(v.point.getCategory());

        return graph.edges.get(v).stream().sorted().collect(Collectors.toList());
    }


    public Map<Category, Map<Video, Long>> analyse() throws StatisticException {
        execute();

        tarjans.entrySet().forEach(entry -> {
            List<List<Vertex>> list = entry.getValue();

            Map<Video, Long> datas = new HashMap<>();
            datas.put(video1, 0L);
            datas.put(video2, 0L);

            tarjanDiffs.put(entry.getKey(), datas);

            list.forEach( l -> {
                long totalA = l.stream().filter(vertex -> vertex.point.getVideo() == video1).count();
                long totalB = l.stream().filter(vertex -> vertex.point.getVideo() == video2).count();

                Long newValueA = datas.get(video1) + Math.max(totalA - totalB, 0);
                Long newValueB = datas.get(video2) + Math.max(totalB - totalA, 0);

                datas.put(video1, newValueA);
                datas.put(video2, newValueB);
            });
        });

        return tarjanDiffs;
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

    public long getTotalVideoAByCategory(Category category){
        return video1.getPointSet().stream().filter(point -> point.getCategory().equals(category)).count();
    }

    public long getTotalVideoBByCategory(Category category){
        return video2.getPointSet().stream().filter(point -> point.getCategory().equals(category)).count();
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

    public StatisticTimeline getStatisticTimeline_v2(Category category) {
        try {
            execute();
            analyse();
        } catch (StatisticException e) {
            e.printStackTrace();
        }

        List<List<Vertex>> tarjanCat = tarjans.get(category);

        StatisticTimeline timeline = new StatisticTimeline();
        timeline.init(video1, video2);
        timeline.setGraphs(this.graphs);
        timeline.setLayoutY(100);
        timeline.setLayoutX(5);
        timeline.drawDots(category);

        tarjanCat.forEach(l -> {
            List<Vertex> vA = l.stream().filter(vertex -> vertex.point.getVideo().equals(video1)).sorted(new Comparator<Vertex>() {
                @Override
                public int compare(Vertex o1, Vertex o2) {
                    double diff = o1.point.getStartDouble() - o2.point.getStartDouble();
                    if (diff <= 0) {
                        return -1;
                    } else {
                        return 1;
                    }

                }
            }).collect(Collectors.toList());
            List<Vertex> vB = l.stream().filter(vertex -> vertex.point.getVideo().equals(video2)).sorted(new Comparator<Vertex>() {
                @Override
                public int compare(Vertex o1, Vertex o2) {
                    double diff = o1.point.getStartDouble() - o2.point.getStartDouble();
                    if (diff <= 0) {
                        return -1;
                    } else {
                        return 1;
                    }
                }
            }).collect(Collectors.toList());

           if(vA.size() > vB.size()){
               //We use vB to find edges
               HashMap<Vertex, Edge> result = createLines(vB, vA, category);

               timeline.drawEdges(result.values());

           }else if(vB.size() > vA.size()){
               HashMap<Vertex, Edge> result = createLines(vA, vB, category);

               timeline.drawEdges(result.values());

           } else {
               HashMap<Vertex, Edge> result = createLines(vB, vA, category);

               timeline.drawEdges(result.values());
           }
        });


        return timeline;
    }

    private Comparator<Vertex> getComparator(Vertex b){
        return (o1, o2) -> {
            int diff = o1.totalEdges - o2.totalEdges;
            if(diff != 0){
                return diff;
            }

            double diffDuration1 = Math.abs(b.point.getStartDouble() - o1.point.getStartDouble());
            double diffDuration2 = Math.abs(b.point.getStartDouble() - o2.point.getStartDouble());

            if(diffDuration1 < diffDuration2){
                return -1;
            } else if(diffDuration2 < diffDuration1){
                return 1;
            }

            return 0;
        };
    }

    private HashMap<Vertex, Edge> createLines(List<Vertex> v1, List<Vertex> v2, Category category) {
        Graph graph = graphs.get(category);

        HashMap<Vertex, Edge> result = new HashMap<>();

        while(v1.size() != 0){
            Vertex b = v1.remove(0);
            //Refactor with a priority stack
//            v1.sort(getComparator(b));

            //Search for the best - Refactor with a priority stack
            Optional<Edge> edge = graph.edges.get(b).stream().filter(e -> v2.contains(e.end)).sorted().findFirst();

            if(edge.isPresent()){
                // Remove for the list of potential target points.
                v2.remove(edge.get().end);

                graph.edges.get(edge.get().end).forEach(edge1 -> edge1.end.totalEdges--);
                graph.edges.get(b).forEach(edge1 -> edge1.end.totalEdges--);
                result.put(b, edge.get());
            } else {
                System.out.println("Not working here. You should find an edge");
            }
        }

      return result;
    }

    public Video getVideo1() {
        return video1;
    }

    public Object getVideo2() {
        return video2;
    }
}