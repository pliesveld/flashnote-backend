package com.pliesveld.flashnote.web.dto;

import com.pliesveld.flashnote.domain.StudentType;

/**
 * Subset of student model for account creation
 */

public class StudentDTO
{
    private String name;
    private String email;
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
