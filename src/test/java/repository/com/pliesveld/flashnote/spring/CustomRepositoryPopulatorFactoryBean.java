package com.pliesveld.flashnote.spring;

import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.pliesveld.flashnote.domain.base.DomainBaseEntity;
import com.pliesveld.flashnote.logging.Markers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.repository.init.Jackson2RepositoryPopulatorFactoryBean;
import org.springframework.data.repository.init.ResourceReader;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class CustomRepositoryPopulatorFactoryBean extends Jackson2RepositoryPopulatorFactoryBean {
    private static final Logger LOG = LogManager.getLogger();

    protected ObjectMapper objectMapper = new ObjectMapper();

    public CustomRepositoryPopulatorFactoryBean() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.objectMapper.setAnnotationIntrospector(new MyJacksonAnnotationIntrospector());
    }

    @Override
    protected ResourceReader getResourceReader() {
        return new CustomJackson2ResouceReader(objectMapper);
    }

    @Override
    public void setMapper(ObjectMapper mapper) {
        this.objectMapper = mapper;
        Assert.isTrue(!mapper.getDeserializationConfig().hasDeserializationFeatures(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES.getMask()), "Mapper needs to fail on unknown properties");
        super.setMapper(mapper);
    }


    static class MyJacksonAnnotationIntrospector extends JacksonAnnotationIntrospector {
        @Override
        public ObjectIdInfo findObjectIdInfo(final Annotated ann) {
            if (DomainBaseEntity.class.isAssignableFrom(ann.getRawType())) {
                LOG.debug(Markers.OBJECT_MAPPER_INIT, "Checking raw type: {}", ann.getRawType());

                return new ObjectIdInfo(
                        PropertyName.construct("@id", null),
                        null,
                        ObjectIdGenerators.IntSequenceGenerator.class,
                        null);
            }
            return super.findObjectIdInfo(ann);
        }

    }

}
