package com.pliesveld.flashnote.domain;

import com.pliesveld.flashnote.unit.spring.DefaultEntityTestAnnotations;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@DefaultEntityTestAnnotations
@Transactional
public class CategoryTest extends AbstractDomainEntityUnitTest
{
    @PersistenceContext
    EntityManager entityManager;

    Serializable category_id;

    @Before
    public void setupEntities()
    {
        Category category = categoryBean();
        category = categoryRepository.save(category);
        category_id = category.getId();
        entityManager.flush();
        entityManager.clear();
    }

    protected Category categoryFromTest()
    {
        assertNotNull(category_id);
        Category category = entityManager.find(Category.class,category_id);
        assertNotNull(category);
        return category;
    }

    @Test
    public void testEntitySanity()
    {
        assertNotNull(category_id);
        Category category = entityManager.find(Category.class,category_id);
        assertNotNull(category);
        assertCategoryRepositoryCount(1);
    }

    @After
    public void flushAfter()
    {
        entityManager.flush();
    }

    @Test
    public void generateCategoryHierarchy()
    {
        Category root = entityManager.find(Category.class, category_id);

        Category child1 = categoryBean();
        Category child2 = categoryBean();
        Category child3 = categoryBean();

        root.addChildCategory(child1);
        root.addChildCategory(child2);
        root.addChildCategory(child3);

        entityManager.persist(child1);
        entityManager.persist(child2);
        entityManager.persist(child3);

        assertNotNull(child1.getId());
        assertNotNull(child2.getId());
        assertNotNull(child3.getId());

        assertCategoryRepositoryCount(4);

    }

    @Test
    public void generateCategoryHierarchyCascade()
    {
        Category root = entityManager.find(Category.class, category_id);

        Category child1 = categoryBean();
        Category child2 = categoryBean();
        Category child3 = categoryBean();

        root.addChildCategory(child1);
        root.addChildCategory(child2);
        root.addChildCategory(child3);

        entityManager.persist(root);

        assertNotNull(child1.getId());
        assertNotNull(child2.getId());
        assertNotNull(child3.getId());

        assertCategoryRepositoryCount(4);

    }

}
