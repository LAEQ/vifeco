package org.laeq.model.converter.jackson;

import com.fasterxml.jackson.databind.util.StdConverter;
import javafx.util.Duration;

public class DurationToMilliConverter extends StdConverter<Duration, Double> {
    @Override
    public Double convert(Duration value) {
        return value.toMillis();
    }
}
