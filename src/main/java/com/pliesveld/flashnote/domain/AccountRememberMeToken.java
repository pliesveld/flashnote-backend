package com.pliesveld.flashnote.domain;

import com.pliesveld.flashnote.domain.converter.InstantConverter;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "ACCOUNT_REMEMBERME_TOKEN")
public class AccountRememberMeToken {

    @Id
    @Column(name = "SERIES", length = 64)
    String id;

    @NotNull
    @JoinColumn(name = "USERNAME", nullable = false, insertable = false,
            foreignKey = @ForeignKey(name = "FK_USERNAME_PERSISTENT"), table = "STUDENT_DETAILS")
    String username;

    @NotNull
    @Column(name = "TOKEN", nullable = false)
    String token;

    @Column(name = "LAST_USED", nullable = false)
    @Convert(converter = InstantConverter.class)
    protected Instant lastUsed;


    @PrePersist
    public void setDefaults() {
        if (lastUsed == null) {
            lastUsed = Instant.now();
        }
    }

    public AccountRememberMeToken() {
    }

    public AccountRememberMeToken(PersistentRememberMeToken token) {
        this.username = token.getUsername();
        this.id = token.getSeries();
        this.token = token.getTokenValue();
        this.lastUsed = token.getDate().toInstant();
    }

    public PersistentRememberMeToken toPersistentRememberMeToken() {
        return new PersistentRememberMeToken(this.username, this.id, this.token, Date.from(this.lastUsed));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Instant getLastUsed() {
        return lastUsed;
    }

    public void setLastUsed(Instant lastUsed) {
        this.lastUsed = lastUsed;
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, token);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof AccountRememberMeToken)) {
            return false;
        }
        final AccountRememberMeToken other = (AccountRememberMeToken) obj;
        return Objects.equals(getUsername(), getUsername()) && Objects.equals(getToken(), other.getToken());
    }

    @Override
    public String toString() {
        return "AccountRememberMeToken{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", token='" + token + '\'' +
                ", lastUsed=" + lastUsed +
                '}';
    }
}
