package org.laeq.model.converter.jackson;

import com.fasterxml.jackson.databind.util.StdConverter;
import java.nio.file.Paths;


public class PathConverterSerialize extends StdConverter<String, String> {
    @Override
    public String convert(String value) {
        return Paths.get(value).getFileName().toString();
    }
}
