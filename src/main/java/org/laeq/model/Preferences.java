package org.laeq.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@JsonPropertyOrder({"rate", "volume", "size", "duration", "opacity", "locale"})
@JsonIgnoreProperties({"localAvailables"})
public class Preferences {
    public  Double rate = 1d;
    public  Double volume = 1d;
    public  Double size = 60d;
    public  Double duration = 5d;
    public  Double opacity = 0.65;
    public  Locale locale = Locale.CANADA_FRENCH;

    public List<Locale> localAvailables;

    public Preferences(){
        localAvailables = new ArrayList<>();
        localAvailables.add(Locale.CANADA_FRENCH);
        localAvailables.add(new Locale("en", "CA"));
        localAvailables.add(new Locale("es", "ES"));
    }

    @JsonIgnore
    public int getLocalIndex(){
        return localAvailables.indexOf(locale);
    }

    @JsonIgnore
    public void setLocaleByIndex(int index){
        locale = localAvailables.get(index);
    }

    public List<String> getLocales(){
        return localAvailables.stream().map(locale1 -> locale1.getDisplayLanguage(locale1)).collect(Collectors.toList());
    }
}
