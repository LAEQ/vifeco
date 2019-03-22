package org.laeq.service.statistic;

import griffon.core.artifact.GriffonService;
import griffon.metadata.ArtifactProviderFor;
import javafx.util.Duration;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonService;
import org.laeq.model.Category;
import org.laeq.model.statistic.Edge;
import org.laeq.model.statistic.Graph;
import org.laeq.model.Point;
import org.laeq.model.Video;
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

    public void calculate(){
        List<List<Vertex>> result = new ArrayList<>();
        Category category = new Category();
        category.setId(1);
        tarjan(graphs.get(category));
    }

    public List<List<Vertex>> tarjan(Graph graph){
        graph.edges.keySet().stream().forEach(vertex -> vertex.num = -1);

        int counter = 0;

        Stack<Vertex> stack = new Stack<>();
        List<List<Vertex>> result = new ArrayList<>();

        graph.edges.keySet().forEach(vertex -> {
            if(vertex.num == -1){
                visit(graph, vertex, counter, stack,  result);
            }
        });

        return result;
    }

    private void visit(Graph graph, Vertex vertex, int counter, Stack<Vertex> stack, List<List<Vertex>> result) {
        vertex.num = counter++;
        vertex.min = vertex.num;

        stack.push(vertex);

        for (Edge edge: graph.edges.get(vertex)) {
            if(edge.end.num == -1){
                visit(graph, edge.end, counter, stack, result);
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

//1.  TARJAN (G = (V, E))
//2.  pour tout v ∈ V
//3.    v.num ← −1
//4.  compteur ← 0
//5.  chemin ← Pile vide
//6.  R ← Partition vide
//7.  pour tout v ∈ V
//8.  si v.num = −1
//9.  PARCOURS P ROFONDEUR (v)
//10. retourner R
//11. PARCOURS P ROFONDEUR (v)
//12. v.num ← compteur + +
//13. v.min ← v.num
//14. chemin.empiler(v)
//15. pour tout arête sortante (v, w)
//16. si w.num = −1
//17. PARCOURS P ROFONDEUR (w)
//18. v.min ← min(v.min, w.min)
//19. sinon si w ∈ chemin
//20. v.min ← min(v.min, w.num)
//21. si v.num = v.min
//22. C ← {}
//23  répéter
//24. w ← chemin. DEPILER
//25. C.ajouter(w)
//26. tant que v 6 = w
//27. R ← R ∪ C