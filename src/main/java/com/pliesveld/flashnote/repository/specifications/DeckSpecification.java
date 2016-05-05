package com.pliesveld.flashnote.repository.specifications;

import com.pliesveld.flashnote.domain.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Path;

final public class DeckSpecification {
    private static final Logger LOG = LogManager.getLogger();
    private DeckSpecification() {}

    public static Specification<Deck> descriptionOrFlashcardContainsIgnoreCase(String searchTerm) {

        return (root, query, cb) -> {
            final String containsLikePattern = getContainsLikePattern(searchTerm);

            ListJoin<Deck,FlashCard> flashCardListJoin = root.join(Deck_.flashcards);

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

        if(searchTerm == null || (searchTerm = searchTerm.trim()).isEmpty()) {
            return "%";
        } else {
            return "%" + searchTerm.toLowerCase() + "%";
        }
    }
}
