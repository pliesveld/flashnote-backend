package com.pliesveld.flashnote.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pliesveld.flashnote.schema.Constants;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "STUDENT_DETAILS",
        uniqueConstraints = @UniqueConstraint(name = "UNIQUE_STUDENT_NAME", columnNames = "STUDENT_NAME"))
/*@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")*/
public class StudentDetails implements Serializable
{
    @Id
    @Column(name = "STUDENT_ID")
    private Integer id;

    @MapsId
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "STUDENT_ID", foreignKey = @ForeignKey(name = "FK_STUDENT_DETAILS"))
//    @org.hibernate.annotations.OnDelete(action = org.hibernate.annotations.OnDeleteAction.CASCADE)
    @JsonIgnore
    private Student student;

    @NotNull
    @Size(min = Constants.MIN_STUDENT_NAME_LENGTH, max = Constants.MAX_STUDENT_NAME_LENGTH)
    @Column(name = "STUDENT_NAME", length = Constants.MAX_STUDENT_NAME_LENGTH, nullable = false)
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
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof StudentDetails)) {
            return false;
        }
        final StudentDetails other = (StudentDetails) obj;
        return Objects.equals(getName(), other.getName());
    }
}
