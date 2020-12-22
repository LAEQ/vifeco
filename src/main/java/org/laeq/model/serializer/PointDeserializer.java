package org.laeq.model.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import javafx.util.Duration;
import org.laeq.model.Category;
import org.laeq.model.Point;

import java.io.IOException;

public class PointDeserializer extends StdDeserializer<Point> {

    public PointDeserializer(){
        this(null);
    }

    public PointDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Point deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);

        int id = (int) node.get("id").numberValue();
        int categoryId = (Integer) node.get("categoryId").numberValue();
        double start = (double) node.get("startDouble").numberValue();
        double x = (double) node.get("x").numberValue();
        double y = (double) node.get("y").numberValue();

        Category category = new Category();
        category.setId(categoryId);

        Point point = new Point();
//        point.setId(id);
//        point.setCategory(category);
//        point.setStart(Duration.millis(start));
//        point.setX(x);
//        point.setY(y);

        return point;
    }
}
