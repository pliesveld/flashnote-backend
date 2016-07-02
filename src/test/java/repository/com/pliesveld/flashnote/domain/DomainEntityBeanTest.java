package com.pliesveld.flashnote.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pliesveld.flashnote.domain.base.BaseEntity;
import com.pliesveld.flashnote.domain.base.DomainBaseEntity;
import com.pliesveld.flashnote.repository.QuestionRepository;
import com.pliesveld.flashnote.serializer.HibernateAwareObjectMapper;
import com.pliesveld.flashnote.spring.Profiles;
import com.pliesveld.flashnote.spring.SpringEntityTestConfig;
import com.pliesveld.tests.beans.DomainEntities;
import com.pliesveld.tests.entitymanager.EMOP;
import com.pliesveld.tests.entitymanager.EMOP_ASSERT;
import com.pliesveld.tests.entitymanager.EMOP_ENTITY;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

@Component
public class DomainEntityBeanTest {
    private final static String LOG_TAG = "LOG_SQL_LEVEL";
    private static final Logger LOG = LogManager.getLogger("org.hibernate.SQL");

    public static void disableSQL() {
        System.setProperty(LOG_TAG, "ERROR");
        ((org.apache.logging.log4j.core.LoggerContext) LogManager.getContext(false)).reconfigure();
    }

    public static void enableSQL() {
        System.setProperty(LOG_TAG, "DEBUG");
        ((org.apache.logging.log4j.core.LoggerContext) LogManager.getContext(false)).reconfigure();
    }

    @Ignore("dirties context")
    @Test
    public void initializeContext() {
        System.setProperty("spring.profiles.active", Profiles.INTEGRATION_TEST);

        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();

        ctx.register(TestEntityRepositoryConfig.class);
        ctx.register(DomainEntities.class);
        ctx.register(TestAnnotationTransactionalAware.class);

        try {
            ctx.refresh();
        } catch (PropertyReferenceException pre) {
            fail(pre.getMessage());
            throw pre;
        } catch (BeanCreationException bce) {
            bce.printStackTrace();
            fail(bce.getMessage());
        }
//
//        for (String names : ctx.getBeanDefinitionNames()) {
//            System.out.println(names);
//        }

        TestEntityRepositoryConfig testRepositoryConfig = ctx.getBean(TestEntityRepositoryConfig.class);
        assertNotNull(testRepositoryConfig);
        assertNotNull(testRepositoryConfig.hibernateAwareObjectMapper);
        assertNotNull(testRepositoryConfig.questionRepository);

        TestAnnotationTransactionalAware testAnnotationTransactionalAware = ctx.getBean(TestAnnotationTransactionalAware.class);
        assertNotNull(testAnnotationTransactionalAware);


//

//        enableSQL();
//        for (String beanName : ctx.getBeanDefinitionNames()) {
//            LOG.debug(beanName);
//        }
//
//        EntityManagerFactory entityManagerFactory = (EntityManagerFactory) ctx.getBean("entityManagerFactory");
//        List<? extends Class<?>> entityClassList = entityManagerFactory.getMetamodel ().getEntities().parallelStream().map(p -> p.getJavaType()).collect(Collectors.toList());
//
//        ArrayList<DomainBaseEntity> entityBeans = new ArrayList<>(entityClassList.size());
//
//        enableSQL();
//        for (Class<?> clazz : entityClassList)
//        {
//            try {
//                Object obj = ctx.getBean(clazz);
//
//
//                if (DomainBaseEntity.class.isAssignableFrom(clazz))
//                {
//                    DomainBaseEntity dbe = (DomainBaseEntity) obj;
//                    entityBeans.add(dbe);
//                    LOG.debug("Got bean of class {}", clazz);
//                } else {
//                    LOG.warn("Got bean; but class {} could not be assigned to DomainBaseEntity.class", clazz);
//                }
//
//            } catch (Exception e) {
//                LOG.debug("Couldn't load bean of class {}",clazz);
//
//            }
//
//        }
//
        final ObjectWriter write = new ObjectWriter(((ObjectMapper) testRepositoryConfig.hibernateAwareObjectMapper).copy());
//
//        for (final DomainBaseEntity obj : entityBeans)
//        {
//            try {
//                LOG.debug("Persisting {}", obj.getClass().getName());
//                final BaseEntity domain =
//                        testAnnotationAware.doInTransaction(
//                                () -> { return obj; },
//                                EMOP_ENTITY.PERSIST,
//                                (entity) -> {
//                                    write.out(entity);
//                                },
//                                EMOP_ASSERT.CONTAINS_ENTITY_TRUE
//
//                        );
//                write.out(domain);
//
//            } catch (PersistenceException | IllegalStateException e) {
//                LOG.warn("Error persisting {} = {} {}", obj.getClass().getName(), e.getMessage(), e.getCause().getMessage());
//            }
//        }
//
//        disableSQL();
//
//
//    }


        final BaseEntity domain =
                testAnnotationTransactionalAware.doInTransaction(
                        () -> {
                            return ctx.getBean(QuestionBank.class);
                        },
                        EMOP_ENTITY.PERSIST,
                        (entity) -> {
                            write.out(entity);
                        },
                        EMOP_ASSERT.CONTAINS_ENTITY_TRUE

                );

//            ((Question) domain).setId(((int)domain.getId()) + 1024*24);
        System.out.println(write.out(domain));

        final Serializable id = domain.getId();

    }

//        assertNotNull(id);
//
//        final BaseEntity domain2 = testAnnotationAware.doInTransaction(
//                () -> {
//                    Question question = new Question();
//                    question.setId((Integer) id);
//                    question.setTitle("UPDATING ORIGINAL QUESTION WITH THIS TITLE");
//                    return question;
//                },
//                EMOP_ENTITY.MERGE,
//                (entity) -> {
//                    write.out(entity);
//                },
//                EMOP_ASSERT.CONTAINS_ENTITY_FALSE
//                );
//        write.out(domain2);
//
//        final BaseEntity domain3 = testAnnotationAware.doInTransaction(
//                () -> {
//                    return new Question();
//                },
//                EMOP_ENTITY.PERSIST,
//
//                (entity) -> {
//                    ((Question) entity).setContent("Setting content of object marked as persist");
//                    write.out(entity);
//                },
//                EMOP_ASSERT.CONTAINS_ENTITY_TRUE
//        );
//        final Integer domain3_id = (Integer) domain3.getId();
//
//        final BaseEntity domain4 = testAnnotationAware.doInTransaction(
//                (em) -> {
//                    return em.getReference(Question.class, domain3_id);
//                },
//
//                EMOP_ENTITY.DETACH,
//
//                (entity) -> {
////                    write.out( ((Question) entity).getContent());
//                },
//                EMOP_ASSERT.LOADED_FALSE
//        );
//        write.out(domain4);


