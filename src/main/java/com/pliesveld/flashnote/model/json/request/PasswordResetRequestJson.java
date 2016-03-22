package com.pliesveld.flashnote.model.json.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.validator.constraints.Email;
import org.springframework.stereotype.Component;

@Component
@JsonInclude(JsonInclude.Include.ALWAYS)
public class PasswordResetRequestJson {

    @Email
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
