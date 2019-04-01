package org.laeq.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import griffon.core.artifact.GriffonService;
import griffon.metadata.ArtifactProviderFor;
import org.codehaus.griffon.runtime.core.artifact.AbstractGriffonService;
import org.laeq.model.Preferences;
import org.laeq.settings.Settings;

import java.io.File;
import java.io.IOException;

@javax.inject.Singleton
@ArtifactProviderFor(GriffonService.class)
public class PreferencesService extends AbstractGriffonService {
    private Preferences preferences;
    private String fileName = "preferences.json";

    public PreferencesService() throws IOException {
        File file = new File(getFileName());

        if(file.exists()){
            preferences = new ObjectMapper().readValue(file, Preferences.class);
        } else {
            preferences = new Preferences();
            export(preferences);
        }
    }

    private String getFileName(){
        return String.format("%s%s%s", Settings.defaultPath, File.separator, fileName);
    }

    public void export(Preferences preferences){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(new File(getFileName()), preferences);
        } catch (Exception e) {
            getLog().error(e.getMessage());
        }
    }

    public Preferences getPreferences() {
        return preferences;
    }
}