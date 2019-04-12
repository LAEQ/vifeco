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




//        jgen.writeStartObject();
//        jgen.writeNumberField("id", value.id);
//        jgen.writeStringField("itemName", value.itemName);
//        jgen.writeNumberField("owner", value.owner.id);
//        jgen.writeEndObject();
    }
}