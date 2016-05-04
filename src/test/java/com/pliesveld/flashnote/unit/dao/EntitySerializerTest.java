package com.pliesveld.flashnote.unit.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pliesveld.flashnote.repository.QuestionRepository;
import com.pliesveld.flashnote.serializer.HibernateAwareObjectMapper;
import com.pliesveld.flashnote.spring.Profiles;
import com.pliesveld.flashnote.spring.serializer.ObjectMapperDebug;
import com.pliesveld.flashnote.unit.spring.SpringEntityTestConfig;
import com.pliesveld.flashnote.unit.spring.SpringSerializationTestConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mapping.PropertyReferenceException;

import javax.persistence.EntityManager;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class EntitySerializerTest {
    private static final Logger LOG = LogManager.getLogger();

    AnnotationConfigApplicationContext ctx;

    @Before
    public void initializeContext() {

        System.setProperty("spring.profiles.active", Profiles.INTEGRATION_TEST);

        ctx = new AnnotationConfigApplicationContext();

        ctx.register(SpringSerializationTestConfig.class);
        ctx.register(TestEntityConfig.class);

    }

    @Test
    public void testContext()
    {

    }

    @Test
    public void testContext_withRegisteringSpringDataConfig()
    {
        //ctx.register(SpringDataConfig.class);
    }

    @After
    public void finalizeContext()
    {
        try {
            ctx.refresh();
        } catch(PropertyReferenceException pre) {
            fail(pre.getMessage());
            throw pre;
        } catch(BeanCreationException bce) {
            bce.printStackTrace();
            fail(bce.getMessage());
        }

        TestEntityConfig testRepositoryConfig = ctx.getBean(TestEntityConfig.class);
        assertNotNull(testRepositoryConfig.entityManager);

        if(testRepositoryConfig.hibernateAwareObjectMapper != null)
            ObjectMapperDebug.debug(this, (ObjectMapper) testRepositoryConfig.hibernateAwareObjectMapper);
    }


}

@Configuration
@Import(value = {SpringEntityTestConfig.class})
class TestEntityConfig {

    @javax.persistence.PersistenceContext
    EntityManager entityManager;

    @Autowired
    HibernateAwareObjectMapper hibernateAwareObjectMapper;

    @Autowired
    QuestionRepository questionRepository;
}

