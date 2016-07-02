package com.pliesveld.flashnote.model.json.request;

import com.pliesveld.flashnote.domain.AccountRole;
import com.pliesveld.flashnote.domain.Student;
import com.pliesveld.flashnote.model.json.base.JsonWebRequestSerializable;
import com.pliesveld.flashnote.model.json.base.ModelBase;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static com.pliesveld.flashnote.schema.Constants.*;

/**
 * Subset of student model for account creation
 */

public class NewStudentRequestJson extends ModelBase implements JsonWebRequestSerializable {
    @NotNull
    @Size(min = MIN_ACCOUNT_NAME_LENGTH, max = MAX_ACCOUNT_NAME_LENGTH)
    private String name;

    @NotNull
    @Email
    @Size(min = MIN_ACCOUNT_EMAIL_LENGTH, max = MAX_ACCOUNT_EMAIL_LENGTH)
    private String email;

    @NotNull
    @Size(min = MIN_ACCOUNT_PASSWORD_LENGTH, max = MAX_ACCOUNT_PASSWORD_LENGTH)
    private String password;

    @NotNull
    private AccountRole role;

    public NewStudentRequestJson() {
    }

    public static NewStudentRequestJson convert(Student student) {
        if (student == null)
            throw new NullPointerException("student argument was null");

        NewStudentRequestJson newStudent = new NewStudentRequestJson();
        newStudent.setEmail(student.getEmail());
        newStudent.setPassword(student.getPassword());
        newStudent.setRole(student.getRole());
        newStudent.setName(student.getName());
        return newStudent;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public AccountRole getRole() {
        return this.role;
    }

    public void setRole(AccountRole role) {
        this.role = role;
    }
}
