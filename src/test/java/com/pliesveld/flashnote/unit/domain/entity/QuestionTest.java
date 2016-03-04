package com.pliesveld.flashnote.unit.domain.entity;

import com.pliesveld.flashnote.domain.Question;
import com.pliesveld.flashnote.repository.QuestionRepository;
import com.pliesveld.flashnote.unit.spring.DefaultTestAnnotations;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@DefaultTestAnnotations
@Transactional
public class QuestionTest {
    private static final Logger LOG = LogManager.getLogger();

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    public void testAuditCreationTime()
    {
        Question que = new Question();
        que.setContent("This is a question?");

        que = questionRepository.save(que);
        que.getCreatedOn();

        assertNotNull(que.getCreatedOn());
        assertTrue(que.getCreatedOn().toEpochMilli() > 0);
        LOG.debug("created question: " + que);
    }
}
