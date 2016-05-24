package com.pliesveld.flashnote.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.pliesveld.flashnote.domain.base.DomainBaseEntity;
import com.pliesveld.flashnote.model.json.Views;
import com.pliesveld.flashnote.schema.Constants;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "STUDENT_DETAILS",
        uniqueConstraints = @UniqueConstraint(name = "UNIQUE_STUDENT_NAME", columnNames = "STUDENT_NAME"))
public class StudentDetails extends DomainBaseEntity<Integer> implements Serializable
{
    private Integer id;
    private Student student;
    private String name;

    @Id
    @Column(name = "STUDENT_DETAILS_ID")
    @JsonView(Views.Summary.class)
    public Integer getId()
    {
        return id;
    }

    @NotNull
    @MapsId
    @OneToOne(cascade = { CascadeType.PERSIST, CascadeType.REMOVE }, fetch = FetchType.LAZY, optional = false, mappedBy = "studentDetails")
    @JoinColumn(name = "STUDENT_DETAILS_ID", foreignKey = @ForeignKey(name = "FK_STUDENT_DETAILS"))
    @JsonIgnore
    public Student getStudent() { return student; }

    @NotNull
    @Size(min = Constants.MIN_STUDENT_NAME_LENGTH, max = Constants.MAX_STUDENT_NAME_LENGTH)
    @Column(name = "STUDENT_NAME", length = Constants.MAX_STUDENT_NAME_LENGTH, nullable = false)
    @JsonView(Views.Summary.class)
    public String getName()
    {
        return name;
    }


    protected StudentDetails() {
        super();
    }

    public StudentDetails(@NotNull Student student, String name) {
        this();
        setStudent(student);
        this.name = name;
    }

    @Deprecated
    public StudentDetails(String name) {
        this();
        this.name = name;
    }

    public void setStudent(@NotNull Student student) {

        this.student = student;
        this.id = student.getId();
        if(student.getStudentDetails() != this)
            student.setStudentDetails(this);

    }

    protected void setName(String name)
    {
        this.name = name;
    }

    protected void setId(Integer id)
    {
        this.id = id;
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
