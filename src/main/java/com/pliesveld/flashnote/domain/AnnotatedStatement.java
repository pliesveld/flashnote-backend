package com.pliesveld.flashnote.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pliesveld.flashnote.domain.converter.InstantConverter;
import com.pliesveld.flashnote.schema.Constants;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;

@Embeddable
public class AnnotatedStatement {

    @NotNull
    @Column(name = "DATE_CREATED", nullable = false)
    @Convert(converter = InstantConverter.class)
    @JsonProperty("created")
    private Instant createdOn;

    @NotNull
    @ManyToOne(targetEntity = StudentDetails.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "STUDENT_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_ANNOTATION_STUDENT"))
    @JsonProperty("created_by")
    private StudentDetails createdBy;

    @NotNull
    @Size(max = Constants.MAX_NOTIFICATION_MESSAGE_LENGTH)
    @Column(name = "MESSAGE", length = Constants.MAX_NOTIFICATION_MESSAGE_LENGTH, nullable = false)
    private String message;

    protected AnnotatedStatement()
    {
    }

    public AnnotatedStatement(StudentDetails createdBy, String message) {
        this.createdOn = Instant.now();
        this.createdBy = createdBy;
        this.message = message;
    }

    public Instant getCreatedOn() { return createdOn; }

    public StudentDetails getCreatedBy() { return createdBy; }

    public String getMessage() { return message; }
}
