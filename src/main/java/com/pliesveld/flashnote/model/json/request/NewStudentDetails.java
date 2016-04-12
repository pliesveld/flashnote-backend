package com.pliesveld.flashnote.model.json.request;

import com.pliesveld.flashnote.domain.Student;
import com.pliesveld.flashnote.domain.StudentRole;
import com.pliesveld.flashnote.model.json.base.JsonWebRequestSerializable;
import com.pliesveld.flashnote.model.json.base.ModelBase;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Subset of studentDetails model for account creation
 */

public class NewStudentDetails extends ModelBase implements JsonWebRequestSerializable
{
    @Size(min = 3,max = 32) @NotNull
    private String name;
    
    @Email @NotNull @Size(min = 5,max = 48)
    private String email;
    
    @Size(min = 1, max = 60) @NotNull
    private String password;

    @NotNull
    private StudentRole role;

    public NewStudentDetails() {
    }

    public static NewStudentDetails convert(Student student) {
        if (student == null)
            throw new NullPointerException("studentDetails argument was null");

        NewStudentDetails newStudent = new NewStudentDetails();
//        newStudent.setName(studentDetails.getDescription());
        newStudent.setEmail(student.getEmail());
        newStudent.setPassword(student.getPassword());
        newStudent.setRole(student.getRole());

        if (student.getStudentDetails() != null)
            newStudent.setName(student.getStudentDetails().getName());

        return newStudent;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public StudentRole getRole() { return this.role; }

    public void setRole(StudentRole role) { this.role = role; }
}
