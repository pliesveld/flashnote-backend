package com.pliesveld.flashnote.unit.domain.entity;

import com.pliesveld.flashnote.domain.*;
import com.pliesveld.flashnote.unit.spring.DefaultTestAnnotations;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@RunWith(SpringJUnit4ClassRunner.class)
@DefaultTestAnnotations
@Transactional
public class AttachmentTest {

    @PersistenceContext
    EntityManager em;

    AbstractAttachment createAttachment()
    {
        AttachmentText attachment = new AttachmentText();
        attachment.setFileName("test.txt");
        attachment.setContentType(AttachmentType.DOC);
        attachment.setContents("THIS IS THE CONTENTS OF THE DOCUMENT ATTACHMENT");
        return attachment;
    }

    AbstractStatement createQuestion()
    {
        Question que = new Question();
        que.setContent("Question.");
        return que;
    }


    @Test
    public void testAttachmentPersistance()
    {
        AbstractStatement stmt = createQuestion();
        AbstractAttachment attch = createAttachment();

        em.persist(stmt);
        attch.setStatement(stmt);
        em.persist(attch);
        em.flush();

    }
}
