package org.laeq.model.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.laeq.model.Point;

import java.io.IOException;

public class PointStatisticSerializer extends StdSerializer<Point> {

    public PointStatisticSerializer() {
        this(null);
    }

    public PointStatisticSerializer(Class<Point> t) {
        super(t);
    }

    @Override
    public void serialize(Point point, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeStartObject();
        jgen.writeObjectField("x", point.getX() );
        jgen.writeObjectField("y", point.getY() );
        jgen.writeObjectField("start", point.getStart().toMillis());
        jgen.writeObjectField("category", point.getCategory());
        jgen.writeObjectField("videoId", point.getVideo().getId());
        jgen.writeEndObject();
    }
}