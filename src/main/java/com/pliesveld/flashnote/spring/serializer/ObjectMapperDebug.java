package com.pliesveld.flashnote.spring.serializer;

import com.fasterxml.jackson.databind.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.pliesveld.flashnote.logging.Markers.OBJECT_MAPPER_INIT;

public class ObjectMapperDebug {
    private static final Logger LOG = LogManager.getLogger();

    public static <T> void debug(T obj, ObjectMapper objectMapper) {
        debug(obj.getClass(), objectMapper);
    }

    public static void debug(Class<?> clazz, ObjectMapper objectMapper) {

        SerializationConfig serializationConfig = objectMapper.getSerializationConfig();
        DeserializationConfig deserializationConfig = objectMapper.getDeserializationConfig();
//
//        objectMapper.addHandler(new DeserializationProblemHandler() {
//            @Override
//            public boolean handleUnknownProperty(DeserializationContext ctxt, JsonParser jp, JsonDeserializer<?> deserializer, Object beanOrClass, String propertyName) throws IOException, JsonProcessingException {
//                LOG.error(OBJECT_MAPPER_ERROR, "class {} prop {}", beanOrClass, propertyName);
//                return super.handleUnknownProperty(ctxt, jp, deserializer, beanOrClass, propertyName);
//            }
//        });
//
        if (LOG.isDebugEnabled(OBJECT_MAPPER_INIT)) {
            LOG.debug(OBJECT_MAPPER_INIT, "{} has a configured ObjectMapper {}", clazz.getSimpleName(), objectMapper);
            LOG.debug(OBJECT_MAPPER_INIT, "ObjectMapper serialization configuration");
            LOG.debug(OBJECT_MAPPER_INIT, "active view: {}", serializationConfig.getActiveView());
            LOG.debug(OBJECT_MAPPER_INIT, "Serialization Inclusion = {}", serializationConfig.getSerializationInclusion());

            for (SerializationFeature feature : SerializationFeature.class.getEnumConstants()) {
                boolean enabled = serializationConfig.hasSerializationFeatures(feature.getMask());
                if (feature.enabledByDefault() != enabled) {
                    LOG.debug(OBJECT_MAPPER_INIT, "{} = {}", feature, enabled);
                }
            }

            for (MapperFeature feature : MapperFeature.class.getEnumConstants()) {
                boolean enabled = serializationConfig.hasMapperFeatures(feature.getMask());
                if (feature.enabledByDefault() != enabled) {
                    LOG.debug(OBJECT_MAPPER_INIT, "{} = {}", feature, enabled);
                }
            }

            if (serializationConfig.mixInCount() > 0)
                LOG.debug(OBJECT_MAPPER_INIT, "mixIn {} : ", serializationConfig.mixInCount());


            LOG.debug(OBJECT_MAPPER_INIT, "ObjectMapper deserialization configuration");
            LOG.debug(OBJECT_MAPPER_INIT, "ACTIVE VIEW: {}", deserializationConfig.getActiveView());

            for (DeserializationFeature feature : DeserializationFeature.class.getEnumConstants()) {
                boolean enabled = deserializationConfig.hasDeserializationFeatures(feature.getMask());
                if (feature.enabledByDefault() != enabled) {
                    LOG.debug(OBJECT_MAPPER_INIT, "{} = {}", feature, enabled);
                }
            }

            for (MapperFeature feature : MapperFeature.class.getEnumConstants()) {
                boolean enabled = deserializationConfig.hasMapperFeatures(feature.getMask());
                if (feature.enabledByDefault() != enabled) {
                    LOG.debug(OBJECT_MAPPER_INIT, "{} = {}", feature, enabled);
                }
            }

            if (deserializationConfig.mixInCount() > 0)
                LOG.debug(OBJECT_MAPPER_INIT, "mixIn {} : ", deserializationConfig.mixInCount());
        }

    }
}
