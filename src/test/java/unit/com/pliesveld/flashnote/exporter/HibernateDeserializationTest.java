package com.pliesveld.flashnote.exporter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pliesveld.flashnote.domain.AbstractDomainEntityUnitTest;
import com.pliesveld.flashnote.domain.base.DomainBaseEntity;
import com.pliesveld.flashnote.serializer.HibernateAwareObjectMapperImpl;
import com.pliesveld.flashnote.spring.DefaultEntityTestAnnotations;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringJUnit4ClassRunner.class)
@DefaultEntityTestAnnotations
@Transactional
//@TestExecutionListeners(listeners = LogHibernateTestExecutionListener.class, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
public class HibernateDeserializationTest extends AbstractDomainEntityUnitTest implements ApplicationContextAware {
    private static final Logger LOG = LogManager.getLogger();
    private static final Logger LOG_SQL = LogManager.getLogger("org.hibernate.SQL");

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
            this.objectMapper.setConfig(this.objectMapper.getSerializationConfig().withView(Object.class));
            this.objectMapper.setConfig(this.objectMapper.getDeserializationConfig().withView(Object.class));
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

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    HibernateAwareObjectMapperImpl hibernateAwareObjectMapper;

    Serializable student_id = null;
    String student_name = "";

    @Test
    public void whenContextLoad_thenCorrect()
    {

    }

//    @Test
//    public void testFlashCard() throws IOException {
//        Question question = questionBean();
//        Answer answer = answerBean();
//        Serializable flashcard_id;
//
//
//        question = questionRepository.save(question);
//        answer = answerRepository.save(answer);
//
//        entityManager.flush();
//
//        FlashCard flashCard = new FlashCard(question,answer);
//        entityManager.persist(flashCard);
//        entityManager.flush();
//
//        flashcard_id = flashCard.getId();
//
//        assertTrue(hibernateAwareObjectMapper.canSerialize(flashCard.getClass()));
//        String fc_json = hibernateAwareObjectMapper.writeValueAsString(flashCard);
//
//        System.out.println(fc_json);
//
//
//        FlashCard fc_deserialized = hibernateAwareObjectMapper.readValue(fc_json,flashCard.getClass());
//        assertNotNull(fc_deserialized);
//
//        assertNotNull(fc_deserialized.getId());
//        assertNotNull(fc_deserialized.getId().getAnswerId());
//        assertNotNull(fc_deserialized.getId().getQuestionId());
//
//        assertNotNull(fc_deserialized.getQuestion());
//        assertNotNull(fc_deserialized.getAnswer());
//
//        assertNotNull(fc_deserialized.getQuestion().getTitle());
//        assertNotNull(fc_deserialized.getQuestion().getContent());
//        assertNotNull(fc_deserialized.getAnswer().getContent());
//    }

//    @Test
//    public void testQuestionBank() throws IOException {
//
//        disableSQL();
//        QuestionBank questionBank = questionBankBean();
//
//        Category category = categoryBean();
//        entityManager.persist(category);
//        entityManager.flush();
//        questionBank.setCategory(category);
//
//        for(int i = 0; i < 5; i++)
//        {
//            Question question = questionBean();
//            entityManager.persist(question);
//            questionBank.add(question);
//        }
//
//        entityManager.persist(questionBank);
//        entityManager.flush();
//
//
//        Serializable questionbank_id = questionBank.getId();
//        entityManager.clear();
//
//        enableSQL();
//        questionBank = entityManager.find(QuestionBank.class, questionbank_id);
////        Hibernate.initialize(questionBank.getQuestions());
////        questionBank.getDescription();
//
//        assertTrue(hibernateAwareObjectMapper.canSerialize(questionBank.getClass()));
//        String qb_json = hibernateAwareObjectMapper.writeValueAsString(questionBank);
//
//        System.out.println(qb_json);
//
//        QuestionBank qb_deserialized = hibernateAwareObjectMapper.readValue(qb_json,questionBank.getClass());
//        assertNotNull(qb_deserialized);
//
//        assertNotNull(qb_deserialized.getId());
//
//    }
}
