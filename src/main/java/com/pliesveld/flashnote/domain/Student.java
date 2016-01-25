package com.pliesveld.flashnote.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "STUDENT")
@NamedQueries(value = {
        @NamedQuery(name = "Student.findByStudentId",
                query = "SELECT OBJECT(s) FROM Student s WHERE s.id = :id"),
        @NamedQuery(name = "Student.findByStudentEmail",
                query = "SELECT OBJECT(s) FROM Student s WHERE s.email = :email"),
        @NamedQuery(name = "Student.count",
                query = "SELECT COUNT(s) FROM Student s")
})
@SecondaryTable(name="STUDENT_ACCOUNT",
        pkJoinColumns = @PrimaryKeyJoinColumn(name="STUDENT_ID"),
        foreignKey = @ForeignKey(name="FK_STUDENT_SECONDARY"),
        uniqueConstraints = @UniqueConstraint(name = "UNIQUE_EMAIL", columnNames = {"STUDENT_EMAIL"})
)
public class Student implements Serializable
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "STUDENT_ID")
    private Integer id;

    @OneToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    @JsonIgnore
    @JoinTable(name="STUDENT_DECK"
        ,joinColumns =  @JoinColumn(name="STUDENT_ID",foreignKey = @ForeignKey(name = "FK_STUDENT"))
        ,foreignKey =                                              @ForeignKey(name="FK_STUDENT")
        ,inverseJoinColumns =   @JoinColumn(name="DECK_ID",foreignKey = @ForeignKey(name = "FK_STUDENT_DECK"))
        ,inverseForeignKey =                                            @ForeignKey(name="FK_STUDENT_DECK")
        ,uniqueConstraints = {@UniqueConstraint(columnNames = {"DECK_ID"},  name="UNIQUE_DECK")}
    )
    private Set<Deck> decks = new HashSet<>();

    @Size(min = 3,max = 32,message = "Name must be between 3 and 32 letters") @NotNull
    @Column(name = "STUDENT_NAME",  length = 32, nullable = false)
    private String name;

    @Email(message = "Invalid email address") @NotNull @Size(min = 5,max = 48,message = "Name must be between 5 and 48 letters")
    @Column(name = "STUDENT_EMAIL", length = 48, nullable = false, unique = true,table = "STUDENT_ACCOUNT")
    private String email;

    @NotNull
    @Size(min = 1, max = 60)
//    @Length(min=60,max = 60)
    @Column(name = "STUDENT_PASSWORD",length = 60,nullable = true,table = "STUDENT_ACCOUNT")
    private String password;

    @Column(name="STUDENT_ROLE",length=16,table = "STUDENT_ACCOUNT")
    @Convert(converter=StudentTypeConverter.class)
    private StudentType role;

    @PrePersist
    public void prePersist() {
        if(role == null)
            role = StudentType.USER;
    }


    public Student() {
    }

    public Student(String student) {
        this.name = student;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
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

    public Set<Deck> getDecks() {
        return decks;
    }

    public void setDecks(Set<Deck> decks) {
        this.decks = decks;
    }

    public StudentType getRole() {
        return role;
    }

    public void setRole(StudentType role) {
        this.role = role;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Student other = (Student) obj;
        if (id == null) {
            if (other.id != null) return false;
        } else if (!id.equals(other.id)) return false;
        if (name == null) {
            if (other.name != null) return false;
        } else if (!name.equals(other.name)) return false;
        return true;
    }
}
