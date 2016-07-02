package com.pliesveld.populator.repository.reader;

import com.pliesveld.flashnote.domain.*;

import java.util.ArrayList;
import java.util.Collection;


public class DataSet {
    private Collection<Question> questions = new ArrayList<>();
    private Collection<Student> students = new ArrayList<>();
    private Collection<Category> categories = new ArrayList<>();
    private Collection<QuestionBank> questionBanks = new ArrayList<>();
    private Collection<Answer> answers = new ArrayList<>();
    private Collection<FlashCard> flashcards = new ArrayList<>();
    private Collection<Deck> decks = new ArrayList<>();

    public Collection<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(Collection<Question> questions) {
        this.questions = questions;
    }

    public Collection<Student> getStudents() {
        return students;
    }

    public void setStudents(Collection<Student> students) {
        this.students = students;
    }

    public Collection<Category> getCategories() {
        return categories;
    }

    public void setCategories(Collection<Category> categories) {
        this.categories = categories;
    }

    public Collection<QuestionBank> getQuestionBanks() {
        return questionBanks;
    }

    public void setQuestionBanks(Collection<QuestionBank> questionBanks) {
        this.questionBanks = questionBanks;
    }

    public Collection<Deck> getDecks() {
        return decks;
    }

    public void setDecks(Collection<Deck> decks) {
        this.decks = decks;
    }

    public Collection<FlashCard> getFlashcards() {
        return flashcards;
    }

    public void setFlashcards(Collection<FlashCard> flashcards) {
        this.flashcards = flashcards;
    }

    public Collection<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(Collection<Answer> answers) {
        this.answers = answers;
    }
}
