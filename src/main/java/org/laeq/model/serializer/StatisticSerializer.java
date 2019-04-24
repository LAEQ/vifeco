package org.laeq.model.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.laeq.service.statistic.StatisticService;

import java.io.IOException;

public class StatisticSerializer extends StdSerializer<StatisticService> {

    public StatisticSerializer() {
        this(null);
    }

    public StatisticSerializer(Class<StatisticService> t) {
        super(t);
    }

    @Override
    public void serialize(StatisticService service, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {

        jgen.writeStartObject();
        jgen.writeObjectFieldStart("videos");
        jgen.writeObjectField("video_1", service.getVideo1());
        jgen.writeObjectField("video_2", service.getVideo2());
        jgen.writeEndObject();
        jgen.writeObjectField("tarjan_diff", service.getTarjanDiffs());
        jgen.writeObjectField("tarjan_edge", service.getTarjanEdges());
        jgen.writeObjectField("lonely_points", service.getLonelyPoints());

        jgen.writeEndObject();
    }
}