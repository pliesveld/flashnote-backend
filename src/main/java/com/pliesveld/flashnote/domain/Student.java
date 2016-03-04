package com.pliesveld.flashnote.domain;

import com.pliesveld.flashnote.domain.converter.StudentRoleConverter;
import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "STUDENT_ACCOUNT",
        uniqueConstraints = @UniqueConstraint(name = "UNIQUE_EMAIL", columnNames = {"STUDENT_EMAIL"}))
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "STUDENT_ID")
    private Integer id;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @PrimaryKeyJoinColumn
    @org.hibernate.annotations.OnDelete(action = org.hibernate.annotations.OnDeleteAction.CASCADE)
    private StudentDetails studentDetails;

    @NotNull
    @Size(min = 5,max = 48)
    @Email
    @Column(name = "STUDENT_EMAIL", length = 48, nullable = false, unique = true)
    private String email;

    @NotNull
    @Size(min = 1, max = 60)
    @Column(name = "STUDENT_PASSWORD", length = 60, nullable = true)
    private String password;

    @NotNull
    @Column(name="STUDENT_ROLE",length=16)
    @Convert(converter=StudentRoleConverter.class)
    @Basic(fetch = FetchType.EAGER)
    private StudentRole role;

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
            role = StudentRole.ROLE_USER;
    }

    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    public StudentDetails getStudentDetails() { return studentDetails; }

    public void setStudentDetails(StudentDetails studentDetails) { this.studentDetails = studentDetails; }

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


}
