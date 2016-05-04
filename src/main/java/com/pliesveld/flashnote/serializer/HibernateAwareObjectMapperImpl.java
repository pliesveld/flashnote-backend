package com.pliesveld.flashnote.serializer;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.pliesveld.flashnote.logging.Markers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.pliesveld.flashnote.logging.Markers.OBJECT_MAPPER_INIT;

@Component("hibernateAwareObjectMapper")
public class HibernateAwareObjectMapperImpl extends ObjectMapper implements HibernateAwareObjectMapper {
    private static final Logger LOG = LogManager.getLogger();

    private Hibernate5Module jacksonHibernateModule;
    private Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder;

    protected HibernateAwareObjectMapperImpl(ObjectMapper src) {
        super(src);
        LOG.debug(OBJECT_MAPPER_INIT, "Initializing {}", this);
    }

    @Autowired
    public HibernateAwareObjectMapperImpl(Hibernate5Module jacksonHibernateModule, Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder) {
        super((ObjectMapper) jackson2ObjectMapperBuilder.build());

        if(LOG.isDebugEnabled(OBJECT_MAPPER_INIT))
            for(Hibernate5Module.Feature feature : Hibernate5Module.Feature.values())
                LOG.debug(OBJECT_MAPPER_INIT, "{} = {}", feature, jacksonHibernateModule.isEnabled(feature));

        registerModule(jacksonHibernateModule);

        setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);

//        enable(SerializationFeature.USE_EQUALITY_FOR_OBJECT_ID);


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
        LOG.debug(Markers.OBJECT_MAPPER_WRITE, "OBJECT_MAPPER {} : {}",this, value.getClass().getName());
        return super.writeValueAsString(value);
    }

    @Override
    public <T> T readValue(JsonParser jp, Class<T> valueType) throws IOException {
        LOG.debug(Markers.OBJECT_MAPPER_READ, "OBJECT_MAPPER {} : {}",this, valueType.getName());
        return super.readValue(jp, valueType);
    }
}
