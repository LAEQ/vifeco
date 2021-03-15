package org.laeq;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.laeq.model.Preferences;
import org.laeq.settings.Settings;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class PreferencesService {
    private Preferences preferences;
    private final String fileName = "preferences.json";
    private final List<Locale> locales = Arrays.asList(Locale.ENGLISH, Locale.FRENCH, new Locale("es"));

    public PreferencesService() {
//        File file = new File(getFileName());
//        if(file.exists()){
//            try {
//                preferences = new ObjectMapper().readValue(file, Preferences.class);
//            } catch (IOException e) {
//
//            }
//        } else {
//            preferences = new Preferences();
//        }
    }

    public Locale getLocale(int index){
        if(index > 0 && index < locales.size()){
            return locales.get(index);
        }

        return locales.get(0);
    }

    public int getLocaleIndex(Locale locale){
        for (int i = 0; i < locales.size(); i++) {
            String key = locales.get(i).toString();
            if(key.equals(locale.toString())){
                return i;
            }
        }
        return 0;
    }

    private String getFileName(){
        return String.format("%s%s%s", Settings.defaultPath, File.separator, fileName);
    }

    public void export(Preferences preferences){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(new File(getFileName()), preferences);
        } catch (Exception e) {

        }
    }

    public Preferences getPreferences() {
        return preferences;
    }

}