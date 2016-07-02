package com.pliesveld.flashnote.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.pliesveld.flashnote.domain.base.DomainBaseEntity;
import com.pliesveld.flashnote.domain.converter.AccountRoleConverter;
import com.pliesveld.flashnote.domain.converter.InstantConverter;
import com.pliesveld.flashnote.model.json.Views;
import com.pliesveld.flashnote.schema.Constants;
import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.Objects;

import static com.pliesveld.flashnote.schema.Constants.*;

@Entity
@Table(name = "STUDENT_ACCOUNT",
    uniqueConstraints = {
        @UniqueConstraint(name = "UNIQUE_ACCOUNT_EMAIL", columnNames = "STUDENT_EMAIL"),
        @UniqueConstraint(name = "UNIQUE_ACCOUNT_NAME", columnNames = "STUDENT_NAME")
        })
public class Student extends DomainBaseEntity<Integer> {

    private Integer id;
    private String name;
    private String email;
    private String password;
    private AccountRole role;
    private boolean temporaryPassword;
    private Instant lastPasswordResetDate;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "STUDENT_ID")
    @JsonView(Views.Summary.class)
    public Integer getId() { return id; }

    @NotNull
    @Size(min = Constants.MIN_ACCOUNT_NAME_LENGTH, max = Constants.MAX_ACCOUNT_NAME_LENGTH)
    @Column(name = "STUDENT_NAME", length = Constants.MAX_ACCOUNT_NAME_LENGTH, nullable = false)
    @JsonView(Views.Summary.class)
    public String getName()
    {
        return name;
    }

    @NotNull
    @Size(min = MIN_ACCOUNT_EMAIL_LENGTH,max = MAX_ACCOUNT_EMAIL_LENGTH)
    @Email
    @Column(name = "STUDENT_EMAIL", length = MAX_ACCOUNT_EMAIL_LENGTH, nullable = false)
    public String getEmail() {
        return email;
    }

    @NotNull
    @Size(min = MIN_ACCOUNT_PASSWORD_LENGTH, max = MAX_ACCOUNT_PASSWORD_LENGTH)
    @Column(name = "STUDENT_PASSWORD", length = MAX_ACCOUNT_PASSWORD_LENGTH, nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public String getPassword() {
        return password;
    }

    @NotNull
    @Column(name = "STUDENT_ROLE", nullable = false)
    @Convert(converter = AccountRoleConverter.class)
    @Basic(fetch = FetchType.EAGER)
    @JsonView(Views.Summary.class)
    public AccountRole getRole() {
        return role;
    }

    @NotNull
    @Column(name = "MUST_CHANGE_PASSWORD", nullable = false)
    public boolean isTemporaryPassword() {
        return temporaryPassword;
    }

    @NotNull
    @Column(name = "LAST_PASSWORD_RESET", nullable = false)
    @Convert(converter = InstantConverter.class)
    public Instant getLastPasswordResetDate() {
        return lastPasswordResetDate;
    }

    public Student() {
        super();
    }

    @PrePersist
    public void prePersist() {
        if (role == null)
            role = AccountRole.ROLE_ACCOUNT;
        temporaryPassword = false;
        lastPasswordResetDate = Instant.now();
    }

    public void setId(Integer id) { this.id = id; }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(AccountRole role) {
        this.role = role;
    }

    public void setTemporaryPassword(boolean temporaryPassword) {
        this.temporaryPassword = temporaryPassword;
    }

    public void setLastPasswordResetDate(Instant lastPasswordResetDate) {
        this.lastPasswordResetDate = lastPasswordResetDate;
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof Student)) {
            return false;
        }
        final Student other = (Student) obj;
        return Objects.equals(getEmail(), other.getEmail());
    }
}
