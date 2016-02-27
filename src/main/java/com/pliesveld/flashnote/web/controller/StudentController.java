package com.pliesveld.flashnote.web.controller;


import com.pliesveld.flashnote.domain.Student;
import com.pliesveld.flashnote.domain.StudentDetails;
import com.pliesveld.flashnote.exception.StudentNotFoundException;
import com.pliesveld.flashnote.service.StudentService;
import com.pliesveld.flashnote.web.dto.StudentDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/students")
public class StudentController {
    
    private static final Logger LOG = LogManager.getLogger();

    @Autowired
    private StudentService studentService;

    private Student verifyStudent(int id) throws StudentNotFoundException
    {
        Student student = studentService.findStudentById(id);
        if(student == null)
            throw new StudentNotFoundException(id);

        return student;
    }

    @RequestMapping(value="/list", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Iterable<StudentDetails>> getAllStudents()
    {
        LOG.info("Retrieving list of all students");
        Iterable<StudentDetails> allStudents = studentService.findAll();
        return new ResponseEntity<>(allStudents, HttpStatus.OK);
    }

    @RequestMapping(value="",method = RequestMethod.POST)
    public ResponseEntity<Void> createStudent(@Valid @RequestBody StudentDTO studentdto)
    {
        Student studentDetails = studentService.create(studentdto.getName(),studentdto.getEmail(),studentdto.getPassword());
        
        LOG.info("Created studentDetails: " + studentDetails);

        HttpHeaders responseHeaders = new HttpHeaders();
        URI newStudentUri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(studentDetails.getId())
                .toUri();

        responseHeaders.setLocation(newStudentUri);
        return new ResponseEntity<>(null,responseHeaders,HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('USER')")
    @RequestMapping(value="/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> removeStudent(@PathVariable Integer id)
    {
        LOG.info("Deleting student with id " + id);
        studentService.delete(id);
        return new ResponseEntity<>(null,HttpStatus.OK);
    }

    @RequestMapping(value="/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getStudent(@PathVariable Integer id)
    {
        LOG.info("Getting studentDetails by id " + id);
        Student student = verifyStudent(id);
        StudentDTO studentDTO = StudentDTO.convert(student);
        return new ResponseEntity<>(studentDTO,HttpStatus.OK);
    }



    /*
    @RequestMapping(value="/{id}/decks/{deckid}", method = RequestMethod.GET)
    public ResponseEntity<?> listDecks(@PathVariable Integer id, @PathVariable Integer deckid)
    {
        LOG.info("Getting student by id " + id);
        StudentDetails student = verifyStudent(id);
        return new ResponseEntity<>(student,HttpStatus.OK);
    }
    */




/*
Alternatives to apache.commons.collections

Iterator<T> source = ...;
List<T> target = new ArrayList<>();
source.forEachRemaining(target::add);

Iterable<T> source = ...;
source.forEach(target::add);


public static <T> List<T> toList(final Iterable<T> iterable) {
    return StreamSupport.stream(iterable.spliterator(), false)
                        .collect(Collectors.toList());
}
 */

}
