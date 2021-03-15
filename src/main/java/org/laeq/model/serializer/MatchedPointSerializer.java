package org.laeq.model.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.laeq.model.statistic.MatchedPoint;

import java.io.IOException;

public class MatchedPointSerializer extends StdSerializer<MatchedPoint> {

    public MatchedPointSerializer() {
        this(null);
    }

    public MatchedPointSerializer(Class<MatchedPoint> t) {
        super(t);
    }

    @Override
    public void serialize(MatchedPoint video, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeStartObject();
//        jgen.writeStringField("name", video.pathToName());
//        jgen.writeObjectField("user", video.getUser());
//        jgen.writeObjectField("collection", video.getCollection());
//        jgen.writeStringField("uuid", video.getId().toString());
//        jgen.writeNumberField("total", video.getPoints().size());
//        jgen.writeObjectField("total_category", video.getCategoryCount());
        jgen.writeEndObject();
    }
}