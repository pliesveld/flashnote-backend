package com.pliesveld.flashnote.domain;


import com.pliesveld.flashnote.spring.BlankEntityTestAnnotations;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@BlankEntityTestAnnotations
@Transactional
public class NotificationTest extends AbstractDomainEntityUnitTest {

    final static private String TEST_MESSAGE = "THIS IS A TEST NOTIFICATION MESSAGE";

    @PersistenceContext
    EntityManager entityManager;

    Serializable recipient_id = null;
    Serializable message_id = null;

    @Before
    public void setupEntities()
    {
        StudentDetails student = this.studentDetailsBean();
        student = studentDetailsRepository.save(student);

        recipient_id = student.getId();

        message_id = notificationRepository.save(new Notification(student, TEST_MESSAGE)).getId();

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    public void testEntityContext()
    {
        assertNotNull(recipient_id);
        assertNotNull(message_id);
    }

    @Test
    public void testEntitySanity()
    {
        assertNotNull(entityManager.find(StudentDetails.class, recipient_id));
        assertNotNull(entityManager.find(Notification.class, message_id));
        assertNotificationRepositoryCount(1);
    }

    @After
    public void flushAfter()
    {
        entityManager.flush();
    }

}
