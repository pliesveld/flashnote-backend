package com.pliesveld.flashnote.security;

import com.fasterxml.jackson.annotation.JsonProperty;



public class AuthToken
{
    @JsonProperty
    private final String password;

    @JsonProperty
    private final String username;

    public AuthToken(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }
}
