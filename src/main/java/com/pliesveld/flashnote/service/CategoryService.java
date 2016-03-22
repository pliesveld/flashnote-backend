package com.pliesveld.flashnote.service;

import com.pliesveld.flashnote.domain.Category;
import com.pliesveld.flashnote.exception.CategoryNotFoundException;
import com.pliesveld.flashnote.exception.CategorySearchException;

import java.util.List;

public interface CategoryService {
    Category getCategoryById(Integer id)                                throws CategoryNotFoundException;
    List<Category> rootCategories();
    List<Category> childCategories(int categoryId)                      throws CategoryNotFoundException;
    List<Category> categoriesHavingName(String categoryTerm)            throws CategorySearchException;
    List<Category> categoriesHavingDescription(String categoryTerm)     throws CategorySearchException;

    List<Category> allCategories();


}