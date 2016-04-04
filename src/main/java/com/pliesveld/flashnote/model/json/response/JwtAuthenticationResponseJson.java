package com.pliesveld.flashnote.model.json.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pliesveld.flashnote.model.json.base.JsonWebResponseSerializable;
import com.pliesveld.flashnote.model.json.base.ModelBase;

@JsonInclude(JsonInclude.Include.ALWAYS)
public class JwtAuthenticationResponseJson extends ModelBase implements JsonWebResponseSerializable {

    private static final long serialVersionUID = 1250166508152483573L;

    private final String token;

    public JwtAuthenticationResponseJson(String token) {
        this.token = token;
    }

    public String getToken() {
        return this.token;
    }
}
