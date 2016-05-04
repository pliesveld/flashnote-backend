package com.pliesveld.flashnote.unit.dao.spring;


import java.util.ArrayList;
import java.util.Collection;

public class DataSetExtractor implements DataExtractor {
    public Object getData(Object obj) {
        DataSet set = (DataSet) obj;
        Collection<Object> data = new ArrayList<Object>();

        for (Object o : set.getStudents()) {
            data.add(o);
        }

        for (Object o : set.getQuestions()) {
            data.add(o);
        }

        for (Object o : set.getCategories()) {
            data.add(o);
        }

        for (Object o : set.getQuestionBanks()) {
            data.add(o);
        }
        return data;
    }
}
