package org.laeq.model.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.laeq.settings.Message;

import java.io.IOException;
import java.util.Iterator;

public class MessageDeserializer extends StdDeserializer<Message> {

    public MessageDeserializer(){
        this(null);
    }

    public MessageDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Message deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode node = p.getCodec().readTree(p);

        String key = node.fieldNames().next();

        JsonNode subNode = node.get(key);

        Message message = new Message();
        message.key = key;

        Iterator<String> it = subNode.fieldNames();

        while(it.hasNext()){
            String lang = it.next();
            message.addMessage(lang, subNode.get(lang).textValue());
        }


        return message;
    }
}
