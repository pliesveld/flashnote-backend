package com.pliesveld.flashnote.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pliesveld.flashnote.domain.base.DomainBaseEntity;
import com.pliesveld.flashnote.serializer.HibernateAwareObjectMapperImpl;
import com.pliesveld.flashnote.unit.dao.spring.LogHibernateTestExecutionListener;
import com.pliesveld.flashnote.unit.spring.DefaultEntityTestAnnotations;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;

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

    @Before
    public void setupEntities()
    {
        StudentDetails studentDetails = this.studentDetailsAndStudentBean();
        entityManager.persist(studentDetails.getStudent());
        entityManager.persist(studentDetails);
        student_id = studentDetails.getId();
        student_name = studentDetails.getName();

        entityManager.flush();
    }


    public <T extends DomainBaseEntity> T loadEntityInTransaction(boolean returnProxyLazy) {
        Question question = this.questionBean();

        StudentDetails studentDetails = new StudentDetails();
        studentDetails.setId((Integer) student_id);

        question.addAnnotation(new AnnotatedStatement(studentDetails,"TEST MESSAGE"));
        entityManager.persist(question);
        entityManager.flush();

        Serializable entity_id = question.getId();
//        entityManager.detach(question);
        entityManager.clear();

        if(returnProxyLazy)
        {
            question = entityManager.getReference(Question.class,entity_id);
        } else {
            question = entityManager.find(Question.class,entity_id);
        }


        return (T) question;
    }


    @Test
    public void testContextLazy() throws JsonProcessingException {

        Question question = loadEntityInTransaction(true);

        assertTrue(hibernateAwareObjectMapper.canSerialize(question.getClass()));
        System.out.println(

           hibernateAwareObjectMapper.
                   writeValueAsString(question)
        );
    }

    @Test
    public void testContextLoaded() throws JsonProcessingException {

        Question question = loadEntityInTransaction(false);

        assertTrue(hibernateAwareObjectMapper.canSerialize(question.getClass()));
        System.out.println(

           hibernateAwareObjectMapper.
                   writeValueAsString(question)
        );
    }

}
//
//@Transactional
//interface HibernateSerializationTestInterface {
//    public <T extends DomainBaseEntity> T loadEntityInTransaction();
//}