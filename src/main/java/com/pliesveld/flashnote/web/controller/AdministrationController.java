package com.pliesveld.flashnote.web.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.pliesveld.flashnote.domain.AbstractAttachment;
import com.pliesveld.flashnote.domain.Student;
import com.pliesveld.flashnote.model.json.Views;
import com.pliesveld.flashnote.model.json.request.NewStudentRequestJson;
import com.pliesveld.flashnote.service.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping(value = "/admin")
public class AdministrationController {
    private static final Logger LOG = LogManager.getLogger();

    @Autowired
    private AccountRegistrationService registrationService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private AttachmentService attachmentService;

    @Autowired
    private DeckService deckService;

    @Autowired
    private BankService bankService;

    @Autowired
    private CardService cardService;

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/student/create", method = RequestMethod.POST)
    public ResponseEntity<Void> createStudent(@Valid @RequestBody NewStudentRequestJson studentdto) {
        Student student = registrationService.createStudent(studentdto.getName(), studentdto.getEmail(), studentdto.getPassword());

        LOG.info("User created account: " + student);

        HttpHeaders responseHeaders = new HttpHeaders();
        URI newStudentUri = MvcUriComponentsBuilder
                .fromController(StudentController.class)
                .path("/{id}")
                .buildAndExpand(student.getId())
                .toUri();

        responseHeaders.setLocation(newStudentUri);
        return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/students", method = RequestMethod.GET)
    public ResponseEntity<Iterable<Student>> getAllStudents() {
        LOG.info("Retrieving list of all students");
        Iterable<Student> allStudents = adminService.findAllStudent();
        return new ResponseEntity<>(allStudents, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/remove/attachment/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteAttachment(@PathVariable("id") int id) {
        attachmentService.removeAttachmentById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/remove/student/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteStudent(@PathVariable("id") int id) {
        adminService.deleteStudent(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/attachments", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> retrieveAllAttachments() {
        return ResponseEntity.ok(attachmentService.findAllAttachments());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/attachments/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> retrieveAttachmentById(@PathVariable("id") int id) {
        AbstractAttachment attachment = attachmentService.findAttachmentById(id);
        return ResponseEntity.ok(attachment);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/questionbanks", method = RequestMethod.GET)
    @JsonView(Views.Internal.class)
    public ResponseEntity<?> retrieveAllDecks() {
        return ResponseEntity.ok(deckService.findAllDecks());
    }


    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/decks", method = RequestMethod.GET)
    @JsonView(Views.Internal.class)
    public ResponseEntity<?> retrieveAllQuestionBanks() {
        return ResponseEntity.ok(bankService.findAllQuestionBanks());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/questions", method = RequestMethod.GET)
    @JsonView(Views.Internal.class)
    public ResponseEntity<?> retrieveAllQuestions() {
        return ResponseEntity.ok(cardService.findAllQuestions());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/answers", method = RequestMethod.GET)
    @JsonView(Views.Internal.class)
    public ResponseEntity<?> retrieveAllAnswers() {
        return ResponseEntity.ok(cardService.findAllAnswers());
    }


}
