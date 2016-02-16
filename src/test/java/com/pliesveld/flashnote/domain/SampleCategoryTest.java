package com.pliesveld.flashnote.domain;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Set;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = com.pliesveld.spring.SpringTestConfig.class, loader = AnnotationConfigContextLoader.class)
@Transactional
public class SampleCategoryTest
{
    @PersistenceContext
    EntityManager entityManager;

    @Test
    public void generateCategorySingle()
    {
        Category category = new Category();
        category.setName("ROOT");
        category.setDescription("ROOT category description");
        entityManager.persist(category);
        entityManager.flush();
        Serializable new_id = category.getId();

        Category category_retrieved = entityManager.find(Category.class, new_id);
        assertNotNull(category_retrieved);
        assertEquals("Retrieved same as persisted", category.getName(), category_retrieved.getName());
    }

    @Test
    public void generateCategoryHierarchy()
    {
        Category category = new Category();
        category.setName("ROOT");
        category.setDescription("ROOT category description");
        entityManager.persist(category);
        entityManager.flush();


        Category chld = new Category();
        chld.setName("CHILD");
        chld.setDescription("Child category description.");
        
        category.addChildCategory(chld);
        entityManager.persist(chld);
        
        /*
        Serializable new_id = category.getId();

        Category category_retrieved = entityManager.find(Category.class, new_id);
        assertNotNull(category_retrieved);
        Set<?> set_child = category_retrieved.getChildCategories();
        assertNotNull(set_child);
        assertEquals(set_child.size(), 1);

        Category chld_retrieved = (Category) set_child.iterator().next();
        assertEquals("Retrieved same as persisted", category.getName(), category_retrieved.getName());
        assertEquals("Child Retrieved same as persisted", chld.getName(), chld_retrieved.getName());
        */

         
    }

}
