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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;



@RunWith(SpringJUnit4ClassRunner.class)
@DefaultServiceTestAnnotations
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CategoryServiceTest extends AbstractTransactionalServiceUnitTest{
    private static final Logger LOG = LogManager.getLogger();

    @Autowired
    CategoryService categoryService;

    @Test
    public void whenCategoryCreate_thenCorrect()
    {
        Category category = new Category();
        category.setName("ROOT");
        category.setDescription("This is a category with a ROOT description");

        category = categoryService.createCategory(category);
        assertNotNull(category);
        assertNotNull(category.getId());
        assertEquals(1,categoryRepository.count());
        assertNotNull(category.getId());
    }

    @Test
    public void givenCategory_whenFindByName_thenCorrect()
    {
        Category category = new Category();
        category.setName("ROOT");
        category.setDescription("This is a category with a ROOT description");

        categoryService.createCategory(category);
        assertEquals(1,categoryRepository.count());

        List<Category> categoryList = categoryService.categoriesHavingName("ROOT");
        assertNotNull(categoryList);
        assertEquals(1,categoryList.size());
    }

    @Test(expected = CategorySearchException.class)
    public void givenCategory_whenFindByInvalidName_thenException()
    {
        Category category = new Category();
        category.setName("ROOT");
        category.setDescription("This is a category with a ROOT description");

        categoryService.createCategory(category);
        assertEquals(1,categoryRepository.count());

        List<Category> categoryList = categoryService.categoriesHavingName(" ROOT ");
        assertNotNull(categoryList);
        assertEquals(1,categoryList.size());
    }

    @Test
    public void givenCategory_whenCreateChildCategoryCascade_thenCorrect()
    {
        Category category = new Category();
        category.setName("ROOT");
        category.setDescription("This is a category with a ROOT description");

        Category category_child = new Category();
        category_child.setName("CHILD");
        category_child.setDescription("This is a category with a CHILD description");

        category_child.setParentCategory(category);
        categoryService.createCategory(category_child);
        assertEquals(2, categoryRepository.count());
    }

    @Test
    public void whenCreateChildCategoryMultiple_thenCorrect()
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

        Category category_child = new Category();
        category_child.setName("CHILD");
        category_child.setDescription("This is a category with a CHILD description");
        category_child.setParentCategory(category_root);
        categoryService.createCategory(category_child);

        assertEquals(6, categoryRepository.count());
        assertNotNull(category_root.getId());
        assertEquals(1,categoryRepository.findByParentCategory_id(category_root.getId()).size());
    }


}
