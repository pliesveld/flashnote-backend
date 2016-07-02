package com.pliesveld.flashnote.model.json.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.pliesveld.flashnote.domain.AccountRole;
import com.pliesveld.flashnote.domain.Student;
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
public class StudentResponseJson extends ModelBase implements JsonWebRequestSerializable {
    @NotNull
    @Size(min = 3, max = 32)
    @JsonView(Views.Summary.class)
    private String name;

    @Email
    @NotNull
    @Size(min = 5, max = 48)
    @JsonView(Views.Summary.class)
    private String email;

    @NotNull
    @JsonView(Views.Summary.class)
    private AccountRole role = AccountRole.ROLE_ACCOUNT;

    public StudentResponseJson() {
    }

    /**
     * Converts a student entity to a new instance of this response transfer object
     */
    public static StudentResponseJson convert(final Student student) {
        if (student == null)
            throw new NullPointerException("student argument was null");

        final StudentResponseJson responseJson = new StudentResponseJson();
        responseJson.setEmail(student.getEmail());
        responseJson.setRole(student.getRole());
        responseJson.setName(student.getName());
        return responseJson;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(AccountRole role) {
        this.role = role;
    }
}
