package org.laeq;

import com.fasterxml.jackson.databind.ObjectMapper;
import griffon.core.artifact.GriffonService;
import griffon.metadata.ArtifactProviderFor;
import org.apache.commons.io.IOUtils;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonService;
import org.laeq.settings.Message;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@javax.inject.Singleton
@ArtifactProviderFor(GriffonService.class)
public class TranslationService extends AbstractGriffonService {
    private File file;
    private String json;
    private final Message[] messages;
    private final Map<String, String> values;
    private Locale locale;

    public TranslationService(){
        messages = new Message[0];
        values = new HashMap<>();
    }

    public TranslationService(File file, Locale locale) throws IOException {
        this.locale = locale;
        this.file = file;
        this.json = IOUtils.toString(new FileInputStream(file), "UTF-8");

        ObjectMapper mapper = new ObjectMapper();

        messages = mapper.readValue(this.json, Message[].class);

        values = new HashMap<>();

        for (int i = 0; i < messages.length; i++) {
            String message = messages[i].getMessage(locale);
            if(message != null){
                values.put(messages[i].key, message);
            }

        }
    }

    public TranslationService(InputStream file, Locale locale) throws IOException {
        this.locale = locale;
        this.json = IOUtils.toString(file, "UTF-8");

        ObjectMapper mapper = new ObjectMapper();

        messages = mapper.readValue(this.json, Message[].class);

        values = new HashMap<>();

        for (int i = 0; i < messages.length; i++) {
            String message = messages[i].getMessage(locale);
            if(message != null){
                values.put(messages[i].key, message);
            }

        }
    }



    public String getMessage(String key){
        if(values.containsKey(key)){
            return values.get(key);
        }

        return String.format("Missing %s for %s", key, locale);
    }
}