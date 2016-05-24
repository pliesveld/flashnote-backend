package com.pliesveld.flashnote.model.json.response;

/**
 * @author Patrick Liesveld
 */

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.pliesveld.flashnote.domain.Student;
import com.pliesveld.flashnote.domain.StudentRole;
import com.pliesveld.flashnote.model.json.Views;
import com.pliesveld.flashnote.model.json.base.JsonWebRequestSerializable;
import com.pliesveld.flashnote.model.json.base.ModelBase;
import org.hibernate.validator.constraints.Email;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


/**
 * Transfer object of the student account that removes the user's password
 */
@Component
@JsonInclude(JsonInclude.Include.ALWAYS)
public class ExistingStudentDetails extends ModelBase implements JsonWebRequestSerializable
{
    @NotNull
    @Size(min = 3,max = 32)
    @JsonView(Views.Summary.class)
    private String name;

    @Email
    @NotNull @Size(min = 5,max = 48)
    @JsonView(Views.Summary.class)
    private String email;

    @NotNull
    @JsonView(Views.Summary.class)
    private StudentRole role = StudentRole.ROLE_ACCOUNT;

    public ExistingStudentDetails() {
    }

    /**
     * Converts a student entity to a new instance of this response transfer object
     */
    public static ExistingStudentDetails convert(Student student) {
        if (student == null)
            throw new NullPointerException("student argument was null");

        ExistingStudentDetails studentDetails = new ExistingStudentDetails();
        studentDetails.setEmail(student.getEmail());
        studentDetails.setRole(student.getRole());

        if (student.getStudentDetails() != null)
            studentDetails.setName(student.getStudentDetails().getName());

        return studentDetails;
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

    public void setRole(StudentRole role) { this.role = role; }
}
