package com.pliesveld.flashnote.domain;

import com.pliesveld.spring.SpringTestConfig;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringTestConfig.class, loader = AnnotationConfigContextLoader.class)
@Transactional
public class StudentDeckTest
{
    @PersistenceContext
    private EntityManager entityManager;

    @Test
    @Transactional
    public void studentPersistGenId()
    {
        Student student1 = new Student();
        student1.setName("Student1");
        student1.setEmail("student1@email.com");
        entityManager.persist(student1);
        entityManager.flush();
        Serializable s1 = student1.getId();
    }

}

