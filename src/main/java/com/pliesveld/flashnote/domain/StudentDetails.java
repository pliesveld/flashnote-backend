package com.pliesveld.flashnote.domain;

import com.pliesveld.flashnote.domain.base.DomainBaseEntity;
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
public class StudentDetails extends DomainBaseEntity<Integer> implements Serializable
{
    @Id
    private Integer id;

    @MapsId
    @OneToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JoinColumn(name = "STUDENT_ID", foreignKey = @ForeignKey(name = "FK_STUDENT_DETAILS"))
    private Student student;

    @NotNull
    @Size(min = Constants.MIN_STUDENT_NAME_LENGTH, max = Constants.MAX_STUDENT_NAME_LENGTH)
    @Column(name = "STUDENT_NAME", length = Constants.MAX_STUDENT_NAME_LENGTH, nullable = false)
    private String name;

    protected StudentDetails() {
        super();
    }

    public StudentDetails(String name) {
        this();
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
        this.id = student.getId();
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
