package com.pliesveld.flashnote.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.pliesveld.flashnote.domain.base.DomainBaseEntity;
import com.pliesveld.flashnote.domain.converter.InstantConverter;
import com.pliesveld.flashnote.domain.converter.StudentRoleConverter;
import com.pliesveld.flashnote.model.json.Views;
import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.Objects;

import static com.pliesveld.flashnote.schema.Constants.*;

@Entity
@Table(name = "STUDENT_ACCOUNT",
        uniqueConstraints = @UniqueConstraint(name = "UNIQUE_STUDENT_EMAIL", columnNames = "STUDENT_EMAIL"))
public class Student extends DomainBaseEntity<Integer> {

    private Integer id;
    private StudentDetails studentDetails;
    private String email;
    private String password;
    private StudentRole role;
    private boolean temporaryPassword;
    private Instant lastPasswordResetDate;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "STUDENT_ID")
    @JsonView(Views.Summary.class)
    public Integer getId() { return id; }

    @PrimaryKeyJoinColumn
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    public StudentDetails getStudentDetails() { return studentDetails; }

    @NotNull
    @Size(min = MIN_STUDENT_EMAIL_LENGTH,max = MAX_STUDENT_EMAIL_LENGTH)
    @Email
    @Column(name = "STUDENT_EMAIL", length = MAX_STUDENT_EMAIL_LENGTH, nullable = false)
    public String getEmail() {
        return email;
    }

    @NotNull
    @Size(min = MIN_STUDENT_PASSWORD_LENGTH, max = MAX_STUDENT_PASSWORD_LENGTH)
    @Column(name = "STUDENT_PASSWORD", length = MAX_STUDENT_PASSWORD_LENGTH, nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public String getPassword() {
        return password;
    }

    @NotNull
    @Column(name = "STUDENT_ROLE", nullable = false)
    @Convert(converter = StudentRoleConverter.class)
    @Basic(fetch = FetchType.EAGER)
    @JsonView(Views.Summary.class)
    public StudentRole getRole() {
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
        if(role == null)
            role = StudentRole.ROLE_ACCOUNT;
        temporaryPassword = false;
        lastPasswordResetDate = Instant.now();
    }

    public void setId(Integer id) { this.id = id; }

    public void setStudentDetails(StudentDetails studentDetails)
    {
        this.studentDetails = studentDetails;
        if(studentDetails != null && (studentDetails.getStudent() == null || !studentDetails.getStudent().equals(this)))
            studentDetails.setStudent(this);
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(StudentRole role) {
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
