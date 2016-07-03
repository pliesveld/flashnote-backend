package com.pliesveld.populator.repository.reader;

import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.pliesveld.flashnote.domain.base.DomainBaseEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.data.repository.init.Jackson2RepositoryPopulatorFactoryBean;
import org.springframework.data.repository.init.ResourceReader;
import org.springframework.data.repository.init.ResourceReaderRepositoryPopulator;
import org.springframework.util.Assert;

public class CustomRepositoryPopulatorFactoryBean extends Jackson2RepositoryPopulatorFactoryBean implements InitializingBean {
    private static final Logger LOG = LogManager.getLogger();
    private Resource[] recources;

    protected ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void setResources(Resource[] resources) {
        this.recources = resources.clone();
        super.setResources(resources);
    }

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
        private static final long serialVersionUID = -2988954748731792197L;

        @Override
        public ObjectIdInfo findObjectIdInfo(final Annotated ann) {
            if (DomainBaseEntity.class.isAssignableFrom(ann.getRawType())) {
//                LOG.debug(Markers.OBJECT_MAPPER_INIT, "Checking raw type: {}", ann.getRawType());

                return new ObjectIdInfo(
                        PropertyName.construct("@id", null),
                        null,
                        ObjectIdGenerators.IntSequenceGenerator.class,
                        null);
            }
            return super.findObjectIdInfo(ann);
        }
    }


    @Override
    protected ResourceReaderRepositoryPopulator createInstance() {
        ResourceReaderRepositoryPopulator populator = super.createInstance();
        populator.setResources(recources);
        return populator;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
    }

//
//    @Override
//    public void onApplicationEvent(ContextRefreshedEvent event) {
//        super.onApplicationEvent(event);
//    }
//


//    @EventListener
//    public void repositoryEvent(RepositoriesPopulatedEvent event)
//    {
//        LOG.error("got event");
//    }
}
