package com.pliesveld.flashnote.exporter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pliesveld.flashnote.domain.AbstractDomainEntityUnitTest;
import com.pliesveld.flashnote.domain.base.DomainBaseEntity;
import com.pliesveld.flashnote.model.json.Views;
import com.pliesveld.flashnote.serializer.HibernateAwareObjectMapperImpl;
import com.pliesveld.flashnote.spring.DefaultEntityTestAnnotations;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
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

@RunWith(SpringJUnit4ClassRunner.class)
@DefaultEntityTestAnnotations
@Transactional
//@TestExecutionListeners(listeners = LogHibernateTestExecutionListener.class, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
public class HibernateSerializationTest extends AbstractDomainEntityUnitTest implements ApplicationContextAware {
    private static final Logger LOG = LogManager.getLogger();
    private static final Logger LOG_SQL = LogManager.getLogger("org.hibernate.SQL");

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    HibernateAwareObjectMapperImpl hibernateAwareObjectMapper;

    ArrayList<DomainBaseEntity> entityBeans = new ArrayList<>();

    private void doSerializationTest(ObjectWriter writer) {
        Map<Class<?>, Serializable> idMap = entityBeans.stream().collect(Collectors.toMap(Object::getClass, DomainBaseEntity::getId));

//        Map<Class<?>, String> outMap = entityBeans.stream().collect(Collectors.toMap(Object::getClass, writer::out));
//        outMap.forEach((clazz, out) -> {
//            System.out.println(clazz);
//            System.out.println(out);
//        });

        enableSQL();
        idMap.forEach((clazz, id) -> {
            LOG_SQL.debug("Loading {} {}", clazz.getName(), id);
            DomainBaseEntity entity = (DomainBaseEntity) entityManager.find(clazz, id, LockModeType.READ);

//            DomainBaseEntity entity = (DomainBaseEntity) entityManager.getReference(clazz, id);

            LOG_SQL.debug("Serializing {} {} using {}", clazz.getName(), id, writer.objectMapper.getSerializationConfig().getActiveView());
            writer.out(entity, writer.view_clazz);
        });
        disableSQL();
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

    @Before
    public void setUp() throws Exception {
        entityBeans = initializeTestClass();
        entityManager.flush();
        entityManager.clear();
    }

    private ArrayList<DomainBaseEntity> initializeTestClass() {
        for(String beanName : ctx.getBeanDefinitionNames()) {
            LOG.debug(beanName);
        }

        EntityManagerFactory entityManagerFactory = (EntityManagerFactory) ctx.getBean("entityManagerFactory");
        List<? extends Class<?>> entityClassList = entityManagerFactory.getMetamodel().getEntities().parallelStream().map(p -> p.getJavaType()).collect(Collectors.toList());

        ArrayList<DomainBaseEntity> entityBeans = new ArrayList<>(entityClassList.size());

        for(Class<?> clazz : entityClassList)
        {
            try {
                Object obj = ctx.getBean(clazz);
                entityManager.persist(obj);
                entityManager.flush();

                if(DomainBaseEntity.class.isAssignableFrom(clazz))
                {
                    DomainBaseEntity dbe = (DomainBaseEntity) obj;
                    entityBeans.add(dbe);
                    LOG.debug("Got bean of class {}", clazz);
                } else {
                    LOG.warn("Got bean; but class {} could not be assigned to DomainBaseEntity.class", clazz);
                }

            } catch(Exception e) {
                LOG.debug("Couldn't load bean of class {}",clazz);

            }

        }
        return entityBeans;
    }
}
