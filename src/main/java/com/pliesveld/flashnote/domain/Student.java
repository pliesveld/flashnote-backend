package com.pliesveld.flashnote.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.pliesveld.flashnote.domain.converter.InstantConverter;
import com.pliesveld.flashnote.domain.converter.StudentRoleConverter;
import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.Objects;

import static com.pliesveld.flashnote.schema.Constants.*;

@Entity
@Table(name = "STUDENT_ACCOUNT",
        uniqueConstraints = @UniqueConstraint(name = "UNIQUE_EMAIL", columnNames = {"STUDENT_EMAIL"}))
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "STUDENT_ID")
    private Integer id;

    @PrimaryKeyJoinColumn
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @org.hibernate.annotations.OnDelete(action = org.hibernate.annotations.OnDeleteAction.CASCADE)
    private StudentDetails studentDetails;

    @NotNull
    @Size(min = MIN_STUDENT_EMAIL_LENGTH,max = MAX_STUDENT_EMAIL_LENGTH)
    @Email
    @Column(name = "STUDENT_EMAIL", length = MAX_STUDENT_EMAIL_LENGTH, nullable = false, unique = true)
    private String email;

    @NotNull
    @Size(min = MIN_STUDENT_PASSWORD_LENGTH, max = MAX_STUDENT_PASSWORD_LENGTH)
    @Column(name = "STUDENT_PASSWORD", length = MAX_STUDENT_PASSWORD_LENGTH, nullable = false)
    @JsonIgnore
    private String password;

    @NotNull
    @Column(name = "STUDENT_ROLE", nullable = false)
    @Convert(converter = StudentRoleConverter.class)
    @Basic(fetch = FetchType.EAGER)
    private StudentRole role;

    @NotNull
    @Column(name = "MUST_CHANGE_PASSWORD", nullable = false)
    private boolean temporaryPassword;

    @NotNull
    @Column(name = "LAST_PASSWORD_RESET", nullable = false)
    @Convert(converter = InstantConverter.class)
    private Instant lastPasswordResetDate;

    public Student() {}

    public Student(Student o) {
        this.id = o.id;
        this.studentDetails = o.studentDetails;
        this.email = o.email;
        this.role = o.role;
    }

    @PrePersist
    public void prePersist() {
        if(role == null)
            role = StudentRole.ROLE_ACCOUNT;
        temporaryPassword = false;
        lastPasswordResetDate = Instant.now();
    }

    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    public StudentDetails getStudentDetails() { return studentDetails; }

    public void setStudentDetails(StudentDetails studentDetails)
    {
        this.studentDetails = studentDetails;
        if(studentDetails.getStudent() == null || !studentDetails.getStudent().equals(this))
            studentDetails.setStudent(this);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public StudentRole getRole() {
        return role;
    }

    public void setRole(StudentRole role) {
        this.role = role;
    }

    public void setTemporaryPassword(boolean temporaryPassword) {
        this.temporaryPassword = temporaryPassword;
    }

    public boolean isTemporaryPassword() {
        return temporaryPassword;
    }

    public Instant getLastPasswordResetDate() {
        return lastPasswordResetDate;
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
