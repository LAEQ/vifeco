package org.laeq.model.converter.jackson;

import com.fasterxml.jackson.databind.util.StdConverter;
import org.laeq.model.Category;

public class CategoryConverterSerialize extends StdConverter<Category, Integer> {
    @Override
    public Integer convert(Category value) {
        return value.getId();
    }
}
