package com.pliesveld.flashnote.web.controller;

import com.pliesveld.flashnote.domain.Answer;
import com.pliesveld.flashnote.domain.Deck;
import com.pliesveld.flashnote.domain.FlashCard;
import com.pliesveld.flashnote.domain.Question;
import com.pliesveld.flashnote.exception.DeckNotFoundException;
import com.pliesveld.flashnote.service.CardService;
import com.pliesveld.flashnote.service.DeckService;
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

    @Autowired
    private DeckService deckService;

    private Deck verifyDeck(int id) throws DeckNotFoundException {
        Deck deck = deckService.findDeckById(id);
        if (deck == null)
            throw new DeckNotFoundException(id);
        return deck;
    }

    @RequestMapping(value = "/deck/{id}", method = RequestMethod.POST)
    public ResponseEntity<?> processDeckCreation(@PathVariable("id") int id,
                                                 @RequestParam(name = "questionId", required = true) int questionId,
                                                 @RequestParam(name = "answerId", required = true) int answerId) {
        Deck deck = verifyDeck(id);

        Question question = cardService.findQuestionById(questionId);
        Answer answer = cardService.findAnswerById(answerId);

        FlashCard fc = new FlashCard(question, answer);
        deckService.updateDeckAddFlashCard(id, fc);

        HttpHeaders responseHeaders = new HttpHeaders();
        URI newStudentUri = MvcUriComponentsBuilder
                .fromController(DeckController.class)
                .path("/{id}").query("card={value}").queryParam("card", fc.getId())

                .path("/flashcard/{fc}")
                .buildAndExpand(deck.getId(), fc.getId())
                .toUri();

        responseHeaders.setLocation(newStudentUri);
        return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/question", method = RequestMethod.POST)
    public ResponseEntity<?> createQuestion(@Valid @RequestBody Question question) {
        Question que = cardService.createQuestion(question);

        HttpHeaders responseHeaders = new HttpHeaders();
        URI newQuestionUri = MvcUriComponentsBuilder
                .fromController(QuestionBankController.class)
                .path("/questions/{id}")
                .buildAndExpand(que.getId())
                .toUri();

        responseHeaders.setLocation(newQuestionUri);
        return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
    }


    @RequestMapping(value = "/answer", method = RequestMethod.POST)
    public ResponseEntity<?> createAnswer(@Valid @RequestBody Answer answer) {
        Answer ans = cardService.createAnswer(answer);

        HttpHeaders responseHeaders = new HttpHeaders();
        URI newAnswerUri = MvcUriComponentsBuilder
                .fromController(QuestionBankController.class)
                .path("/answers/{id}")
                .buildAndExpand(ans.getId())
                .toUri();

        responseHeaders.setLocation(newAnswerUri);
        return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
    }
}
