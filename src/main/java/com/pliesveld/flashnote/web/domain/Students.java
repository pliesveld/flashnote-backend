package com.pliesveld.flashnote.web.domain;

import com.pliesveld.flashnote.domain.Student;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by happs on 1/18/16.
 */
@Component
public class Students {
    public Students() {
    }

    public Students(List<Student> arg) {
        setStudents(arg);
    }

    private List<Student> students;

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }
}
