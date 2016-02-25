package com.pliesveld.flashnote.domain;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "STUDENT_DETAILS")
public class StudentDetails implements Serializable
{
    @Id
    @Column(name = "STUDENT_ID")
    private Integer id;

    @MapsId
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "STUDENT_ID")
    private Student student;

    @OneToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REFRESH})
    @JoinTable(name="STUDENT_DECK"
        ,joinColumns =  @JoinColumn(name="STUDENT_ID",foreignKey = @ForeignKey(name = "FK_STUDENT"))
        ,foreignKey =                                              @ForeignKey(name="FK_STUDENT")
        ,inverseJoinColumns =   @JoinColumn(name="DECK_ID",foreignKey = @ForeignKey(name = "FK_STUDENT_DECK"))
        ,inverseForeignKey =                                            @ForeignKey(name="FK_STUDENT_DECK")
        ,uniqueConstraints = {@UniqueConstraint(columnNames = {"DECK_ID"},  name="UNIQUE_DECK")}
    )
    private Set<Deck> decks = new HashSet<>();

    @Size(min = 3,max = 32)
    @Column(name = "STUDENT_NAME",  length = 32, nullable = false)
    private String name;

    public StudentDetails() {
    }

    public StudentDetails(String name) {
        this.name = name;
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

    public Set<Deck> getDecks() {
        return decks;
    }

    public void setDecks(Set<Deck> decks) {
        this.decks = decks;
    }

    public Student getStudent() { return student; }

    public void setStudent(Student student) { this.student = student; }

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
        StudentDetails other = (StudentDetails) obj;
        if (id == null) {
            if (other.id != null) return false;
        } else if (!id.equals(other.id)) return false;
        if (name == null) {
            if (other.name != null) return false;
        } else if (!name.equals(other.name)) return false;
        return true;
    }
}
