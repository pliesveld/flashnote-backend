package com.pliesveld.flashnote.web.controller;

import com.pliesveld.flashnote.domain.Student;
import com.pliesveld.flashnote.domain.StudentDetails;
import com.pliesveld.flashnote.model.json.request.NewStudentDetails;
import com.pliesveld.flashnote.service.AccountRegistrationService;
import com.pliesveld.flashnote.service.AdminService;
import com.pliesveld.flashnote.service.AttachmentService;
import com.pliesveld.flashnote.service.StudentService;
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
@RequestMapping(value="/admin")
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

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(value = "/student/create", method = RequestMethod.POST)
    public ResponseEntity<Void> createStudent(@Valid @RequestBody NewStudentDetails studentdto)
    {
        Student studentDetails = registrationService.createStudent(studentdto.getName(),studentdto.getEmail(),studentdto.getPassword());

        LOG.info("User created account: " + studentDetails);

        HttpHeaders responseHeaders = new HttpHeaders();
        URI newStudentUri = MvcUriComponentsBuilder
                .fromController(StudentController.class)
                .path("/{id}")
                .buildAndExpand(studentDetails.getId())
                .toUri();

        responseHeaders.setLocation(newStudentUri);
        return new ResponseEntity<>(null,responseHeaders, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(value="/student/list", method = RequestMethod.GET)
    public ResponseEntity<Iterable<StudentDetails>> getAllStudents()
    {
        LOG.info("Retrieving list of all students");
        Iterable<StudentDetails> allStudents = adminService.findAllStudentDetails();
        return new ResponseEntity<>(allStudents, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(value="/remove/attachment/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteAttachment(@PathVariable("id") int id)
    {
        attachmentService.removeAttachmentById(id);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(value="/remove/student/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteStudent(@PathVariable("id") int id)
    {
        adminService.deleteStudent(id);
    }

}
