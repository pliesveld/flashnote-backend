package com.pliesveld.flashnote.serializer;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.pliesveld.flashnote.logging.Markers;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

import static com.pliesveld.flashnote.logging.Markers.OBJECT_MAPPER_INIT;

@Component("hibernateAwareObjectMapper")
public class HibernateAwareObjectMapperImpl extends ObjectMapper implements HibernateAwareObjectMapper {
    private static final Logger LOG = LogManager.getLogger();


    protected HibernateAwareObjectMapperImpl(ObjectMapper src) {
        super(src);
        LOG.debug(OBJECT_MAPPER_INIT, "Initializing {}", this);
    }

    @Autowired
    public HibernateAwareObjectMapperImpl(Hibernate5Module jacksonHibernateModule, Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder) {
        super((ObjectMapper) jackson2ObjectMapperBuilder.build());


        LOG.debug(OBJECT_MAPPER_INIT, "Active View : {}", getSerializationConfig().getActiveView());


        for(Hibernate5Module.Feature feature : Hibernate5Module.Feature.values())
            LOG.debug(OBJECT_MAPPER_INIT, "{} = {}", feature, jacksonHibernateModule.isEnabled(feature));

        registerModule(jacksonHibernateModule);

        setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);

        configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
//        configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false);

//        may cause lazy-loading
//        enable(SerializationFeature.USE_EQUALITY_FOR_OBJECT_ID);
        LOG.debug(OBJECT_MAPPER_INIT, "Registered modules {}", ArrayUtils.toString(_registeredModuleTypes));
    }

    @Override
    public ObjectMapper copy() {
        return new HibernateAwareObjectMapperImpl(this);
    }

    @Override
    @Autowired
    public Object setHandlerInstantiator(HandlerInstantiator hi) {
        return super.setHandlerInstantiator(hi);
    }

    @Override
    public String writeValueAsString(Object value) throws JsonProcessingException {
        LOG.trace(Markers.OBJECT_MAPPER_WRITE, "OBJECT_MAPPER1 {} : {}",this, value.getClass().getName());
        return super.writeValueAsString(value);
    }

    @Override
    public void writeTree(JsonGenerator jgen, JsonNode rootNode) throws IOException, JsonProcessingException {
        LOG.trace(Markers.OBJECT_MAPPER_WRITE, "OBJECT_MAPPER2 {} : ",this);
        super.writeTree(jgen, rootNode);
    }

    @Override
    public void writeTree(JsonGenerator jgen, TreeNode rootNode) throws IOException, JsonProcessingException {
        LOG.trace(Markers.OBJECT_MAPPER_WRITE, "OBJECT_MAPPER3 {} : ",this);
        super.writeTree(jgen, rootNode);
    }

    @Override
    public void writeValue(JsonGenerator g, Object value) throws IOException, JsonGenerationException, JsonMappingException {
        LOG.trace(Markers.OBJECT_MAPPER_WRITE, "OBJECT_MAPPER4 {} : {}",this, value.getClass().getName());
        super.writeValue(g, value);
    }

    @Override
    public void writeValue(OutputStream out, Object value) throws IOException, JsonGenerationException, JsonMappingException {
        LOG.trace(Markers.OBJECT_MAPPER_WRITE, "OBJECT_MAPPER5 {} : {}",this, value.getClass().getName());
        super.writeValue(out, value);
    }

    @Override
    public void writeValue(Writer w, Object value) throws IOException, JsonGenerationException, JsonMappingException {
        LOG.trace(Markers.OBJECT_MAPPER_WRITE, "OBJECT_MAPPER6 {} : {}",this, value.getClass().getName());
        super.writeValue(w, value);
    }

    @Override
    public <T> T readValue(JsonParser jp, Class<T> valueType) throws IOException {
        LOG.trace(Markers.OBJECT_MAPPER_READ, "OBJECT_MAPPER7 {} : {}",this, valueType.getName());
        return super.readValue(jp, valueType);
    }
}
