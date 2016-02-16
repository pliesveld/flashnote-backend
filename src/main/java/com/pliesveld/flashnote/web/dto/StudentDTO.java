package com.pliesveld.flashnote.web.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

import com.pliesveld.flashnote.domain.StudentType;

/**
 * Subset of student model for account creation
 */

public class StudentDTO implements Serializable
{
    @Size(min = 3,max = 32,message = "Name must be between 3 and 32 letters") @NotNull
    private String name;
    
    @Email(message = "Invalid email address") @NotNull @Size(min = 5,max = 48,message = "Name must be between 5 and 48 letters")
    private String email;
    
    @Size(min = 1, max = 60) @NotNull
    private String password;

    public StudentDTO() {
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

    public StudentType getRole() {
        return StudentType.USER;
    }

}
