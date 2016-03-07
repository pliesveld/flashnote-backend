package com.pliesveld.flashnote.model.json.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pliesveld.flashnote.model.json.base.JsonWebResponseSerializable;
import com.pliesveld.flashnote.model.json.base.ModelBase;
import org.springframework.stereotype.Component;

/**
 * Reports database entity count for remote testing
 */

@Component
@JsonInclude(JsonInclude.Include.ALWAYS)
public class CardStatistics extends ModelBase implements JsonWebResponseSerializable {
    private Long deckCount;
    private Long flashCardCount;
    private Long questionsCount;
    private Long answersCount;

    public CardStatistics() {}

    public CardStatistics(Long deckCount, Long flashCardCount, Long questionsCount, Long answersCount) {
        this.deckCount = deckCount;
        this.flashCardCount = flashCardCount;
        this.questionsCount = questionsCount;
        this.answersCount = answersCount;
    }

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
