package com.pliesveld.flashnote.model.json.request;

import com.pliesveld.flashnote.model.json.base.JsonWebRequestSerializable;
import com.pliesveld.flashnote.model.json.base.ModelBase;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class RegistrationRequestJson extends ModelBase implements JsonWebRequestSerializable {

    @Size(min = 1)
    private String name;

    @Email
    private String email;

    @NotNull
    @Size(min = 1)
    private String password;

    public RegistrationRequestJson() {
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