    static public class ObjectWriter<T extends ObjectMapper> {
        private static final Logger LOG = LogManager.getLogger("org.hibernate.SQL");
        private ObjectMapper objectMapper;

        public <T extends ObjectMapper> ObjectWriter(T objectMapper) {
            this.objectMapper = (T) objectMapper;
        }

        public String out(Object obj) {
            try {
                String out = this.objectMapper.writeValueAsString(obj);
                LOG.debug(out);
                return out;
            } catch (JsonProcessingException e) {
                throw new IllegalArgumentException("Could not serialize");
            }
        }
    }


}

@Configuration
@Import(value = {SpringEntityTestConfig.class})
class TestEntityRepositoryConfig {

//    @Autowired(required = false)
//    Question question;
//
//    @Autowired(required = false)
//    Deck deck;

    @Autowired(required = false)
    HibernateAwareObjectMapper hibernateAwareObjectMapper;

    @Autowired(required = false)
    QuestionRepository questionRepository;

}

@Component
class TestAnnotationTransactionalAware {
    private static final Logger LOG = LogManager.getLogger("org.hibernate.SQL");

    @PersistenceContext
    EntityManager entityManager;

    private static final AtomicInteger cnt = new AtomicInteger();

//    @Transactional
//    public DomainBaseEntity doInTransaction(
//            Supplier<DomainBaseEntity> supplier,
//            EMOP emop,
//            Consumer<DomainBaseEntity> consumer)
//    {
//
//        LOG.debug("\nbegin Transaction context #{}\n", cnt.incrementAndGet());
//        assertEntityManager();
//
//        DomainBaseEntity entity = supplier.get();
//        emop.apply(entityManager,entity);
//        consumer.accept(entity);
//
//        LOG.debug("\nend Transaction test #{}\n", cnt.get());
//        return entity;
//    }

    @Transactional
    public DomainBaseEntity doInTransaction(
            Supplier<DomainBaseEntity> supplier,
            EMOP emop_first,
            Consumer<DomainBaseEntity> consumer,
            EMOP emop
    ) {
        LOG.debug("begin Transaction context #{}", cnt.incrementAndGet());
        assertEntityManager();

        DomainBaseEntity entity = supplier.get();
        emop_first.apply(entityManager, entity);
        consumer.accept(entity);
        emop.apply(entityManager, entity);

        LOG.debug("end Transaction test #{}", cnt.get());
        return entity;
    }

    @Transactional
    public DomainBaseEntity doInTransaction(
            Function<EntityManager, DomainBaseEntity> supplier,
            EMOP emop_first,
            Consumer<DomainBaseEntity> consumer,
            EMOP emop
    ) {
        LOG.debug("begin Transaction context #{}", cnt.incrementAndGet());
        assertEntityManager();

        DomainBaseEntity entity = supplier.apply(entityManager);
        emop_first.apply(entityManager, entity);
        consumer.accept(entity);
        emop.apply(entityManager, entity);

        LOG.debug("end Transaction test #{}", cnt.get());
        return entity;
    }


    private void assertEntityManager() {
        assertNotNull("EntityManager not wired", entityManager);
        assertNotNull("EntityManagerFactory not open", entityManager.getEntityManagerFactory().isOpen());
        assertNotNull("EntityManager not open", entityManager.isOpen());
        assertNotNull("EntityManager not in transaction", entityManager.isJoinedToTransaction());
    }

}
