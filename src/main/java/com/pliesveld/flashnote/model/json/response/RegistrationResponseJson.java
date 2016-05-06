package com.pliesveld.flashnote.model.json.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.pliesveld.flashnote.model.json.Views;
import com.pliesveld.flashnote.model.json.base.JsonWebResponseSerializable;
import com.pliesveld.flashnote.model.json.base.ModelBase;
import org.springframework.stereotype.Component;

@Component
@JsonInclude(JsonInclude.Include.ALWAYS)
public class RegistrationResponseJson extends ModelBase implements JsonWebResponseSerializable {

    @JsonView(Views.Summary.class)
    private String status_code;

    @JsonView(Views.Summary.class)
    private String message;

    static final public String EMAIL_TAKEN = "email_taken";
    static final public String NAME_TAKEN = "name_taken";
    static final public String EMAIL_SENT = "email_sent";

    public RegistrationResponseJson() {
    }

    public RegistrationResponseJson(String status_code, String message) {
        this.status_code = status_code;
        this.message = message;
    }

    public String getStatus_code() {
        return status_code;
    }

    public void setStatus_code(String status_code) {
        this.status_code = status_code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
