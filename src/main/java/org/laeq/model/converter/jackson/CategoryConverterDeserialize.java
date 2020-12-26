package org.laeq.model.converter.jackson;

import com.fasterxml.jackson.databind.util.StdConverter;
import org.laeq.model.Category;

public class CategoryConverterDeserialize extends StdConverter<Integer, Category> {
    @Override
    public Category convert(Integer value) {
        return new Category(value);
    }
}
