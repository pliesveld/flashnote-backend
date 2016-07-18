package com.pliesveld.flashnote.repository.specifications;

import com.pliesveld.flashnote.domain.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

final public class DeckSpecification {
    private static final Logger LOG = LogManager.getLogger();

    private DeckSpecification() {}

    public static Specification<Deck> getFilterSpec(final Integer filterCategoryId, final String filterDescription,
                                                    final String filterFlashcardContent) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filterCategoryId != null) {
                predicates.add(hasCategory(filterCategoryId).toPredicate(root, query, cb));
            }

            if (StringUtils.hasText(filterDescription)) {
                predicates.add(descriptionContainsIgnoreCase(filterDescription).toPredicate(root, query, cb));
            }

            if (StringUtils.hasText(filterFlashcardContent)) {
                predicates.add(flashcardContainsIgnoreCase(filterFlashcardContent).toPredicate(root, query, cb));
            }
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }

    public static Specification<Deck> descriptionContainsIgnoreCase(String searchTerm) {
        return (root, query, cb) -> {
            final String containsLikePattern = getContainsLikePattern(searchTerm);
            return cb.like(cb.lower(root.<String>get(Deck_.description)), containsLikePattern);
        };
    }

    public static Specification<Deck> flashcardContainsIgnoreCase(String searchTerm) {
        return (root, query, cb) -> {
            final String containsLikePattern = getContainsLikePattern(searchTerm);

            ListJoin<Deck, FlashCard> flashCardListJoin = root.join(Deck_.flashcards);

            Path<Question> questionExp = flashCardListJoin.<Question>get(FlashCard_.question);
            Path<Answer> answerExp = flashCardListJoin.<Answer>get(FlashCard_.answer);
            Expression<String> questionContentExp = questionExp.<String>get(Question_.content);
            Expression<String> answerContentExp = answerExp.<String>get(Answer_.content);

            query.distinct(true);
            return cb.or(cb.like(cb.lower(questionContentExp), containsLikePattern),
                         cb.like(cb.lower(answerContentExp), containsLikePattern));
        };
    }

    @Deprecated
    public static Specification<Deck> descriptionOrFlashcardContainsIgnoreCase(String searchTerm) {
        return (root, query, cb) -> {
            final String containsLikePattern = getContainsLikePattern(searchTerm);

            ListJoin<Deck, FlashCard> flashCardListJoin = root.join(Deck_.flashcards);

            Path<Question> questionExp = flashCardListJoin.<Question>get(FlashCard_.question);
            Path<Answer> answerExp = flashCardListJoin.<Answer>get(FlashCard_.answer);

            Expression<String> questionContentExp = questionExp.<String>get(Question_.content);
            Expression<String> answerContentExp = answerExp.<String>get(Answer_.content);

            query.distinct(true);
            return
                    cb.or(cb.like(cb.lower(root.<String>get(Deck_.description)), containsLikePattern),
                          cb.like(cb.lower(questionContentExp), containsLikePattern),
                          cb.like(cb.lower(answerContentExp), containsLikePattern));
        };
    }

    private static String getContainsLikePattern(String searchTerm) {
        if (searchTerm == null || (searchTerm = searchTerm.trim()).isEmpty()) {
            return "%";
        } else {
            return "%" + searchTerm.toLowerCase() + "%";
        }
    }

    public static Specification<Deck> hasCategory(final Integer categoryId) {
        return (root, query, cb) -> {
            Path<Category> categoryExp = root.join(Deck_.category);
            Expression<Integer> categoryIdExp = categoryExp.<Integer>get(Category_.id);
            return cb.equal(categoryIdExp, categoryId);
        };
    }
}

