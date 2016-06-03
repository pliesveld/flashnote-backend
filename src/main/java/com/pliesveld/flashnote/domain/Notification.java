package com.pliesveld.flashnote.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.pliesveld.flashnote.domain.base.DomainBaseEntity;
import com.pliesveld.flashnote.domain.converter.InstantConverter;
import com.pliesveld.flashnote.domain.converter.NotificationTypeConverter;
import com.pliesveld.flashnote.model.json.Views;
import com.pliesveld.flashnote.schema.Constants;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;

@Entity
@Table(name = "NOTIFICATION")
@org.hibernate.annotations.Immutable
public class Notification extends DomainBaseEntity<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "NOTIFICATION_ID")
    @JsonView(Views.Summary.class)
    private Integer id;

    @NotNull
    @ManyToOne(targetEntity = Student.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "STUDENT_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_NOTIFICATION_STUDENT"))
    @JsonView(Views.Summary.class)
    private Student recipient;

    @NotNull
    @Size(min = Constants.MIN_NOTIFICATION_MESSAGE_LENGTH, max = Constants.MAX_NOTIFICATION_MESSAGE_LENGTH)
    @Column(name = "MESSAGE", length = Constants.MAX_NOTIFICATION_MESSAGE_LENGTH, nullable = false)
    @JsonView(Views.Summary.class)
    private String message;

    @Column(name = "DATE_CREATED", nullable = false)
    @Convert(converter = InstantConverter.class)
    @JsonProperty(value = "created", access = JsonProperty.Access.READ_ONLY)
    @JsonView(Views.Summary.class)
    private Instant createdOn;

    @NotNull
    @Column(name = "TYPE", nullable = false)
    @Convert(converter = NotificationTypeConverter.class)
    @JsonView(Views.Summary.class)
    private NotificationType type;

    protected Notification() {
        super();
    }

    public Notification(NotificationType type, Student recipient, String message) {
        this();
        this.recipient = recipient;
        this.message = message;
        this.type = type;
    }

    public Notification(Student recipient, String message) {
        this();
        this.recipient = recipient;
        this.message = message;
        this.type = NotificationType.SYSTEM_ERROR;
    }

    public Integer getId() {
        return id;
    }

    public Student getRecipient() {
        return recipient;
    }

    public String getMessage() {
        return message;
    }

    public Instant getCreatedOn() {
        return createdOn;
    }

    public NotificationType getType() {
        return type;
    }

    @PrePersist
    protected void onCreate()
    {
        createdOn = Instant.now();
    }

}
