package com.pliesveld.flashnote.web.controller;


import com.fasterxml.jackson.annotation.JsonView;
import com.pliesveld.flashnote.domain.Category;
import com.pliesveld.flashnote.domain.Deck;
import com.pliesveld.flashnote.domain.Student;
import com.pliesveld.flashnote.exception.CategoryNotFoundException;
import com.pliesveld.flashnote.exception.DeckNotFoundException;
import com.pliesveld.flashnote.exception.StudentNotFoundException;
import com.pliesveld.flashnote.model.json.Views;
import com.pliesveld.flashnote.model.json.response.CardStatistics;
import com.pliesveld.flashnote.repository.specifications.DeckSpecification;
import com.pliesveld.flashnote.service.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import javax.validation.Valid;

@RestController
@RequestMapping("/decks")
public class DeckController {
    private static final Logger LOG = LogManager.getLogger();

    @Autowired
    private StudentService studentService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CardService cardService;

    @Autowired
    private DeckService deckService;

    @Autowired
    private AttachmentService attachmentService;

    private Student verifyStudent(int id) throws StudentNotFoundException {
        Student student = studentService.findStudentById(id);
        if (student == null)
            throw new StudentNotFoundException(id);

        return student;
    }

    private Deck verifyDeck(int id) throws DeckNotFoundException {
        Deck deck = deckService.findDeckById(id);
        if (deck == null)
            throw new DeckNotFoundException(id);
        return deck;
    }

    @RequestMapping(value = "", method = RequestMethod.GET, params = "!categoryId")
    @ResponseStatus(code = HttpStatus.OK)
    @JsonView(Views.Summary.class)
    public Page<Deck> retrieveAllDecks(Pageable pageRequest) {
        return deckService.browseDecks(pageRequest);
    }

    @RequestMapping(value = "", method = RequestMethod.GET, params = "categoryId")
    @ResponseStatus(code = HttpStatus.OK)
    @JsonView(Views.Summary.class)
    public Page<Deck> retrieveAllDecksInCategory(@RequestParam("categoryId") int categoryId, Pageable pageRequest) {
        final Specification<Deck> spec = DeckSpecification.hasCategory(categoryId);
        return deckService.browseDecksWithSpec(spec, pageRequest);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity createDeck(@Valid @RequestBody Deck deck) {
        Category category = deck.getCategory();
        if (category == null || category.getId() == null) {
            throw new CategoryNotFoundException(0);
        }

        int id = category.getId();
        if (!categoryService.doesCategoryIdExist(id)) {

            throw new CategoryNotFoundException(id);
        }

        deck = deckService.createDeck(deck);

        return ResponseEntity.created(
                MvcUriComponentsBuilder.fromController(DeckController.class)
                        .path("/{id}").buildAndExpand(deck.getId()).toUri()).build();
    }

    @RequestMapping(value="", method = RequestMethod.PUT)
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity updateDeck(@Valid @RequestBody Deck deck)
    {
        Category category = deck.getCategory();
        if(category == null || category.getId() == null)
        {
            throw new CategoryNotFoundException(0);
        }

        int id = category.getId();
        if(!categoryService.doesCategoryIdExist(id))
        {

            throw new CategoryNotFoundException(id);
        }

        deck = deckService.createDeck(deck);

        return ResponseEntity.created(
                MvcUriComponentsBuilder.fromController(DeckController.class)
                        .path("/{id}").buildAndExpand(deck.getId()).toUri()).build();
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    @ResponseStatus(code = HttpStatus.OK)
    public Deck retrieveDeck(@PathVariable("id") int id) {
        return verifyDeck(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(code = HttpStatus.OK)
    public void deleteDeck(@PathVariable("id") int id) {
        deckService.deleteDeck(id);
    }

    @RequestMapping(value = "/count", method = RequestMethod.GET)
    public ResponseEntity<?> getCardStatistics() {
        LOG.info("Retrieving counts of all decks");
        CardStatistics cardStatistics = new CardStatistics();
        cardStatistics.setDeckCount(cardService.countDecks());
        cardStatistics.setQuestionBankCount(cardService.countQuestionBanks());
        cardStatistics.setFlashCardCount(cardService.countFlashCards());
        cardStatistics.setQuestionsCount(cardService.countQuestions());
        cardStatistics.setAnswersCount(cardService.countAnswers());
        cardStatistics.setAnswersCount(attachmentService.countAttachments());
        return new ResponseEntity<>(cardStatistics, HttpStatus.OK);
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public Page<Deck> findBySearchTerm(@RequestParam("query") String searchTerm, Pageable pageRequest) {
        return deckService.findBySearchTerm(searchTerm, pageRequest);
    }

}
