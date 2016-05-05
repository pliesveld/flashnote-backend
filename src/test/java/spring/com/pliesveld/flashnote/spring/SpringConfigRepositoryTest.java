package com.pliesveld.flashnote.spring;

import com.pliesveld.flashnote.repository.DeckRepository;
import com.pliesveld.flashnote.repository.StudentRepository;
import com.pliesveld.flashnote.spring.data.SpringDataConfig;
import com.pliesveld.flashnote.spring.db.PersistenceContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.util.StringUtils;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class SpringConfigRepositoryTest {
    private static final Logger LOG = LogManager.getLogger();

    @Test
    public void constructDeckRepositry()
    {

        System.setProperty("spring.profiles.active",Profiles.INTEGRATION_TEST);
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();

        LOG.info(StringUtils.arrayToCommaDelimitedString(ctx.getEnvironment().getActiveProfiles()));
        try {
        ctx.register(SpringDataTestConfig.class);
        ctx.register(TestRepositoryConfig.class);


        ctx.register(TestRepositoryConfig.class);
        ctx.refresh();
        } catch(PropertyReferenceException pre) {
            fail(pre.getMessage());
            throw pre;
        } catch(BeanCreationException bce) {
            bce.printStackTrace();
            fail(bce.getMessage());
        }

        TestRepositoryConfig testRepositoryConfig = ctx.getBean(TestRepositoryConfig.class);


        assertNotNull(testRepositoryConfig.studentRepository);
        assertNotNull(testRepositoryConfig.deckRepository);
    }
}

@Configuration
@Import(value = {SpringDataConfig.class, PersistenceContext.class})
class TestRepositoryConfig {

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    DeckRepository deckRepository;



    public TestRepositoryConfig() {}

    int sample = 4;
}
