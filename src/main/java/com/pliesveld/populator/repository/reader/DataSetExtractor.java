package com.pliesveld.populator.repository.reader;


import java.util.ArrayList;
import java.util.Collection;

public class DataSetExtractor implements DataExtractor {
    public Object getData(Object obj) {
        DataSet set = (DataSet) obj;
        Collection<Object> data = new ArrayList<Object>();

        for (Object o : set.getTextAttachments()) {
            data.add(o);
        }

        for (Object o : set.getStudents()) {
            data.add(o);
        }

        for (Object o : set.getQuestions()) {
            data.add(o);
        }

        for (Object o : set.getCategories()) {
            data.add(o);
        }

        for (Object o : set.getAnswers()) {
            data.add(o);
        }

        for (Object o : set.getFlashcards()) {
            data.add(o);
        }

        for (Object o : set.getQuestionBanks()) {
            data.add(o);
        }

        for (Object o : set.getDecks()) {
            data.add(o);
        }

        return data;
    }
}
