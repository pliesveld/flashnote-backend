package com.pliesveld.flashnote.domain.dto;

import com.pliesveld.flashnote.domain.Student;
import com.pliesveld.flashnote.domain.StudentRole;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Subset of studentDetails model for account creation
 */

public class StudentDTO implements Serializable
{
    @Size(min = 3,max = 32) @NotNull
    private String name;
    
    @Email @NotNull @Size(min = 5,max = 48)
    private String email;
    
    @Size(min = 1, max = 60) @NotNull
    private String password;

    @NotNull
    private StudentRole role = StudentRole.ROLE_ACCOUNT;

    public StudentDTO() {
    }

    public static StudentDTO convert(Student student) {
        if (student == null)
            throw new NullPointerException("studentDetails argument was null");

        StudentDTO studentDTO = new StudentDTO();
//        studentDTO.setName(studentDetails.getTitle());
        studentDTO.setEmail(student.getEmail());
        studentDTO.setPassword(student.getPassword());
        studentDTO.setRole(student.getRole());

        if (student.getStudentDetails() != null)
            studentDTO.setName(student.getStudentDetails().getName());

        return studentDTO;
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
