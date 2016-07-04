package com.pliesveld.flashnote.repository.specifications;

import com.pliesveld.flashnote.domain.Question;
import com.pliesveld.flashnote.domain.Question_;
import org.springframework.data.jpa.domain.Specification;

final public class QuestionSpecification {
    private QuestionSpecification() {
    }

    public static Specification<Question> contentContainsIgnoreCase(String searchTerm) {
        return (root, query, cb) -> {
            String containsLikePattern = getContainsLikePattern(searchTerm);

            return cb.like(cb.lower(root.<String>get(Question_.content)), containsLikePattern);
        };
    }

    private static String getContainsLikePattern(String searchTerm) {

        if (searchTerm == null || (searchTerm = searchTerm.trim()).isEmpty()) {
            return "%";
        } else {
            return "%" + searchTerm.toLowerCase() + "%";
        }
    }
}
