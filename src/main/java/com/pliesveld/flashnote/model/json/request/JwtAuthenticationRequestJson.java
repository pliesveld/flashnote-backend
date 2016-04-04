package com.pliesveld.flashnote.model.json.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pliesveld.flashnote.model.json.base.JsonWebRequestSerializable;
import com.pliesveld.flashnote.model.json.base.ModelBase;
import org.springframework.stereotype.Component;


@Component
@JsonInclude(JsonInclude.Include.ALWAYS)
public class JwtAuthenticationRequestJson extends ModelBase implements JsonWebRequestSerializable {

    private static final long serialVersionUID = -8445943548965154778L;

    private String username;
    private String password;

    public JwtAuthenticationRequestJson() {
        super();
    }

    public JwtAuthenticationRequestJson(String username, String password) {
        this.setUsername(username);
        this.setPassword(password);
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}