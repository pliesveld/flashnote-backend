package com.pliesveld.flashnote.repository;

import com.pliesveld.flashnote.domain.FlashCard;
import com.pliesveld.flashnote.domain.FlashCardPrimaryKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FlashCardRepository extends JpaRepository<FlashCard,FlashCardPrimaryKey> {

    List<FlashCard> findAllByQuestion(int id);
    List<FlashCard> findAllByAnswer(int id);
}
