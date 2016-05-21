package com.pliesveld.flashnote.spring;

import com.pliesveld.flashnote.domain.*;

import java.util.Collection;

/**
 * Created by happs on 5/1/16.
 */
public class DataSet {
    private Collection<Question> questions;
    private Collection<Student> students;
    private Collection<Category> categories;
    private Collection<QuestionBank> questionBanks;
    private Collection<Deck> decks;

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

    public Collection<Deck> getDecks() { return decks; }

    public void setDecks(Collection<Deck> decks) { this.decks = decks; }
}
