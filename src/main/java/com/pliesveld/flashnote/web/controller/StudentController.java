package com.pliesveld.flashnote.web.controller;


import com.pliesveld.flashnote.domain.Deck;
import com.pliesveld.flashnote.domain.Student;
import com.pliesveld.flashnote.exception.StudentNotFoundException;
import com.pliesveld.flashnote.service.StudentService;
import com.pliesveld.flashnote.web.dto.StudentDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Set;

@RestController
@RequestMapping("/students")
public class StudentController {
    final Logger logger = LoggerFactory.getLogger(StudentController.class);

    @Autowired
    private StudentService studentService;

    private Student verifyStudent(int id) throws StudentNotFoundException
    {
        Student student = studentService.findById(id);
        if(student == null)
            throw new StudentNotFoundException(id);

        return student;
    }

    @RequestMapping(value="/list", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Iterable<Student>> listdata()
    {
        logger.info("Retrieving list of all students");
        Iterable<Student> allStudents = studentService.findAll();
        return new ResponseEntity<>(allStudents, HttpStatus.OK);
    }

    @RequestMapping(value="",method=RequestMethod.POST)
    public ResponseEntity<?> createStudent(@Valid @RequestBody StudentDTO studentdto)
    {
        Student student = studentService.create(studentdto.getName(),studentdto.getEmail(),studentdto.getPassword());
        
        logger.info("Created student: " + student);

        HttpHeaders responseHeaders = new HttpHeaders();
        URI newStudentUri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(student.getId())
                .toUri();

        responseHeaders.setLocation(newStudentUri);
        return new ResponseEntity<>(null,responseHeaders,HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(value="/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> removeStudent(@PathVariable Integer id)
    {
        logger.info("Deleting student with id " + id);
        studentService.delete(id);
        return new ResponseEntity<>(null,HttpStatus.OK);
    }

    @RequestMapping(value="/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getStudent(@PathVariable Integer id)
    {
        logger.info("Getting student by id " + id);
        Student student = verifyStudent(id);
        return new ResponseEntity<>(student,HttpStatus.OK);
    }

    @RequestMapping(value="/{id}/decks", method = RequestMethod.GET)
    public ResponseEntity<?> listDecks(@PathVariable Integer id)
    {
        logger.info("Listing decks of student by id " + id);
        Student student = verifyStudent(id);
        Set<Deck> decks = student.getDecks();
        return new ResponseEntity<>(decks,HttpStatus.OK);
    }

    /*
    @RequestMapping(value="/{id}/decks/{deckid}", method = RequestMethod.GET)
    public ResponseEntity<?> listDecks(@PathVariable Integer id, @PathVariable Integer deckid)
    {
        logger.info("Getting student by id " + id);
        Student student = verifyStudent(id);
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
