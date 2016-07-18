package com.pliesveld.flashnote.repository.specifications;

import com.pliesveld.flashnote.domain.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.SetJoin;
import java.util.ArrayList;
import java.util.List;

final public class QuestionBankSpecification {
    private static final Logger LOG = LogManager.getLogger();

    private QuestionBankSpecification() {}

    public static Specification<QuestionBank> getFilterSpec(final Integer filterCategoryId, final String filterDescription, final String filterQuestionsContent, Integer questionId) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filterCategoryId != null) {
                predicates.add(hasCategory(filterCategoryId).toPredicate(root, query, cb));
            }

            if (StringUtils.hasText(filterDescription)) {
                predicates.add(descriptionContainsIgnoreCase(filterDescription).toPredicate(root, query, cb));
            }

            if (StringUtils.hasLength(filterQuestionsContent)) {
                predicates.add(questionsContentContainsIgnoreCase(filterQuestionsContent).toPredicate(root, query, cb));
            }

            if (questionId != null) {
                predicates.add(containsQuestion(questionId).toPredicate(root, query, cb));
            }

            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }

    public static Specification<QuestionBank> containsQuestion(final Integer questionId) {
        return (root, query, cb) -> {
            SetJoin<QuestionBank, Question> questionBankQuestionsJoin = root.join(QuestionBank_.questions);
            Expression<Integer> questionIdExp = questionBankQuestionsJoin.<Integer>get(Question_.id);
            return cb.equal(questionIdExp, questionId);
        };
    }

    public static Specification<QuestionBank> questionsContentContainsIgnoreCase(final String searchTerm) {
        return (root, query, cb) -> {
            String containsLikePattern = getContainsLikePattern(searchTerm);

            SetJoin<QuestionBank, Question> questionBankQuestionsJoin = root.join(QuestionBank_.questions);
            Expression<String> contentsExp = questionBankQuestionsJoin.<String>get(Question_.content);
            query.distinct(true);

            return cb.like(cb.lower(contentsExp), containsLikePattern);
        };
    }

    public static Specification<QuestionBank> descriptionContainsIgnoreCase(final String searchTerm) {
        return (root, query, cb) -> {
            String containsLikePattern = getContainsLikePattern(searchTerm);

            SetJoin<QuestionBank, Question> questionBankQuestionsJoin = root.join(QuestionBank_.questions);
            Expression<String> contentsExp = questionBankQuestionsJoin.<String>get(Question_.content);
            query.distinct(true);

            return cb.or(cb.like(cb.lower(root.<String>get(QuestionBank_.description)), containsLikePattern),
                         cb.like(cb.lower(contentsExp), containsLikePattern));
        };
    }

    private static String getContainsLikePattern(String searchTerm) {
        if (searchTerm == null || (searchTerm = searchTerm.trim()).isEmpty()) {
            return "%";
        } else {
            return "%" + searchTerm.toLowerCase() + "%";
        }
    }

    public static Specification<QuestionBank> hasCategory(final Integer categoryId) {
        return (root, query, cb) -> {
            Path<Category> categoryExp = root.join(QuestionBank_.category);
            Expression<Integer> categoryIdExp = categoryExp.<Integer>get(Category_.id);
            return cb.equal(categoryIdExp, categoryId);
        };
    }
}
