package com.pliesveld.flashnote.domain;

import com.pliesveld.flashnote.domain.converter.InstantConverter;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;

import javax.persistence.*;
import java.time.Instant;
import java.util.Date;

@Entity
@Table(name = "PERSISTENT_LOGINS")
public class AccountRememberMeToken {

    @Id
    @Column(name = "SERIES", length = 64)
    String id;

    @JoinColumn(name = "USERNAME", nullable = false, insertable = false,
            foreignKey = @ForeignKey(name = "FK_USERNAME_PERSISTENT"), table = "STUDENT_DETAILS")
    String username;

    @Column(name = "TOKEN", nullable = false)
    String token;

    @Column(name = "LAST_USED", nullable = false)
    @Convert(converter = InstantConverter.class)
    protected Instant lastUsed;


    @PrePersist
    public void setDefaults() {
        if(lastUsed == null)
        {
            lastUsed = Instant.now();
        }
    }

    public AccountRememberMeToken()
    {
    }

    public AccountRememberMeToken(PersistentRememberMeToken token)
    {
        this.username = token.getUsername();
        this.id = token.getSeries();
        this.token = token.getTokenValue();
        this.lastUsed = token.getDate().toInstant();
    }

    public PersistentRememberMeToken toPersistentRememberMeToken()
    {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AccountRememberMeToken that = (AccountRememberMeToken) o;

        if (lastUsed != null ? !lastUsed.equals(that.lastUsed) : that.lastUsed != null) return false;
        if (token != null ? !token.equals(that.token) : that.token != null) return false;
        if (username != null ? !username.equals(that.username) : that.username != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = username != null ? username.hashCode() : 0;
        result = 31 * result + (token != null ? token.hashCode() : 0);
        result = 31 * result + (lastUsed != null ? lastUsed.hashCode() : 0);
        return result;
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
