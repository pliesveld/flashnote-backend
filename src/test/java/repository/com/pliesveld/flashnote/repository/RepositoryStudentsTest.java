package com.pliesveld.flashnote.repository;

import com.pliesveld.flashnote.domain.Student;
import com.pliesveld.flashnote.spring.Profiles;
import com.pliesveld.flashnote.spring.SpringDataTestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static org.junit.Assert.*;

/**
 * Created by happs on 3/2/16.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles(Profiles.INTEGRATION_TEST)
@ContextHierarchy({
        @ContextConfiguration(classes = { SpringDataTestConfig.class }, loader = AnnotationConfigContextLoader.class),
        @ContextConfiguration(classes = { RepositoryStudentsTest.class }, loader = AnnotationConfigContextLoader.class)
})
@DirtiesContext
@Configuration
public class RepositoryStudentsTest extends AbstractPopulatedRepositoryUnitTest {

    @Override
    protected Resource[] repositoryProperties() {
        return new Resource[]{ new ClassPathResource("test-data-students.json", this.getClass()) };
    }


    @Test
    public void testLoadRepositoryFromJson()
    {
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



