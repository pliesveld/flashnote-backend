package com.pliesveld.flashnote.repository;

import com.pliesveld.flashnote.spring.Profiles;
import com.pliesveld.flashnote.spring.SpringDataTestConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ImportResource;
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
@ContextConfiguration(classes = {SpringDataTestConfig.class, CategoriesTest.class}, loader = AnnotationConfigContextLoader.class)
@ImportResource("classpath:repository/com/pliesveld/flashnote/spring/json-repository-populator.xml")
@Transactional
public class CategoriesTest {
    private static final Logger LOG = LogManager.getLogger();

    @Autowired
    CategoryRepository categoryRepository;

    @Test
    public void testLoadRepositoryFromJson()
    {
        assertTrue(categoryRepository.count() > 0);
    }

}