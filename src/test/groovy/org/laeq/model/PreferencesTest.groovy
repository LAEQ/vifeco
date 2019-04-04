package org.laeq.model

import com.fasterxml.jackson.databind.ObjectMapper
import spock.lang.Specification

class PreferencesTest extends Specification {
    def "serialization" (){
        setup:
        Preferences preferences = new Preferences()

        when:
        String result = new ObjectMapper().writeValueAsString(preferences)

        String expected = '{"rate":1.0,"volume":1.0,"size":60.0,"duration":5.0,"opacity":0.65,"locale":"fr_CA"}'

        then:
        result == expected
    }

    def "deserialize with no default language" () {
        setup:
        String json = '{"rate":1.0,"volume":1.0,"size":60.0,"duration":5.0,"opacity":0.65,"locale":"fr_CA"}'

        ObjectMapper mapper = new ObjectMapper()

        when:
        Preferences result = mapper.readValue(json, Preferences.class)

        then:
        result.rate == 1.0
        result.volume == 1.0
        result.size == 60.0
        result.duration == 5.0
        result.opacity == 0.65
        result.locale == Locale.CANADA_FRENCH
    }

    def "deserialize with english" () {
        setup:
        String json = '{"rate":1.0,"volume":1.0,"size":60.0,"duration":5.0,"opacity":0.65,"locale":"en_CA"}'

        ObjectMapper mapper = new ObjectMapper()

        when:
        Preferences result = mapper.readValue(json, Preferences.class)

        then:
        result.rate == 1.0
        result.volume == 1.0
        result.size == 60.0
        result.duration == 5.0
        result.opacity == 0.65
        result.locale == new Locale("en", "CA")
    }

    def "deserialize with french canadian" () {
        setup:
        String json = '{"rate":1.0,"volume":1.0,"size":60.0,"duration":5.0,"opacity":0.65,"locale":"fr_CA"}'

        ObjectMapper mapper = new ObjectMapper()

        when:
        Preferences result = mapper.readValue(json, Preferences.class)

        then:
        result.rate == 1.0
        result.volume == 1.0
        result.size == 60.0
        result.duration == 5.0
        result.opacity == 0.65
        result.locale == Locale.CANADA_FRENCH
    }
}
