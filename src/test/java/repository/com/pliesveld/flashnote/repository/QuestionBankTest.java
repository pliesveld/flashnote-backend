package com.pliesveld.flashnote.repository;

import com.pliesveld.flashnote.spring.Profiles;
import com.pliesveld.flashnote.spring.SpringDataTestConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertTrue;

/**
 * Created by happs on 3/2/16.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles(Profiles.INTEGRATION_TEST)
@ContextConfiguration(classes = SpringDataTestConfig.class, loader = AnnotationConfigContextLoader.class)
@Transactional
public class QuestionBankTest {
    private static final Logger LOG = LogManager.getLogger();

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    QuestionBankRepository questionBankRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Test
    public void testLoadRepositoryFromJson()
    {
        assertTrue(questionRepository.count() > 0);
        assertTrue(categoryRepository.count() > 0);
        assertTrue(questionBankRepository.count() > 0);

    }

    @Test
    public void testFindQuestionBank()
    {
        LOG.info("Categories");
        categoryRepository.findAll().forEach(LOG::info);

        LOG.info("QuestionBank");
        questionBankRepository.findAll().forEach(LOG::info);

        LOG.info("Questions");
        questionRepository.findAll().forEach(LOG::info);


    }



}



