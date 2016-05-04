package com.pliesveld.flashnote.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pliesveld.flashnote.serializer.HibernateAwareObjectMapperImpl;
import com.pliesveld.flashnote.unit.dao.spring.LogHibernateTestExecutionListener;
import com.pliesveld.flashnote.unit.spring.DefaultEntityTestAnnotations;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.io.Serializable;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@DefaultEntityTestAnnotations
@Transactional
@TestExecutionListeners(listeners = LogHibernateTestExecutionListener.class, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
public class HibernateSerializationTest extends AbstractDomainEntityUnitTest {
    private static final Logger LOG = LogManager.getLogger();

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    HibernateAwareObjectMapperImpl hibernateAwareObjectMapper;

    Serializable student_id = null;
    String student_name = "";

    @Test
    public void testQuestionLazy() throws JsonProcessingException {
        StudentDetails studentDetails = this.studentDetailsAndStudentBean();
        entityManager.persist(studentDetails.getStudent());
        entityManager.persist(studentDetails);
        student_id = studentDetails.getId();
        student_name = studentDetails.getName();

        entityManager.flush();

        Question question = this.questionBean();

        question.addAnnotation(new AnnotatedStatement(studentDetails,"TEST MESSAGE"));
        entityManager.persist(question);
        entityManager.flush();

        Serializable entity_id = question.getId();

        entityManager.clear();

        question = entityManager.getReference(Question.class,entity_id);

        assertTrue(hibernateAwareObjectMapper.canSerialize(question.getClass()));
        System.out.println(

           hibernateAwareObjectMapper.
                   writeValueAsString(question)
        );
    }

    @Test
    public void testQuestionLoaded() throws JsonProcessingException {
        StudentDetails studentDetails = this.studentDetailsAndStudentBean();
        entityManager.persist(studentDetails.getStudent());
        entityManager.persist(studentDetails);
        student_id = studentDetails.getId();
        student_name = studentDetails.getName();

        entityManager.flush();

        Question question = this.questionBean();

        question.addAnnotation(new AnnotatedStatement(studentDetails,"TEST MESSAGE"));
        entityManager.persist(question);
        entityManager.flush();

        Serializable entity_id = question.getId();

        entityManager.clear();

        question = entityManager.find(Question.class,entity_id);



        assertTrue(hibernateAwareObjectMapper.canSerialize(question.getClass()));
        System.out.println(

           hibernateAwareObjectMapper.
                   writeValueAsString(question)
        );
    }


    @Test
    public void testFlashCard() throws IOException {
        Question question = questionBean();
        Answer answer = answerBean();
        Serializable flashcard_id;


        question = questionRepository.save(question);
        answer = answerRepository.save(answer);

        entityManager.flush();

        FlashCard flashCard = new FlashCard(question,answer);
        entityManager.persist(flashCard);
        entityManager.flush();

        flashcard_id = flashCard.getId();

        assertTrue(hibernateAwareObjectMapper.canSerialize(flashCard.getClass()));
        String fc_json = hibernateAwareObjectMapper.writeValueAsString(flashCard);

        System.out.println(fc_json);


        FlashCard fc_deserialized = hibernateAwareObjectMapper.readValue(fc_json,flashCard.getClass());
        assertNotNull(fc_deserialized);

        assertNotNull(fc_deserialized.getId());
        assertNotNull(fc_deserialized.getId().getAnswerId());
        assertNotNull(fc_deserialized.getId().getQuestionId());

        assertNotNull(fc_deserialized.getQuestion());
        assertNotNull(fc_deserialized.getAnswer());

        assertNotNull(fc_deserialized.getQuestion().getTitle());
        assertNotNull(fc_deserialized.getQuestion().getContent());
        assertNotNull(fc_deserialized.getAnswer().getContent());


    }

    @Test
    public void testQuestionBank() throws IOException {

        disableSQL();
        QuestionBank questionBank = questionBankBean();

        Category category = categoryBean();
        entityManager.persist(category);
        entityManager.flush();
        questionBank.setCategory(category);

        for(int i = 0; i < 5; i++)
        {
            Question question = questionBean();
            entityManager.persist(question);
            questionBank.add(question);
        }

        entityManager.persist(questionBank);
        entityManager.flush();


        Serializable questionbank_id = questionBank.getId();
        entityManager.clear();

        enableSQL();
        questionBank = entityManager.find(QuestionBank.class, questionbank_id);
//        Hibernate.initialize(questionBank.getQuestions());
//        questionBank.getDescription();

        assertTrue(hibernateAwareObjectMapper.canSerialize(questionBank.getClass()));
        String qb_json = hibernateAwareObjectMapper.writeValueAsString(questionBank);

        System.out.println(qb_json);

        QuestionBank qb_deserialized = hibernateAwareObjectMapper.readValue(qb_json,questionBank.getClass());
        assertNotNull(qb_deserialized);

        assertNotNull(qb_deserialized.getId());

    }
}
