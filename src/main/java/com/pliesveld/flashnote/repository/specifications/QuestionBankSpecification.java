package com.pliesveld.flashnote.repository.specifications;

import com.pliesveld.flashnote.domain.Question;
import com.pliesveld.flashnote.domain.QuestionBank;
import com.pliesveld.flashnote.domain.QuestionBank_;
import com.pliesveld.flashnote.domain.Question_;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.SetJoin;

final public class QuestionBankSpecification {
    private static final Logger LOG = LogManager.getLogger();
    private QuestionBankSpecification() {}

    public static Specification<QuestionBank> descriptionContainsIgnoreCase(String searchTerm) {

        return (root, query, cb) -> {
            String containsLikePattern = getContainsLikePattern(searchTerm);

            SetJoin<QuestionBank,Question> questionBankQuestionsJoin = root.join(QuestionBank_.questions);
            Expression<String> contentsExp = questionBankQuestionsJoin.<String>get(Question_.content);

//            LOG.debug("SetJoin : {}", questionBankQuestionsJoin);
//            LOG.debug("IdExp : {}", contentsExp);

            query.distinct(true);

           return
                   cb.or(cb.like(cb.lower(root.<String>get(QuestionBank_.description)), containsLikePattern),
                           cb.like(cb.lower(contentsExp), containsLikePattern));
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