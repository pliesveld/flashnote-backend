package com.pliesveld.flashnote.unit.repository;

import com.pliesveld.flashnote.repository.StudentRepository;
import com.pliesveld.flashnote.spring.Profiles;
import com.pliesveld.flashnote.spring.SpringRootConfig;
import com.pliesveld.flashnote.spring.db.H2DataSource;
import com.pliesveld.flashnote.spring.db.PersistenceContext;
import org.junit.Test;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.stereotype.Controller;
import org.springframework.test.context.ActiveProfiles;

import static junit.framework.Assert.*;

@Configuration
@ActiveProfiles(Profiles.INTEGRATION_TEST)
@ComponentScan(basePackages = {
        "com.pliesveld.flashnote.spring.db",
        "com.pliesveld.flashnote.service",
        "com.pliesveld.flashnote.repository"
},
        excludeFilters = @ComponentScan.Filter(Controller.class))
public class ConstructRepositoryBeanTest {

    @Test
    public void constructDeckRepositry()
    {

        System.setProperty("spring.profiles.active",Profiles.INTEGRATION_TEST);
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();

        try {
        ctx.register(SpringRootConfig.class);
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
        assertEquals(4,testRepositoryConfig.sample);

        assertNotNull(testRepositoryConfig.studentRepository);
    }
}

@Configuration
@Import(value = {H2DataSource.class,PersistenceContext.class})
class TestRepositoryConfig {

    @Autowired
    StudentRepository studentRepository;

    public TestRepositoryConfig() {}

    int sample = 4;
}
