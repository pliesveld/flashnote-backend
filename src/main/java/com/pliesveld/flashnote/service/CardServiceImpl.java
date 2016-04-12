package com.pliesveld.flashnote.service;

import com.pliesveld.flashnote.domain.*;
import com.pliesveld.flashnote.exception.DeckNotFoundException;
import com.pliesveld.flashnote.exception.FlashCardCreateException;
import com.pliesveld.flashnote.exception.QuestionNotFoundException;
import com.pliesveld.flashnote.repository.*;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Service(value = "cardService")
public class CardServiceImpl implements CardService {
    
    private static final org.apache.logging.log4j.Logger LOG = org.apache.logging.log4j.LogManager.getLogger();

    @Autowired
    StatementRepository statementRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    CategoryRepository categoryRepositry;

    @Autowired
    FlashCardRepository flashCardRepository;

    @Autowired
    DeckRepository deckRepository;

    @Autowired
    QuestionBankRepository questionBankRepository;

    @PersistenceContext
    EntityManager entityManager;


    @Override
    public Question findQuestionById(int id) {
        return questionRepository.findOne(id);
    }

    @Override
    public Answer findAnswerById(int id) {
        return answerRepository.findOne(id);
    }

    @Override
    public AbstractStatement findStatementById(int id) {
        return statementRepository.findOneById(id);
    }

    @Override
    public Deck findDeckById(int id) throws DeckNotFoundException {
        Deck deck = deckRepository.findOne(id);
        if(deck == null)
            throw new DeckNotFoundException(id);

        deck.getFlashcards();
        deck.getCategory();
        deck.getAuthor();
        deck.getDescription();
        return deck;
    }

    @Override
    public List<FlashCard> findFlashCardsByContainingQuestionId(int questionId) throws QuestionNotFoundException {
        if(!questionRepository.exists(questionId))
            throw new QuestionNotFoundException(questionId);

        return flashCardRepository.findAllByQuestion(questionId);
    }

    @Override
    public Long countQuestions() {
        return questionRepository.count();
    }

    @Override
    public Long countAnswers() {
        return answerRepository.count();
    }

    @Override
    public Long countFlashCards() {
        return flashCardRepository.count();
    }

    @Override
    public Long countDecks() {
        return deckRepository.count();
    }

    @Override
    public Question createQuestion(Question question) {
        return questionRepository.save(question);
    }

    @Override
    public Answer createAnswer(Answer answer) {
        return answerRepository.save(answer);
    }

    @Override
    public FlashCard createFlashCard(Question question, Answer answer) {
                   
        FlashCard fc = new FlashCard(question,answer);
        flashCardRepository.save(fc);
        return fc;
    }

    @Override
    public FlashCard createFlashCardReferecingQuestion(int questionId, Answer answer) throws QuestionNotFoundException {
        if(!questionRepository.exists(questionId))
            throw new QuestionNotFoundException(questionId);

        if(!answerRepository.exists(answer.getId()))
        {
            answer.setId(null);
            answerRepository.save(answer);
        } else {
            FlashCard fc = flashCardRepository.findOne(new FlashCardPrimaryKey(questionId, answer.getId()));
            if(fc != null)
                throw new FlashCardCreateException(fc.getId());
        }

        Question question = questionRepository.findOne(questionId);
        return createFlashCard(question,answer);
    }

    @Override
    public void addToDeckFlashCard(Deck deck, FlashCard flashCard) {
        deck = entityManager.merge(deck);
        flashCard = entityManager.merge(flashCard);
        deck.getFlashcards().add(flashCard);
    }

    @Override
    public List<Deck> findAllDecks() {
        List<Deck> list = new ArrayList<>();
        deckRepository.findAll().forEach(list::add);
        return list;
    }

    public Deck createDeck(Deck deck) {
        return deckRepository.save(deck);
    }

    @Override
    public void deleteDeck(int id) {
        if(!deckRepository.exists(id))
            throw new DeckNotFoundException(id);
        deckRepository.delete(id);
    }

    @Override
    public List<QuestionBank> findAllQuestionBanks() {
        ArrayList<QuestionBank> list = new ArrayList<>();
        questionBankRepository.findAll().forEach(bank ->
        {
            //Hibernate.initialize(bank.getQuestions());
            bank.getQuestions().forEach(Question::getId);
            list.add(bank);

        });
        return list;
    }

    @Override
    public QuestionBank createQuestionBank(QuestionBank questionBank) {
        return questionBankRepository.save(questionBank);
    }

    @Override
    public QuestionBank findQuestionBankById(int id) {
        QuestionBank questionBank = questionBankRepository.findOne(id);
        questionBank.getId();
        questionBank.getDescription();
        Hibernate.initialize(questionBank.getQuestions());
        return questionBank;
    }

    @Override
    public void deleteQuestionBank(int id) {
        if(!questionBankRepository.exists(id))
            throw new QuestionNotFoundException(id);
        questionBankRepository.delete(id);
    }

    @Override
    public boolean doesCategoryIdExist(int id) {
        return categoryRepositry.exists(id);
    }
}
