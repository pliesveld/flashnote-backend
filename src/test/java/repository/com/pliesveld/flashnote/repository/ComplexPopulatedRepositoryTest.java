package com.pliesveld.flashnote.repository;

import com.pliesveld.flashnote.spring.Profiles;
import com.pliesveld.populator.repository.reader.RepositorySettings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles(Profiles.INTEGRATION_TEST)
@ContextHierarchy({
        @ContextConfiguration(name = "REPOSITORY", classes = { ComplexPopulatedRepositoryTest.class }, loader = AnnotationConfigContextLoader.class)
})
@DirtiesContext
public class ComplexPopulatedRepositoryTest extends AbstractPopulatedRepositoryUnitTest {
    private static final Logger LOG = LogManager.getLogger();

    @Bean
    public RepositorySettings repositorySettings() {
        RepositorySettings repositorySettings = new RepositorySettings(new Resource[]
                {
                        new ClassPathResource("test-data-category.json", this.getClass()),
                        new ClassPathResource("test-data-collection.json", this.getClass())
                });
        return repositorySettings;
    }

    @Test
    public void whenContextLoad_thenCorrect() {
        assertTrue(categoryRepository.count() > 0);
        assertTrue(questionRepository.count() > 0);
        assertTrue(questionBankRepository.count() > 0);
    }

    @Test
    public void whenCategoryFind_thenCorrect() {
        debugRepository(categoryRepository);
        debugRepository(questionRepository);
        debugRepository(questionBankRepository);
    }

}