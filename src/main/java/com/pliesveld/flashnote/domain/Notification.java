package com.pliesveld.flashnote.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pliesveld.flashnote.domain.converter.InstantConverter;
import com.pliesveld.flashnote.domain.converter.NotificationTypeConverter;
import com.pliesveld.flashnote.schema.Constants;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;

@Entity
@Table(name = "NOTIFICATION")
@org.hibernate.annotations.Immutable
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "NOTIFICATION_ID")
    private Integer id;

    @NotNull
    @ManyToOne(targetEntity = StudentDetails.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "STUDENT_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_NOTIFICATION_STUDENT"))
    private StudentDetails recipient;

    @NotNull
    @Size(max = Constants.MAX_NOTIFICATION_MESSAGE_LENGTH)
    @Column(name = "MESSAGE", length = Constants.MAX_NOTIFICATION_MESSAGE_LENGTH, nullable = false)
    private String message;

    @Column(name = "DATE_CREATED", nullable = false)
    @Convert(converter = InstantConverter.class)
    @JsonProperty("created")
    private Instant createdOn;

    @Column(name = "TYPE", nullable = false)
    @Convert(converter = NotificationTypeConverter.class)
    private NotificationType type;

    protected Notification() {
    }

    public Notification(NotificationType type, StudentDetails recipient, String message) {
        this.recipient = recipient;
        this.message = message;
        this.type = type;
    }

    public Notification(StudentDetails recipient, String message) {
        this.recipient = recipient;
        this.message = message;
        this.type = NotificationType.SYSTEM_ERROR;
    }

    public Integer getId() {
        return id;
    }

    public StudentDetails getRecipient() {
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
