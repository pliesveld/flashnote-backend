package com.pliesveld.flashnote.repository;

import com.pliesveld.flashnote.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Integer> {
}
