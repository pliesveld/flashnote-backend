package com.pliesveld.flashnote.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.pliesveld.flashnote.domain.base.DomainBaseEntity;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class DomainObjectSerializer extends JsonSerializer<DomainBaseEntity<Integer>> {

    @Override
    public void serialize(DomainBaseEntity<Integer> value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
        gen.writeNumber(value.getId());
    }
}