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
//    @org.hibernate.annotations.OnDelete(action = org.hibernate.annotations.OnDeleteAction.CASCADE)
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

    public void setStudent(Student student) {

        this.student = student;
        if(student.getStudentDetails() == null || !student.getStudentDetails().equals(this))
            student.setStudentDetails(this);

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StudentDetails that = (StudentDetails) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
