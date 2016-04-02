package com.pliesveld.flashnote.web.controller;

import com.pliesveld.flashnote.domain.Answer;
import com.pliesveld.flashnote.domain.Deck;
import com.pliesveld.flashnote.domain.FlashCard;
import com.pliesveld.flashnote.domain.Question;
import com.pliesveld.flashnote.exception.DeckNotFoundException;
import com.pliesveld.flashnote.service.CardService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
public class ConstructController {
    private static final Logger LOG = LogManager.getLogger();

    @Autowired
    private CardService cardService;

    private Deck verifyDeck(int id) throws DeckNotFoundException
    {
        Deck deck = cardService.findDeckById(id);
        if(deck == null)
            throw new DeckNotFoundException(id);
        return deck;
    }

    @RequestMapping(value="/deck/{id}", method = RequestMethod.POST)
    public ResponseEntity<?> processDeckCreation(@PathVariable("id") int id,
                                                 @RequestParam(name = "question_id", required = true) int question_id,
                                                 @RequestParam(name = "answer_id", required = true) int answer_id)
    {
        Deck deck = verifyDeck(id);

        Question question = cardService.findQuestionById(question_id);
        Answer answer = cardService.findAnswerById(answer_id);

        FlashCard fc = cardService.createFlashCard(question,answer);

        cardService.addToDeckFlashCard(deck,fc);

        HttpHeaders responseHeaders = new HttpHeaders();
        URI newStudentUri = MvcUriComponentsBuilder
                .fromController(DeckController.class)
                .path("/{id}").query("card={value}").queryParam("card", fc.getId())

                .path("/flashcard/{fc}")
                .buildAndExpand(deck.getId(),fc.getId())
                .toUri();

        responseHeaders.setLocation(newStudentUri);
        return new ResponseEntity<>(null,responseHeaders, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/question", method = RequestMethod.POST)
    public ResponseEntity<?> createQuestion(@Valid @RequestBody Question question)
    {
        Question que = cardService.createQuestion(question);

        HttpHeaders responseHeaders = new HttpHeaders();
        URI newQuestionUri = MvcUriComponentsBuilder
                .fromController(StatementController.class)
                .path("/questions/{id}")
                .buildAndExpand(que.getId())
                .toUri();

        responseHeaders.setLocation(newQuestionUri);
        return new ResponseEntity<>(null,responseHeaders,HttpStatus.CREATED);
    }


    @RequestMapping(value = "/answer", method = RequestMethod.POST)
    public ResponseEntity<?> createAnswer(@Valid @RequestBody Answer answer)
    {
        Answer ans = cardService.createAnswer(answer);

        HttpHeaders responseHeaders = new HttpHeaders();
        URI newAnswerUri = MvcUriComponentsBuilder
                .fromController(StatementController.class)
                .path("/answers/{id}")
                .buildAndExpand(ans.getId())
                .toUri();

        responseHeaders.setLocation(newAnswerUri);
        return new ResponseEntity<>(null,responseHeaders,HttpStatus.CREATED);
    }


}