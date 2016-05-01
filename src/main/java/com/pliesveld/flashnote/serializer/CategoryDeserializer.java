package com.pliesveld.flashnote.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.pliesveld.flashnote.domain.Category;
import com.pliesveld.flashnote.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.IOException;

@Component
public class CategoryDeserializer extends JsonDeserializer<Category> {

    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public Category deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        Assert.isTrue(!jp.isExpectedStartArrayToken());
        Assert.isTrue(!jp.isExpectedStartObjectToken());
        Assert.isTrue(jp.getCurrentToken().isNumeric());
        Integer cat_id = jp.getIntValue();

        return categoryRepository.findOne(cat_id);
    }
}
