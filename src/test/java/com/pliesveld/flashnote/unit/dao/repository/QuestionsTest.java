package com.pliesveld.flashnote.unit.dao.repository;

import com.pliesveld.flashnote.repository.QuestionRepository;
import com.pliesveld.flashnote.spring.Profiles;
import com.pliesveld.flashnote.unit.dao.spring.SpringDataTestConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by happs on 3/2/16.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles(Profiles.INTEGRATION_TEST)
@ContextConfiguration(classes = SpringDataTestConfig.class, loader = AnnotationConfigContextLoader.class)
public class QuestionsTest {
    private static final Logger LOG = LogManager.getLogger();

    @Autowired
    QuestionRepository questionRepository;



    @Test
    public void testLoadRepositoryFromJson()
    {
        assertTrue(questionRepository.count() > 0);
        assertEquals(questionRepository.count(), 3);


    }





}



