package com.pliesveld.flashnote.repository;

import com.pliesveld.flashnote.domain.Category;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends CrudRepository<Category,Integer>, JpaSpecificationExecutor<Category> {
    List<Category> findByParentCategoryIsNull();
    List<Category> findByParentCategory_id(int parentCategory);
    Category        findOneByNameEquals(String categoryName);
    List<Category> findByNameContains(String categoryTerm);
    List<Category> findByDescriptionContains(String categoryTerm);
}
