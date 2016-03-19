package com.pliesveld.flashnote.repository;

import com.pliesveld.flashnote.domain.FlashCard;
import com.pliesveld.flashnote.domain.FlashCardPrimaryKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlashCardRepository extends JpaRepository<FlashCard,FlashCardPrimaryKey> {

    List<FlashCard> findAllByQuestion(int id);
    List<FlashCard> findAllByAnswer(int id);
}
