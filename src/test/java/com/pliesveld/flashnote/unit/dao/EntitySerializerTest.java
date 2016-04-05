package com.pliesveld.flashnote.unit.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pliesveld.flashnote.domain.Answer;
import com.pliesveld.flashnote.domain.Question;
import com.pliesveld.flashnote.domain.Student;
import com.pliesveld.flashnote.domain.StudentRole;
import com.pliesveld.flashnote.spring.Profiles;
import com.pliesveld.flashnote.spring.SpringRootConfig;
import com.pliesveld.flashnote.spring.db.H2DataSource;
import com.pliesveld.flashnote.spring.db.PersistenceContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManagerFactory;
import javax.persistence.metamodel.ManagedType;
import javax.persistence.metamodel.Metamodel;
import javax.persistence.metamodel.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class EntitySerializerTest {
    private static final Logger LOG = LogManager.getLogger();

    @Test
    public void findManagedEntities()
    {

        System.setProperty("spring.profiles.active", Profiles.INTEGRATION_TEST);
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();

        LOG.info(StringUtils.arrayToCommaDelimitedString(ctx.getEnvironment().getActiveProfiles()));
        try {
            ctx.register(SpringRootConfig.class);
            ctx.register(TestEntityConfig.class);

            ctx.refresh();
        } catch(PropertyReferenceException pre) {
            fail(pre.getMessage());
            throw pre;
        } catch(BeanCreationException bce) {
            bce.printStackTrace();
            fail(bce.getMessage());
        }

        TestEntityConfig testRepositoryConfig = ctx.getBean(TestEntityConfig.class);
        assertNotNull(testRepositoryConfig.entityManagerFactory);

        Metamodel mm = testRepositoryConfig.entityManagerFactory.getMetamodel();
        Set<ManagedType<?>> managedTypes = mm.getManagedTypes();

        //managedTypes.forEach((itemType) -> LOG.debug("{} : {}",itemType.getPersistenceType(),itemType.getJavaType().getName()));

        List<Class<?>> entityClasses = new ArrayList<>();
        managedTypes.iterator().forEachRemaining((itemType) -> {if (itemType.getPersistenceType().equals(Type.PersistenceType.ENTITY)) {entityClasses.add(itemType.getJavaType());}});

        entityClasses.forEach((entity_class) -> serialize_json_print(entity_class));

    }



    private <C> void serialize_json_print(Class<C> entity_class) {
        C entity = null;
        try {
            entity = entity_class.newInstance();
        } catch (InstantiationException e) {
            LOG.warn("Could not instantiate {}",entity_class.getName());
            return;
        } catch (IllegalAccessException e) {
            LOG.warn("Illegal access when instantiating {}",entity_class.getName());
            e.printStackTrace();
            return;
        }

        switch(entity_class.getSimpleName())
        {
            case "Question":
                Question question = (Question) entity;
                question.setTitle("Title");
                question.setContent("This is a question.");
                question.setId(1);
                break;

            case "Answer":
                Answer answer = (Answer) entity;
                answer.setId(2);
                answer.setContent("This is an answer.");
                break;

            case "Student":
                Student student = (Student) entity;
                student.setId(4);
                student.setEmail("json@example.com");
                student.setPassword("password");
                student.setRole(StudentRole.ROLE_USER);
                break;

            default:
                LOG.info("Skipping {}", entity_class.getName());
                return;
        }

        ObjectMapper mapper = new ObjectMapper();
        try {
            LOG.info(mapper.writerFor(entity_class).writeValueAsString(entity));
        } catch (JsonProcessingException e) {
            LOG.warn("Could not serialize to json " + entity_class.getName());
            e.printStackTrace();
        }


    }
}

@Configuration
@Import(value = {H2DataSource.class,PersistenceContext.class})
class TestEntityConfig {

    @Autowired
    EntityManagerFactory entityManagerFactory;
}

