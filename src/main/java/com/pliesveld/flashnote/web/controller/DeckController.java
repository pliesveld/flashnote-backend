package com.pliesveld.flashnote.web.controller;


import com.pliesveld.flashnote.domain.Deck;
import com.pliesveld.flashnote.domain.StudentDetails;
import com.pliesveld.flashnote.exception.DeckNotFoundException;
import com.pliesveld.flashnote.exception.StudentNotFoundException;
import com.pliesveld.flashnote.model.json.response.CardStatistics;
import com.pliesveld.flashnote.service.CardService;
import com.pliesveld.flashnote.service.StudentService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/decks")
public class DeckController {
    private static final Logger LOG = LogManager.getLogger();

    @Autowired
    private StudentService studentService;

    @Autowired
    private CardService cardService;
    
    private StudentDetails verifyStudent(int id) throws StudentNotFoundException
    {
        StudentDetails studentDetails = studentService.findStudentDetailsById(id);
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

    @RequestMapping(value="", method = RequestMethod.GET)
    @ResponseBody @ResponseStatus(code = HttpStatus.OK)
    public List<Deck> retrieveAllDecks()
    {
        return cardService.findAllDecks();
    }

    @RequestMapping(value="/{id}", method = RequestMethod.GET)
    @ResponseBody @ResponseStatus(code = HttpStatus.OK)
    public Deck retrieveDeck(@PathVariable("id") int id)
    {
        return verifyDeck(id);
    }

    @RequestMapping(value="/count", method = RequestMethod.GET)
    public ResponseEntity<?> getCardStatistics()
    {
        LOG.info("Retrieving counts of all decks");
        CardStatistics cardStatistics = new CardStatistics();
        cardStatistics.setDeckCount(cardService.countDecks());
        cardStatistics.setFlashCardCount(cardService.countFlashCards());
        cardStatistics.setQuestionsCount(cardService.countQuestions());
        cardStatistics.setAnswersCount(cardService.countAnswers());
        return new ResponseEntity<>(cardStatistics,HttpStatus.OK);
    }



}
