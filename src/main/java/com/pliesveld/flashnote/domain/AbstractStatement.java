package com.pliesveld.flashnote.domain;

import com.fasterxml.jackson.annotation.JsonView;
import com.pliesveld.flashnote.domain.base.AbstractAuditableEntity;
import com.pliesveld.flashnote.model.json.Views;
import com.pliesveld.flashnote.schema.Constants;
import org.hibernate.annotations.CollectionId;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Type;
import org.springframework.util.DigestUtils;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "STATEMENT")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorValue(value = "STATEMENT")
public abstract class AbstractStatement extends AbstractAuditableEntity<Integer> {
    protected Integer id;
    protected String content;
    protected String contentHash;
    protected Collection<AnnotatedStatement> annotations = new ArrayList<AnnotatedStatement>();

    public AbstractStatement() {
        super();
    }

    @Id
    @SequenceGenerator(name = "statement_gen", sequenceName = "statement_id_seq", initialValue = 9000)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "statement_gen")
    @Column(name = "STATEMENT_ID")
    @JsonView(Views.Summary.class)
    public Integer getId() {
        return id;
    }

    @NotNull
    @Size(min = Constants.MIN_STATEMENT_CONTENT_LENGTH, max = Constants.MAX_STATEMENT_CONTENT_LENGTH)
    @Column(name = "CONTENT", length = Constants.MAX_STATEMENT_CONTENT_LENGTH, nullable = false)
    @JsonView(Views.Summary.class)
    public String getContent() {
        return content;
    }

    @NotNull
    @Size(min = Constants.MD5_HASH_LENGTH, max = Constants.MD5_HASH_LENGTH)
    @Column(name = "CONTENT_HASH", length = Constants.MD5_HASH_LENGTH, nullable = false)
    public String getContentHash() {
        return contentHash;
    }

    @Size(min = 0, max = Constants.MAX_STATEMENT_ANNOTATIONS_SIZE)
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "ANNOTATION", joinColumns = {@JoinColumn(name = "STATEMENT_ID", foreignKey = @ForeignKey(name = "FK_ANNOTATION_STATEMENT"))})
    @SequenceGenerator(name = "annotation_gen", sequenceName = "annotation_id_seq", initialValue = 15000)
//    @GenericGenerator(name="annotation_gen", strategy="annontation_id_seq")
    @CollectionId(
            columns = @Column(name = "ANNOTATION_ID"),
            type = @Type(type = "integer"),
            generator = "annotation_gen"
    )
    @LazyCollection(LazyCollectionOption.EXTRA)
    @JsonView(Views.SummaryWithCollections.class)
    public Collection<AnnotatedStatement> getAnnotations() {
        return annotations;
    }

    protected void setId(final Integer id) {
        this.id = id;
    }

    public void setContent(final String content) {
        this.content = content;
        this.updateContent();
    }

    private void setContentHash(final String contentHash) {
        this.contentHash = contentHash;
    }

    protected void setAnnotations(Collection<AnnotatedStatement> annotations) {
        this.annotations = annotations;
    }

    public void addAnnotation(AnnotatedStatement annotatedStatement) {
        this.annotations.add(annotatedStatement);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getContentHash());
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
        return Objects.equals(getContentHash(), other.getContentHash());
    }

    protected void updateContent() {
        setContentHash(DigestUtils.md5DigestAsHex(getContent().getBytes()));
    }
}
