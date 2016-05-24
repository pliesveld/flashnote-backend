package com.pliesveld.flashnote.repository.specifications;

import com.pliesveld.flashnote.domain.Category;
import com.pliesveld.flashnote.domain.Category_;
import org.springframework.data.jpa.domain.Specification;


final public class CategorySpecification {
    private CategorySpecification() {}

    public static Specification<Category> titleOrDescriptionContainsIgnoreCase(String searchTerm) {
        return (root, query, cb) -> {
            String containsLikePattern = getContainsLikePattern(searchTerm);

            return cb.or(
                    cb.like(cb.lower(root.<String>get(Category_.name)),containsLikePattern),
                    cb.like(cb.lower(root.<String>get(Category_.description)),containsLikePattern)
            );
        };
    }

    public static Specification<Category> categoryId(int categoryId) {
        return (root, query, cb) -> cb.and(cb.equal(root.<Integer>get(Category_.id),categoryId));

    }

    private static String getContainsLikePattern(String searchTerm) {

        if(searchTerm == null || (searchTerm = searchTerm.trim()).isEmpty()) {
            return "%";
        } else {
            return "%" + searchTerm.toLowerCase() + "%";
        }
    }
}
