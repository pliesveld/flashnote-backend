package com.pliesveld.flashnote.web.controller;


import com.pliesveld.flashnote.domain.Student;
import com.pliesveld.flashnote.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/student")
public class StudentController {
    final Logger logger = LoggerFactory.getLogger(StudentController.class);

    @Autowired
    private StudentService studentService;

    @RequestMapping(value="/list", method = RequestMethod.GET)
    public ResponseEntity<Iterable<Student>> listdata()
    {
        logger.info("Retrieving list of all students");
        Iterable<Student> allStudents = studentService.findAll();
        return new ResponseEntity<>(allStudents, HttpStatus.OK);
    }

    @RequestMapping(value="",method=RequestMethod.POST)
    public ResponseEntity<?> createStudent(@RequestBody Student student)
    {
        logger.info("Creating student: " + student);

        studentService.getDao().save(student);

        HttpHeaders responseHeaders = new HttpHeaders();
        URI newStudentUri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(student.getId())
                .toUri();
        responseHeaders.setLocation(newStudentUri);
        return new ResponseEntity<>(null,responseHeaders,HttpStatus.CREATED);
    }

    @RequestMapping(value="/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getStudent(@PathVariable Integer id)
    {
        logger.info("Getting student by id " + id);
        Student student = studentService.findById(id);
        HttpStatus status = (student == null ? HttpStatus.NOT_FOUND : HttpStatus.OK);
        return new ResponseEntity<>(studentService.findById(id),status);
    }



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
