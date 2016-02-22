package com.pliesveld.flashnote.service;

import com.pliesveld.flashnote.domain.Category;
import com.pliesveld.flashnote.exception.CategoryNotFoundException;
import com.pliesveld.flashnote.exception.CategorySearchException;
import com.pliesveld.flashnote.repository.CategoryRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service("categoryService")
public class CategoryServiceImpl implements CategoryService {
    private static final Logger LOG = LogManager.getLogger();

    @Autowired
    CategoryRepository categoryRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Category> rootCategories() {
        return categoryRepository.findByParentCategoryIsNull();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Category> childCategories(int categoryId) {
        if(!categoryRepository.exists(categoryId))
            throw new CategoryNotFoundException(categoryId);

        return categoryRepository.findByParentCategory_id(categoryId);
    }

    private void verifyCategoryTerm(String categoryTerm) throws CategorySearchException
    {
        if(StringUtils.containsWhitespace(categoryTerm))
            throw new CategorySearchException(categoryTerm);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Category> categoriesHavingName(String categoryTerm) {
        verifyCategoryTerm(categoryTerm);
        return categoryRepository.findByNameContains(categoryTerm);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Category> categoriesHavingDescription(String categoryTerm) {
        verifyCategoryTerm(categoryTerm);
        return categoryRepository.findByDescriptionContains(categoryTerm);
    }
}
