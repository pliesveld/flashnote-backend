package com.pliesveld.flashnote.domain;

import com.pliesveld.flashnote.spring.BlankEntityTestAnnotations;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
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
public class AttachmentTextTest extends AbstractDomainEntityUnitTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @PersistenceContext
    EntityManager entityManager;

    Serializable attachment_id;

    @Before
    public void setupEntities() {
        AttachmentText attachmentText = attachmentTextBean();
        attachmentText = attachmentTextRepository.save(attachmentText);
        attachment_id = attachmentText.getId();
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    public void whenContextLoad_thenCorrect() {
        assertNotNull(entityManager.find(AttachmentText.class, attachment_id));
    }

    @Test
    public void givenTextAttachment_whenLoadingBaseRepositoryById() {
        assertNotNull(attachmentRepository.findOneById((Integer) attachment_id));
    }

    @Test
    public void givenTextAttachment_whenLoadingTextRepositoryById() {
        assertNotNull(attachmentTextRepository.findOneById((Integer) attachment_id));
    }

    @After
    public void flushAfter() {
        entityManager.flush();
    }

    /*
    @Test
    public void testAttachmentQuestionPersistance()
    {
        AbstractStatement stmt = createQuestion();
        AbstractAttachment attch = createAttachment();

        em.persist(stmt);
        attch.setStatement(stmt);
        em.persist(attch);
        em.flush();
    }

    @Test
    public void testAttachmentQuestionCascadePersistance()
    {
        AbstractStatement stmt = createQuestion();
        AbstractAttachment attch = createAttachment();

        attch.setStatement(stmt);
        em.persist(attch);
        em.flush();
    }

    @Test
    public void testAttachmentAnswerPersistance()
    {
        AbstractStatement stmt = createAnswer();
        AbstractAttachment attch = createAttachment();

        em.persist(stmt);
        attch.setStatement(stmt);
        em.persist(attch);
        em.flush();
    }

    @Test
    public void testAttachmentAnswerCascadePersistance()
    {
        AbstractStatement stmt = createAnswer();
        AbstractAttachment attch = createAttachment();

        attch.setStatement(stmt);
        em.persist(attch);
        em.flush();
    }
    */
}
