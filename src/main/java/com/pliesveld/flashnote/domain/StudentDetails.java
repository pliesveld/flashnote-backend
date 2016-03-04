package com.pliesveld.flashnote.domain;

import com.pliesveld.flashnote.schema.Constants;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "STUDENT_DETAILS",
        uniqueConstraints = @UniqueConstraint(name = "UNIQUE_STUDENT_NAME", columnNames = "STUDENT_NAME"))
public class StudentDetails implements Serializable
{
    @Id
    @Column(name = "STUDENT_ID")
    private Integer id;

    @MapsId
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "STUDENT_ID", foreignKey = @ForeignKey(name = "FK_STUDENT"))
    @org.hibernate.annotations.OnDelete(action = org.hibernate.annotations.OnDeleteAction.CASCADE)
    private Student student;

    @NotNull
    @Size(min = Constants.MIN_STUDENT_NAME_LENGTH,max = Constants.MAX_STUDENT_NAME_LENGTH)
    @Column(name = "STUDENT_NAME",  length = Constants.MAX_STUDENT_NAME_LENGTH, nullable = false)
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
