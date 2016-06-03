package com.pliesveld.flashnote.exporter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pliesveld.flashnote.domain.AbstractDomainEntityUnitTest;
import com.pliesveld.flashnote.domain.base.DomainBaseEntity;
import com.pliesveld.flashnote.model.json.Views;
import com.pliesveld.flashnote.serializer.HibernateAwareObjectMapperImpl;
import com.pliesveld.flashnote.spring.Profiles;
import com.pliesveld.flashnote.spring.SpringEntityTestConfig;
import com.pliesveld.tests.beans.DomainEntities;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionEventListener;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles(Profiles.INTEGRATION_TEST)
@ContextHierarchy({
        @ContextConfiguration(classes = SpringEntityTestConfig.class, loader = AnnotationConfigContextLoader.class),
        @ContextConfiguration(classes = DomainEntities.class, loader = AnnotationConfigContextLoader.class),
        @ContextConfiguration(classes = HibernateSerializationTest.class, loader = AnnotationConfigContextLoader.class)
})
@TestPropertySource( locations = "classpath:test-datasource.properties" )
@Transactional
//@TestExecutionListeners(listeners = LogHibernateTestExecutionListener.class, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
@DirtiesContext
@Configuration
public class HibernateSerializationTest extends AbstractDomainEntityUnitTest implements ApplicationContextAware {
    private static final Logger LOG = LogManager.getLogger();
    private static final Logger LOG_SQL = LogManager.getLogger("org.hibernate.SQL");

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    HibernateAwareObjectMapperImpl hibernateAwareObjectMapper;

    @Autowired
    SessionCounter sessionCounter;

    ArrayList<DomainBaseEntity> entityBeans = new ArrayList<>();

    @Before
    public void setUp() throws Exception {
        assertNotNull(sessionCounter);
        entityManager.unwrap(org.hibernate.Session.class).addEventListeners(sessionCounter);
        entityBeans = initializeTestClass();
        entityManager.flush();
        //TODO: clearing context displays SQL statements executed to retrieve domain entity;  however currently; entities may have uninitialized collections
//        entityManager.clear();
    }

    private void doSerializationTest(ObjectWriter writer) {
        Map<Class<?>, Serializable> idMap = entityBeans.stream().collect(Collectors.toMap(Object::getClass, DomainBaseEntity::getId));

//        Map<Class<?>, String> outMap = entityBeans.stream().collect(Collectors.toMap(Object::getClass, writer::out));
//        outMap.forEach((clazz, out) -> {
//            LOG.debug("Contents using string reflection {}", clazz);
//            System.out.println(clazz);
//            debugEntity(out);
//        });

        enableSQL();
        idMap.forEach((clazz, id) -> {
            LOG_SQL.debug("\n\n\nSerializing {}(id={}) with view {}", clazz.getName(), id, writer.view_clazz != null ? writer.view_clazz.getSimpleName() : null);
            DomainBaseEntity entity = (DomainBaseEntity) entityManager.find(clazz, id, LockModeType.READ);
//            DomainBaseEntity entity = (DomainBaseEntity) entityManager.getReference(clazz, id);
            writer.out(entity, writer.view_clazz);
//            entityManager.clear();

        });
        disableSQL();

    }

    @Test
    public void whenContextLoad_thenCorrect() {

    }

    @Test
    public void testSerialization_withDefaultMapper() throws Exception {
        ObjectWriter writer = new ObjectWriter(hibernateAwareObjectMapper, null);
        doSerializationTest(writer);
    }

    @Test
    public void testSerialization_withSummaryView() throws Exception {

        ObjectWriter writer = new ObjectWriter(hibernateAwareObjectMapper, Views.Summary.class);
        doSerializationTest(writer);
    }

    @Test
    public void testSerialization_withSummaryWithCollectionsView() throws Exception {

        ObjectWriter writer = new ObjectWriter(hibernateAwareObjectMapper, Views.SummaryWithCollections.class);
        doSerializationTest(writer);
    }

    @Test
    public void testSerialization_withInternalView() throws Exception {
        ObjectWriter writer = new ObjectWriter(hibernateAwareObjectMapper, Views.Internal.class);
        doSerializationTest(writer);
    }

    static public class ObjectWriter {
        private static final Logger LOG = LogManager.getLogger("org.hibernate.SQL");
        private ObjectMapper objectMapper;
        private Class<?> view_clazz = Object.class;

        public <T extends ObjectMapper> ObjectWriter(T objectMapper, Class<?> view_clazz) {
            this.objectMapper = (T) objectMapper.copy();
            this.objectMapper.setConfig(this.objectMapper.getSerializationConfig().withView(view_clazz));
            this.objectMapper.setConfig(this.objectMapper.getDeserializationConfig().withView(view_clazz));
            this.view_clazz = view_clazz;
        }

