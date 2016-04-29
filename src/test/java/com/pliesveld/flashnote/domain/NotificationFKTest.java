package com.pliesveld.flashnote.domain;


import com.pliesveld.flashnote.unit.spring.DefaultEntityTestAnnotations;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceException;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@DefaultEntityTestAnnotations
@Transactional
public class NotificationFKTest extends NotificationTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    @Override
    public void testEntityContext()
    {
        assertNotNull(recipient_id);
        assertNotNull(message_id);
    }

    @Test
    @Override
    public void testEntitySanity()
    {

        assertNotNull(entityManager.find(Notification.class, message_id));
        assertNotificationRepositoryCount(1);
    }

    @Test
    public void testStudentRemovalFKViolation()
    {
        StudentDetails studentDetails = entityManager.find(StudentDetails.class, recipient_id);
        thrown.expect(PersistenceException.class);
        entityManager.remove(studentDetails);
    }


    @Test
    public void testStudentRemoval()
    {
        StudentDetails studentDetails = entityManager.find(StudentDetails.class, recipient_id);
        notificationRepository.deleteAll();
        entityManager.remove(studentDetails);
    }
}
