package com.pliesveld.flashnote.repository;

import com.pliesveld.flashnote.domain.Category;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CategoryRepository extends CrudRepository<Category,Integer> {
    List<Category> findByParentCategoryIsNull();
    List<Category> findByParentCategory_id(int parentCategory);
    List<Category> findByNameContains(String categoryTerm);
    List<Category> findByDescriptionContains(String categoryTerm);
}