        public <T extends ObjectMapper> ObjectWriter(T objectMapper) {
            this.objectMapper = (T) objectMapper.copy();
            this.objectMapper.setConfig(this.objectMapper.getSerializationConfig().withView(null));
            this.objectMapper.setConfig(this.objectMapper.getDeserializationConfig().withView(null));
        }

        public String out(Object obj) {
            try {
                String out = this.objectMapper.writeValueAsString(obj);
                LOG_SQL.debug(out);
                return out;
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                throw new IllegalArgumentException("Could not serialize " + obj.getClass().getName());
            }
        }
        public String out(Object obj, Class<?> temp_view_clazz) {
            try {
                String out = this.objectMapper.writerWithView(temp_view_clazz).writeValueAsString(obj);
                LOG_SQL.debug(out);
                return out;
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                throw new IllegalArgumentException("Could not serialize " + obj.getClass().getName());
            }
        }
    }

    private ArrayList<DomainBaseEntity> initializeTestClass() {
//        for(String beanName : ctx.getBeanDefinitionNames()) {
//            LOG.trace(beanName);
//        }

        EntityManagerFactory entityManagerFactory = (EntityManagerFactory) ctx.getBean("entityManagerFactory");
        List<? extends Class<?>> entityClassList = entityManagerFactory.getMetamodel().getEntities().parallelStream().map(p -> p.getJavaType()).collect(Collectors.toList());

        ArrayList<DomainBaseEntity> entityBeans = new ArrayList<>(entityClassList.size());

        for(Class<?> clazz : entityClassList)
        {
            try {
                sessionCounter.clear();
                Object obj = ctx.getBean(clazz);

                entityManager.persist(obj);
                entityManager.flush();
//                entityManager.clear();

                LOG.trace("Saving {} {}", () -> {return clazz.getSimpleName();}, () -> {
                    final int ent_cnt = sessionCounter.getEntities();
                    final int col_cnt = sessionCounter.getCollections();
                    if(ent_cnt == col_cnt && ent_cnt == 0)
                        return "";
                    return String.format("[Entities=%d, Collections=%d]",ent_cnt, col_cnt);
                });


                if(DomainBaseEntity.class.isAssignableFrom(clazz))
                {
                    DomainBaseEntity dbe = (DomainBaseEntity) obj;
                    entityBeans.add(dbe);
                } else {
                    LOG.info("Got bean; but class {} could not be assigned to DomainBaseEntity.class", clazz);
                }

            } catch(Exception e) {
                LOG.warn("Couldn't persist bean {}", clazz);

            }

        }
        return entityBeans;
    }


    @Component
    protected static class SessionCounter implements SessionEventListener {
        private static final Logger LOG = LogManager.getLogger();

        private int collections;
        private int entities;

        public int getCollections() {
            return collections;
        }

        public int getEntities() {
            return entities;
        }

        @Override
        public void transactionCompletion(boolean successful) {
            LOG.trace("TransactionComplete: success {}", successful);
        }

        @Override
        public void jdbcConnectionAcquisitionStart() {

        }

        @Override
        public void jdbcConnectionAcquisitionEnd() {

        }

        @Override
        public void jdbcConnectionReleaseStart() {

        }

        @Override
        public void jdbcConnectionReleaseEnd() {

        }

        @Override
        public void jdbcPrepareStatementStart() {

        }

        @Override
        public void jdbcPrepareStatementEnd() {

        }

        @Override
        public void jdbcExecuteStatementStart() {

        }

        @Override
        public void jdbcExecuteStatementEnd() {

        }

        @Override
        public void jdbcExecuteBatchStart() {

        }

        @Override
        public void jdbcExecuteBatchEnd() {

        }

        @Override
        public void cachePutStart() {

        }

        @Override
        public void cachePutEnd() {

        }

        @Override
        public void cacheGetStart() {

        }

        @Override
        public void cacheGetEnd(boolean hit) {

        }

        @Override
        public void flushStart() {
            LOG.trace("flushStart");
            clear();

        }

        void clear() {
            this.entities = 0;
            this.collections = 0;
        }

        @Override
        public void flushEnd(int numberOfEntities, int numberOfCollections) {
            this.entities += numberOfEntities;
            this.collections += numberOfCollections;
            LOG.trace("flushEnd entities {} collections {}", numberOfEntities, numberOfCollections);
        }

        @Override
        public void partialFlushStart() {
            LOG.trace("partialFlushStart");
        }

        @Override
        public void partialFlushEnd(int numberOfEntities, int numberOfCollections) {

        }

        @Override
        public void dirtyCalculationStart() {

        }

        @Override
        public void dirtyCalculationEnd(boolean dirty) {

        }

        @Override
        public void end() {
            LOG.trace("end");

        }
    }
}

