package com.pliesveld.flashnote.web.controller;

import com.pliesveld.flashnote.domain.AccountPasswordResetToken;
import com.pliesveld.flashnote.domain.AccountRegistrationToken;
import com.pliesveld.flashnote.domain.Student;
import com.pliesveld.flashnote.exception.ResourceLimitException;
import com.pliesveld.flashnote.exception.StudentNotFoundException;
import com.pliesveld.flashnote.model.json.request.PasswordResetRequestJson;
import com.pliesveld.flashnote.model.json.request.RegistrationRequestJson;
import com.pliesveld.flashnote.model.json.response.RegistrationResponseJson;
import com.pliesveld.flashnote.security.OnRegistrationCompleteEvent;
import com.pliesveld.flashnote.service.AccountRegistrationService;
import com.pliesveld.flashnote.service.StudentService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static com.pliesveld.flashnote.model.json.response.RegistrationResponseJson.*;

@RestController
@RequestMapping(path = "/account")
@CrossOrigin(origins = "http://localhost:4455", allowedHeaders = "*", methods = {RequestMethod.POST, RequestMethod.OPTIONS})
public class RegistrationController {
    private static final Logger LOG = LogManager.getLogger();

    @Autowired
    private StudentService studentService;

    @Autowired
    private AccountRegistrationService registrationService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @RequestMapping(path = "/sign-up", method = RequestMethod.OPTIONS)
    @CrossOrigin
    @ResponseStatus(HttpStatus.OK)
    public void discoverOptions()
    {
    }

    @RequestMapping(path = "/sign-up", method = RequestMethod.POST )
    public ResponseEntity<RegistrationResponseJson> processRegistrationMessage(@Valid @RequestBody RegistrationRequestJson registrationRequestJson, HttpServletRequest request)
    {
        String ipAddr = request.getHeader("X-FORWARDED-FOR");
        if(ipAddr == null)
            ipAddr = request.getRemoteAddr();

        LOG.info("from {} Processing registration request {}", ipAddr, registrationRequestJson);

        String email = registrationRequestJson.getEmail();
        String name = registrationRequestJson.getName();
        String password = registrationRequestJson.getPassword();

        RegistrationResponseJson response = null;

        HttpStatus statusCode = HttpStatus.OK;

        if(studentService.findByEmail(email) != null)
        {
            response = new RegistrationResponseJson(EMAIL_TAKEN, "Email address has already been registered");
            statusCode = HttpStatus.FORBIDDEN;

        } else  if(studentService.findByName(name) != null)
        {
            response = new RegistrationResponseJson(NAME_TAKEN, "Name has already been registered");
            statusCode = HttpStatus.FORBIDDEN;
        } else {

            Student student = registrationService.createStudent(name, email, password);

            AccountRegistrationToken registration = registrationService.createAccountRegistration(student);

            String token = registration.getToken();

            String confirmURL = MvcUriComponentsBuilder
                    .fromController(RegistrationController.class)
                    .path("/confirm")
                    .queryParam("token", token)
                .build().toUriString();


            registrationService.emailVerificationConfirmationURLtoAccountHolder(student, confirmURL);
            response = new RegistrationResponseJson(EMAIL_SENT, "A confirmation email has been sent to your address " + email);
        }


        return new ResponseEntity<>(response,statusCode);
    }

    @RequestMapping(path = "/confirm", method = RequestMethod.GET)
    public ResponseEntity<?> processRegistrationConfirmation(@Valid
            @RequestParam(value = "token", required = true) String token)
    {
        LOG.debug("Checking token={}",token);
        Student student = registrationService.processRegistrationConfirmation(token);
        if(student != null)
        {
            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(student));
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @RequestMapping(path = "/reset", method = RequestMethod.POST )
    public ResponseEntity<RegistrationResponseJson> processAccountPasswordResetRequest(@Valid @RequestBody PasswordResetRequestJson passwordResetRequestJson, HttpServletRequest request)
    {
        String ipAddr = request.getHeader("X-FORWARDED-FOR");
        if(ipAddr == null)
            ipAddr = request.getRemoteAddr();

        LOG.info("from {} Processing registration request {}", ipAddr, passwordResetRequestJson);

        String email = passwordResetRequestJson.getEmail();

        RegistrationResponseJson response = null;

        HttpStatus statusCode = HttpStatus.OK;
        Student student = null;

        if((student = studentService.findByEmail(email)) == null)
        {
            throw new StudentNotFoundException(email);
        }

        AccountPasswordResetToken resetToken = registrationService.findOrCreatePasswordResetToken(student);

        Instant last_email = resetToken.getEmailSentOn();
        Instant email_threshold = Instant.now().plus(3L, ChronoUnit.HOURS);

        if(last_email != null)
        {
            if(last_email.isBefore(email_threshold))
            {
                LOG.info("refusing to send email before {}", email_threshold);
                throw new ResourceLimitException("Cannot resend email until " + email_threshold);
            }
        }

        String confirmURL = MvcUriComponentsBuilder
                .fromController(RegistrationController.class)
                .path("/reset/confirm")
                .queryParam("token", resetToken.getToken())
                .build().toUriString();

        registrationService.emailPasswordResetToAccountHolder(email, confirmURL);
        response = new RegistrationResponseJson(EMAIL_SENT, "A password reset confirmation link has been sent to your email address " + email);

        return new ResponseEntity<>(response,statusCode);
    }


    @RequestMapping(path = "/reset/confirm", method = RequestMethod.GET )
    public ResponseEntity<RegistrationResponseJson> processResetConfirmMessage(
            @RequestParam(value = "token", required = true) String token)

    {
        Student student = registrationService.processPasswordResetConfirmation(token);

        RegistrationResponseJson response = null;
        HttpStatus statusCode = HttpStatus.OK;

        if(student == null)
        {
            statusCode = HttpStatus.NOT_FOUND;
            return new ResponseEntity<>(statusCode);
        }

        String email = student.getEmail();
        String temp_password = student.getPassword();

        registrationService.emailTemporaryPasswordToAccountHolder(email,temp_password);
        response = new RegistrationResponseJson(EMAIL_SENT, "A temporary password has been sent to your address email address " + email);

        return new ResponseEntity<>(response,statusCode);
    }

}
