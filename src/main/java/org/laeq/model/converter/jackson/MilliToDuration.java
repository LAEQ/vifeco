package org.laeq.model.converter.jackson;


import com.fasterxml.jackson.databind.util.StdConverter;
import javafx.util.Duration;

public class MilliToDuration extends StdConverter<Double, Duration> {
    @Override
    public Duration convert(Double value) {
        return Duration.millis(value);
    }
}
