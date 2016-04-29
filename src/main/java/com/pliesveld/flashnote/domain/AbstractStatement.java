package com.pliesveld.flashnote.domain;

import com.pliesveld.flashnote.domain.base.AbstractAuditableEntity;
import com.pliesveld.flashnote.schema.Constants;
import org.hibernate.annotations.CollectionId;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "STATEMENT")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class AbstractStatement extends AbstractAuditableEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "STATEMENT_ID")
    protected Integer id;

    @NotNull
    @Size(max = Constants.MAX_STATEMENT_CONTENT_LENGTH)
    @Column(name = "CONTENT", length = Constants.MAX_STATEMENT_CONTENT_LENGTH, nullable = false)
    protected String content = "";

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "ANNOTATION", joinColumns = { @JoinColumn(name = "STATEMENT_ID",foreignKey = @ForeignKey(name = "FK_ANNOTATION_STATEMENT"))})
//    @GenericGenerator(name="sequence-gen", strategy="sequence")
    @CollectionId(
        columns   = @Column(name = "ANNOTATION_ID"),
        type      = @Type(type = "integer"),
        generator = Constants.SEQUENCE_GENERATOR
//        generator = "sequence-gen"
    )
    @LazyCollection(LazyCollectionOption.EXTRA)
    protected Collection<AnnotatedStatement> annotations = new ArrayList<AnnotatedStatement>();

    public AbstractStatement() {}

    public String getContent() {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public Integer getId() { return id; }

    protected void setId(Integer id)
    {
        this.id = id;
    }

    public Collection<AnnotatedStatement> getAnnotations() { return annotations; }

    public void addAnnotation(AnnotatedStatement annotatedStatement)
    {
        this.annotations.add(annotatedStatement);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof AbstractStatement)) {
            return false;
        }
        final AbstractStatement other = (AbstractStatement) obj;
        return Objects.equals(getContent(), other.getContent());
    }


}
