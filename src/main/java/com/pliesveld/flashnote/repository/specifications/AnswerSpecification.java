package com.pliesveld.flashnote.repository.specifications;

import com.pliesveld.flashnote.domain.Answer;
import com.pliesveld.flashnote.domain.Answer_;
import org.springframework.data.jpa.domain.Specification;

final public class AnswerSpecification {
    private AnswerSpecification() {
    }

    public static Specification<Answer> contentContainsIgnoreCase(String searchTerm) {
        return (root, query, cb) -> {
            String containsLikePattern = getContainsLikePattern(searchTerm);

            return cb.like(cb.lower(root.<String>get(Answer_.content)), containsLikePattern);
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
