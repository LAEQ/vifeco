package org.laeq.model.converter;

import javafx.util.Duration;
import javax.persistence.AttributeConverter;

public class DurationConverter implements AttributeConverter<Duration, Double> {
    @Override
    public Double convertToDatabaseColumn(Duration attribute) {
        return attribute.toMillis();
    }

    @Override
    public Duration convertToEntityAttribute(Double dbData) {
        return Duration.millis(dbData);
    }
}
