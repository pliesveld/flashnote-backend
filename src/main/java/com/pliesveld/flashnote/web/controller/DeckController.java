package com.pliesveld.flashnote.web.controller;


import com.pliesveld.flashnote.domain.Deck;
import com.pliesveld.flashnote.domain.FlashCard;
import com.pliesveld.flashnote.domain.StudentDetails;
import com.pliesveld.flashnote.exception.DeckNotFoundException;
import com.pliesveld.flashnote.exception.StudentNotFoundException;
import com.pliesveld.flashnote.service.CardService;
import com.pliesveld.flashnote.service.StudentService;
import com.pliesveld.flashnote.web.dto.CardStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/deck")
public class DeckController {
    private static final org.apache.logging.log4j.Logger LOG = org.apache.logging.log4j.LogManager.getLogger();

    @Autowired
    private StudentService studentService;

    @Autowired
    private CardService cardService;
    
    private StudentDetails verifyStudent(int id) throws StudentNotFoundException
    {
        StudentDetails studentDetails = studentService.findById(id);
        if(studentDetails == null)
            throw new StudentNotFoundException(id);

        return studentDetails;
    }
    
    private Deck verifyDeck(int id) throws DeckNotFoundException
    {
        Deck deck = cardService.findDeckById(id);
        if(deck == null)
            throw new DeckNotFoundException(id);
        return deck;
    }

    @RequestMapping(value="/count", method = RequestMethod.GET)
    public ResponseEntity<?> entity_counts()
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
        LOG.info("Retreiving deck " + id);
        Deck deck = verifyDeck(id);
        return deck;
    }
    
    @RequestMapping(value="/{id}", method = RequestMethod.POST)
    public ResponseEntity<?> addflashcard(@PathVariable("id") int id)
    {
        Deck deck = verifyDeck(id);
        
        
        FlashCard fc = new FlashCard();
        
        HttpHeaders responseHeaders = new HttpHeaders();
        URI newStudentUri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}").query("card={value}").queryParam("card", fc.getId())
                .buildAndExpand(deck.getId())
                .toUri();

        responseHeaders.setLocation(newStudentUri);
        return new ResponseEntity<>(null,responseHeaders,HttpStatus.CREATED);
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
