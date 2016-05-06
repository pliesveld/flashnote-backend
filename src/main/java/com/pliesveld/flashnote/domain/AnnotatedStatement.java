package com.pliesveld.flashnote.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.pliesveld.flashnote.domain.converter.InstantConverter;
import com.pliesveld.flashnote.model.json.Views;
import com.pliesveld.flashnote.schema.Constants;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;

@Embeddable
public class AnnotatedStatement {

    private Instant createdOn;
    private StudentDetails createdBy;
    private String message;

    protected AnnotatedStatement()
    {
        super();
    }

    public AnnotatedStatement(StudentDetails createdBy, String message) {
        this();
        this.createdOn = Instant.now();
        this.createdBy = createdBy;
        this.message = message;
    }

    @NotNull
    @Column(name = "DATE_CREATED", nullable = false)
    @Convert(converter = InstantConverter.class)
    @JsonProperty("created")
    @JsonView(Views.SummaryWithCollections.class)
    public Instant getCreatedOn() { return createdOn; }

    @NotNull
    @ManyToOne(targetEntity = StudentDetails.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "STUDENT_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_ANNOTATION_STUDENT"))
    @JsonProperty("created_by")
    @JsonView(Views.Summary.class)
    public StudentDetails getCreatedBy() { return createdBy; }

    @NotNull
    @Size(min = Constants.MIN_NOTIFICATION_MESSAGE_LENGTH, max = Constants.MAX_NOTIFICATION_MESSAGE_LENGTH)
    @Column(name = "MESSAGE", length = Constants.MAX_NOTIFICATION_MESSAGE_LENGTH, nullable = false)
    @JsonView(Views.Summary.class)
    public String getMessage() { return message; }

    protected void setCreatedOn(Instant createdOn) { this.createdOn = createdOn; }

    protected void setCreatedBy(StudentDetails createdBy) {this.createdBy = createdBy; }

    protected void setMessage(String message) { this.message = message; }
}
