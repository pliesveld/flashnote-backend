package com.pliesveld.flashnote.web.controller;


import com.pliesveld.flashnote.domain.AbstractStatement;
import com.pliesveld.flashnote.domain.Deck;
import com.pliesveld.flashnote.domain.Student;
import com.pliesveld.flashnote.domain.StudentDetails;
import com.pliesveld.flashnote.exception.StudentNotFoundException;
import com.pliesveld.flashnote.model.json.response.ExistingStudentDetails;
import com.pliesveld.flashnote.service.StudentService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value="/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> removeStudent(@PathVariable Integer id)
    {
        LOG.info("Deleting student with id " + id);
        studentService.delete(id);
        return new ResponseEntity<>(null,HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<?> getAllStudent(Pageable page)
    {
        return ResponseEntity.ok(studentService.findAll(page));
    }

    @RequestMapping(value="/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getStudent(@PathVariable Integer id)
    {
        LOG.info("Getting studentDetails by id " + id);
        Student student = verifyStudent(id);
        ExistingStudentDetails existingStudent = ExistingStudentDetails.convert(student);
        return new ResponseEntity<>(existingStudent,HttpStatus.OK);
    }

    @RequestMapping(value="/{id}/statements", method = RequestMethod.GET)
    @ResponseBody
    @ResponseStatus(code = HttpStatus.OK)
    public List<AbstractStatement> retrieveStudentStatements(@PathVariable("id") int student_id)
    {
        StudentDetails studentDetails = verifyStudent(student_id).getStudentDetails();
        return studentService.findStatementsBy(studentDetails);
    }

    @RequestMapping(value="/{id}/decks", method = RequestMethod.GET)
    @ResponseBody
    @ResponseStatus(code = HttpStatus.OK)
    public List<Deck> retrieveStudentDecks(@PathVariable("id") int student_id)
    {
        return studentService.findDecksByOwner(student_id);
    }

}
