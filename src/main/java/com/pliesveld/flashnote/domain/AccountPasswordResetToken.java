package com.pliesveld.flashnote.domain;

import com.pliesveld.flashnote.domain.converter.InstantConverter;
import com.pliesveld.flashnote.schema.Constants;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.time.Instant;

@Entity
@Table(name = "ACCOUNT_RESET_TOKEN",
    indexes = { @Index(name = "IDX_RESET_TOKEN", columnList = "TOKEN") }
)
public class AccountPasswordResetToken {

	@Id
	private Integer id;

    @NotNull
    @MapsId
    @OneToOne(cascade = { CascadeType.REFRESH, CascadeType.MERGE }, fetch = FetchType.EAGER)
    @JoinColumn(name = "STUDENT_ID", foreignKey = @ForeignKey(name = "FK_STUDENT_ACCOUNT_RESET"), nullable = false)
    private Student student;

    @NotNull
    @Column(name = "TOKEN", nullable = false, length = Constants.MAX_ACCOUNT_TOKEN_LENGTH, unique = true)
    private String token;

    @Convert(converter = InstantConverter.class)
    @Column(name = "EXPIRATION", nullable = false)
    private Instant expiration;

    @Convert(converter = InstantConverter.class)
    @Column(name = "LAST_EMAIL_SENT")
    private Instant emailSentOn;

    public AccountPasswordResetToken() {
		super();
	}

	public AccountPasswordResetToken(@NotNull Student student, String reset_token) {
		this.student = student;
		this.id = student.getId();
		this.token = reset_token;
	}

    @PrePersist
    public void prePersist() {
        if(expiration == null)
            expiration = Instant.now().plus(Duration.ofDays(Constants.PASSWORD_RESET_TOKEN_DURATION_DAYS));
    }

	public Integer getId() {
		return id;
	}

	protected void setId(Integer id) {
		this.id = id;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
		this.id = student.getId();
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Instant getExpiration() {
		return expiration;
	}

	public void setExpiration(Instant expiration) {
		this.expiration = expiration;
	}
	
	public Instant getEmailSentOn() {
		return emailSentOn;
	}

	public void setEmailSentOn(Instant emailSentOn) {
		this.emailSentOn = emailSentOn;
	}

	@Override
	public String toString() {
		return "AccountPasswordResetToken [id=" + id + ", token=" + token
				+ ", expiration=" + expiration + ", emailSentOn=" + emailSentOn + "]";
	}

}
