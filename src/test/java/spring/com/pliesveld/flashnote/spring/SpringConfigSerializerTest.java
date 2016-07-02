package com.pliesveld.flashnote.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pliesveld.flashnote.repository.QuestionRepository;
import com.pliesveld.flashnote.serializer.HibernateAwareObjectMapper;
import com.pliesveld.flashnote.spring.serializer.ObjectMapperDebug;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mapping.PropertyReferenceException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class SpringConfigSerializerTest {
    private static final Logger LOG = LogManager.getLogger();

    AnnotationConfigApplicationContext ctx;

    @Before
    public void initializeContext() {

        System.setProperty("spring.profiles.active", Profiles.INTEGRATION_TEST);
        ctx = new AnnotationConfigApplicationContext();
    }


    @Test
    public void testContextBeans_withRegisteringSpringDataConfig()
    {
        ctx.register(SpringSerializationTestConfig.class);
        ctx.register(TestEntitySerializerConfig.class);
        finalizeContext();

        TestEntitySerializerConfig testRepositoryConfig = ctx.getBean(TestEntitySerializerConfig.class);
        assertNotNull(testRepositoryConfig.entityManager);

        if (testRepositoryConfig.hibernateAwareObjectMapper != null)
            ObjectMapperDebug.debug(this, (ObjectMapper) testRepositoryConfig.hibernateAwareObjectMapper);
    }

    @Test
    public void testContext_withCustomObjectMapperBeans()
    {
        ctx.register(SpringSerializationTestConfig.class);
        ctx.register(TestEntitySerializerConfigBeans.class);
        finalizeContext();

        TestEntitySerializerConfigBeans testSpringConfig = ctx.getBean(TestEntitySerializerConfigBeans.class);
        assertNotNull(testSpringConfig.entityManager);
        assertNotNull(testSpringConfig.objectMapperWithSummary);
    }

    private void finalizeContext()
    {
        try {
            ctx.refresh();
        } catch (PropertyReferenceException pre) {
            fail(pre.getMessage());
            throw pre;
        } catch (BeanCreationException bce) {
            bce.printStackTrace();
            fail(bce.getMessage());
        }

    }


}

@Configuration
@Import(value = {SpringEntityTestConfig.class})
class TestEntitySerializerConfig {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    HibernateAwareObjectMapper hibernateAwareObjectMapper;

    @Autowired
    QuestionRepository questionRepository;
}


@Configuration
@Import(value = {SpringEntityTestConfig.class})
class TestEntitySerializerConfigBeans {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    HibernateAwareObjectMapper hibernateAwareObjectMapper;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    ObjectMapper objectMapperWithSummary;

}

