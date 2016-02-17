package com.pliesveld.flashnote.web.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.pliesveld.flashnote.domain.Deck;
import com.pliesveld.flashnote.service.CardService;
import com.pliesveld.flashnote.service.StudentService;
import com.pliesveld.flashnote.web.dto.CardStatistics;

@RestController
@RequestMapping("/decks")
public class DeckController {
    private static final org.apache.logging.log4j.Logger LOG = org.apache.logging.log4j.LogManager.getLogger();

    @Autowired
    private StudentService studentService;

    @Autowired
    private CardService cardService;

    @RequestMapping(value="/count", method = RequestMethod.GET)
    public ResponseEntity<CardStatistics> entity_counts()
    {
        LOG.info("Retrieving counts of all decks");
        CardStatistics cardStatistics = new CardStatistics();
        cardStatistics.setDeckCount(cardService.countDecks());
        cardStatistics.setFlashCardCount(cardService.countFlashCards());
        cardStatistics.setQuestionsCount(cardService.countQuestions());
        cardStatistics.setAnswersCount(cardService.countAnswers());
        return new ResponseEntity<>(cardStatistics,HttpStatus.OK);
    }

    @RequestMapping(value="/{id}", method = RequestMethod.GET)
    @ResponseBody @ResponseStatus(code = HttpStatus.OK)
    public Deck retrieve_deck(@PathVariable("id") int id)
    {
        Deck deck = cardService.getDeckById(id);
        return deck;
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
