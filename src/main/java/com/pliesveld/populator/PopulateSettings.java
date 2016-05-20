package com.pliesveld.populator;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "flashnote.populate")
public class PopulateSettings {

    private int countQuestions;

    private int countStudents;

    private int countBanks;

    private int countQuestionPerBank;

    public PopulateSettings() {
    }

    public void setCountQuestions(int countQuestions) {
        this.countQuestions = countQuestions;
    }

    public int getCountQuestions() {
        return countQuestions;
    }

    public int getCountStudents() {
        return countStudents;
    }

    public void setCountStudents(int countStudents) {
        this.countStudents = countStudents;
    }

    public int getCountBanks() {
        return countBanks;
    }

    public void setCountBanks(int countBanks) {
        this.countBanks = countBanks;
    }

    public int getCountQuestionPerBank() {
        return countQuestionPerBank;
    }

    public void setCountQuestionPerBank(int countQuestionPerBank) {
        this.countQuestionPerBank = countQuestionPerBank;
    }
}
