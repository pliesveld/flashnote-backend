package com.pliesveld.flashnote.dao;

import com.pliesveld.flashnote.domain.Category;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public class CategoryDao extends GenericDao<Category> {
    public CategoryDao() {
        setPersistentClass(Category.class);
    }
}

