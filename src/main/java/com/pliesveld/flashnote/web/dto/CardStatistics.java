package com.pliesveld.flashnote.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.stereotype.Component;

/**
 * Created by happs on 1/25/16.
 */

@Component
@JsonInclude(JsonInclude.Include.ALWAYS)
public class CardStatistics {
    private Long deckCount;
    private Long flashCardCount;
    private Long questionsCount;
    private Long answersCount;

    public void setDeckCount(Long deckCount) {
        this.deckCount = deckCount;
    }

    public void setFlashCardCount(Long flashCardCount) {
        this.flashCardCount = flashCardCount;
    }

    public void setQuestionsCount(Long questionsCount) {
        this.questionsCount = questionsCount;
    }

    public void setAnswersCount(Long answerssCount) {
        this.answersCount = answerssCount;
    }

    public Long getDeckCount() {
        return deckCount;
    }

    public Long getFlashCardCount() {
        return flashCardCount;
    }

    public Long getQuestionsCount() {
        return questionsCount;
    }

    public Long getAnswersCount() {
        return answersCount;
    }
}
