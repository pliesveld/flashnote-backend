package com.pliesveld.flashnote.service;

import com.pliesveld.flashnote.domain.Category;
import com.pliesveld.flashnote.exception.CategoryNotFoundException;
import com.pliesveld.flashnote.exception.CategorySearchException;
import com.pliesveld.flashnote.repository.CategoryRepository;
import com.pliesveld.flashnote.repository.specifications.CategorySpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service("categoryService")
public class CategoryServiceImpl implements CategoryService {
    private static final Logger LOG = LogManager.getLogger();

    @Autowired
    private CategoryRepository categoryRepository;

    private void verifyCategoryTerm(String categoryTerm) throws CategorySearchException {
        if (StringUtils.containsWhitespace(categoryTerm))
            throw new CategorySearchException(categoryTerm);
    }

    @Override
    public Category getCategoryById(int id) throws CategoryNotFoundException {
        Category category = categoryRepository.findOne(id);
        if (category == null) {
            throw new CategoryNotFoundException(id);
        }
        return category;
    }

    @Override
    public boolean doesCategoryIdExist(int id) {
        return categoryRepository.exists(id);
    }

    @Override
    public List<Category> rootCategories() {
        return categoryRepository.findByParentCategoryIsNull();
    }

    @Override
    public List<Category> childCategories(int categoryId) {
        if (!categoryRepository.exists(categoryId))
            throw new CategoryNotFoundException(categoryId);
        return categoryRepository.findByParentCategory_id(categoryId);
    }

    @Override
    public List<Category> categoriesHavingName(String categoryTerm) {
        verifyCategoryTerm(categoryTerm);
        return categoryRepository.findByNameContains(categoryTerm);
    }

    @Override
    public List<Category> categoriesHavingDescription(String categoryTerm) {
        verifyCategoryTerm(categoryTerm);
        return categoryRepository.findByDescriptionContains(categoryTerm);
    }

    @Override
    public List<Category> allCategories() {
        List<Category> list = new ArrayList<Category>();
        categoryRepository.findAll().forEach(list::add);
        return list;
    }

    @Override
    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Page<Category> findBySearchTerm(String searchTerm, Pageable pageRequest) {
        Specification<Category> searchSpec = CategorySpecification.titleOrDescriptionContainsIgnoreCase(searchTerm);
        return categoryRepository.findAll(searchSpec, pageRequest);
    }


}
