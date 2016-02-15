package com.pliesveld.flashnote.web.controller;


import com.pliesveld.flashnote.domain.Deck;
import com.pliesveld.flashnote.domain.Student;
import com.pliesveld.flashnote.exception.StudentNotFoundException;
import com.pliesveld.flashnote.service.CardService;
import com.pliesveld.flashnote.service.StudentService;
import com.pliesveld.flashnote.web.dto.CardStatistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Set;

@RestController
@RequestMapping("/decks")
public class DeckController {
    final Logger logger = LoggerFactory.getLogger(DeckController.class);

    @Autowired
    private StudentService studentService;

    @Autowired
    private CardService cardService;

    @RequestMapping(value="/count", method = RequestMethod.GET)
    public ResponseEntity<CardStatistics> entity_counts()
    {
        logger.info("Retrieving counts of all decks");
        CardStatistics cardStatistics = new CardStatistics();
        cardStatistics.setDeckCount(cardService.countDecks());
        cardStatistics.setFlashCardCount(cardService.countFlashCards());
        cardStatistics.setQuestionsCount(cardService.countQuestions());
        cardStatistics.setAnswersCount(cardService.countAnswers());
        return new ResponseEntity<>(cardStatistics,HttpStatus.OK);
    }


/*
Alternatives to apache.commons.collections

Iterator<T> source = ...;
List<T> target = new ArrayList<>();
source.forEachRemaining(target::add);

Iterable<T> source = ...;
source.forEach(target::add);


public static <T> List<T> toList(final Iterable<T> iterable) {
    return StreamSupport.stream(iterable.spliterator(), false)
                        .collect(Collectors.toList());
}
 */

}
