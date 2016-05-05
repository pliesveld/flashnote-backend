package com.pliesveld.flashnote.service;

import com.pliesveld.flashnote.domain.Category;
import com.pliesveld.flashnote.exception.CategoryNotFoundException;
import com.pliesveld.flashnote.exception.CategorySearchException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;

@Validated
@Transactional(readOnly = true)
public interface CategoryService {
    Category getCategoryById(Integer id)                                throws CategoryNotFoundException;
    List<Category> rootCategories();
    List<Category> childCategories(int categoryId)                      throws CategoryNotFoundException;
    List<Category> categoriesHavingName(String categoryTerm)            throws CategorySearchException;
    List<Category> categoriesHavingDescription(String categoryTerm)     throws CategorySearchException;

    Page<Category> findBySearchTerm(String searchTerm, Pageable pageRequest);
    List<Category> allCategories();

    @Transactional
    Category createCategory(@Valid Category category);
}