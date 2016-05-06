package com.pliesveld.flashnote.model.json.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pliesveld.flashnote.model.json.base.JsonWebRequestSerializable;
import com.pliesveld.flashnote.model.json.base.ModelBase;
import org.hibernate.validator.constraints.Email;
import org.springframework.stereotype.Component;

@Component
@JsonInclude(JsonInclude.Include.ALWAYS)
public class PasswordResetRequestJson extends ModelBase implements JsonWebRequestSerializable {

    @Email
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
