package com.pliesveld.flashnote.repository;

import com.pliesveld.flashnote.domain.Student;
import com.pliesveld.flashnote.spring.Profiles;
import com.pliesveld.flashnote.spring.repository.RepositorySettings;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static org.junit.Assert.*;



@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles(Profiles.INTEGRATION_TEST)
@ContextHierarchy({
        @ContextConfiguration(name = "REPOSITORY", classes = { PopulatedStudentsRepositoryTest.class }, loader = AnnotationConfigContextLoader.class)
})
@DirtiesContext
public class PopulatedStudentsRepositoryTest extends AbstractPopulatedRepositoryUnitTest {

    @Bean
    public RepositorySettings repositorySettings() {
        return RepositorySettings.load("test-data-students.json", this.getClass());
    }

    @Test
    public void whenContextLoad_thenCorrect() {
        assertTrue(studentRepository.count() > 0);
        assertTrue(studentDetailsRepository.count() > 0);
    }

    @Test
    public void testFindByEmail()
    {
        final String STUDENT_EMAIL = "student@example.com";
        Student student = studentRepository.findOneByEmail(STUDENT_EMAIL);
        assertNotNull(student);
        assertEquals(student.getEmail(), STUDENT_EMAIL);
    }


}



