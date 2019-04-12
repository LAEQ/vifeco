package org.laeq.settings;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.laeq.model.serializer.MessageDeserializer;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@JsonDeserialize(using = MessageDeserializer.class)
public class Message {
    public String key;
    public Map<Locale, String> values = new HashMap<>();

    public Message(){}

    public void addMessage(String local, String message){
        values.put(Locale.forLanguageTag(local), message);
    }

    public String getMessage(Locale locale){
        return values.get(locale);
    }
}
