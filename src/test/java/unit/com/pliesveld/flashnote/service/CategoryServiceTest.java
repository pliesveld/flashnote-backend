package com.pliesveld.flashnote.service;

import com.pliesveld.flashnote.domain.Category;
import com.pliesveld.flashnote.exception.CategorySearchException;
import com.pliesveld.flashnote.repository.CategoryRepository;
import com.pliesveld.flashnote.spring.DefaultServiceTestAnnotations;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by happs on 1/15/16.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@DefaultServiceTestAnnotations
@Transactional
public class CategoryServiceTest {
    private static final Logger LOG = LogManager.getLogger();

    @Autowired
    CategoryService categoryService;

    @Autowired
    CategoryRepository categoryRepository;

    @Test
    public void categoryCreation()
    {
        Category category = new Category();
        category.setName("ROOT");
        category.setDescription("This is a category with a ROOT description");

        categoryRepository.save(category);
        assertEquals(1,categoryRepository.count());
        assertNotNull(category.getId());
    }

    @Test
    public void categorySearch()
    {
        Category category = new Category();
        category.setName("ROOT");
        category.setDescription("This is a category with a ROOT description");

        categoryRepository.save(category);
        assertEquals(1,categoryRepository.count());

        List<Category> categoryList = categoryService.categoriesHavingName ("ROOT");
        assertNotNull(categoryList);
        assertEquals(1,categoryList.size());
    }

    @Test(expected = CategorySearchException.class)
    public void invalidQueryTerms()
    {
        Category category = new Category();
        category.setName("ROOT");
        category.setDescription("This is a category with a ROOT description");

        categoryRepository.save(category);
        assertEquals(1,categoryRepository.count());

        List<Category> categoryList = categoryService.categoriesHavingName (" ROOT ");
        assertNotNull(categoryList);
        assertEquals(1,categoryList.size());
    }

    @Test
    public void categoryCreationCascade()
    {
        Category category = new Category();
        category.setName("ROOT");
        category.setDescription("This is a category with a ROOT description");

        Category category_child = new Category();
        category_child.setName("CHILD");
        category_child.setDescription("This is a category with a CHILD description");

        category_child.setParentCategory(category);
        categoryRepository.save(category_child);
        assertEquals(2, categoryRepository.count());
    }

    @Test
    public void childCategories()
    {
        List<String> roots = Arrays.asList("ROOT1", "ROOT2","ROOT3","ROOT4");

        /* make some roots */
        roots.forEach((s) -> {
            Category category = new Category();
            category.setName(s);
            category.setDescription("Description for " + s);
            categoryRepository.save(category);
        });


        Category category_root = new Category();
        category_root.setName("ROOT THAT HAS CHILDREN");
        category_root.setDescription("This is a category with a ROOT description");
        categoryRepository.save(category_root);

        Category category_child = new Category();
        category_child.setName("CHILD");
        category_child.setDescription("This is a category with a CHILD description");
        category_child.setParentCategory(category_root);
        categoryRepository.save(category_child);


        assertEquals(6, categoryRepository.count());
//        assertEquals(5,categoryRepository.findByParentCategoryIsNull().size());
        assertNotNull(category_root.getId());
        assertEquals(1,categoryRepository.findByParentCategory_id(category_root.getId()).size());

    }


}
