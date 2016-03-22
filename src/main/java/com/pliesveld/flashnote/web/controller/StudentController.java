package com.pliesveld.flashnote.web.controller;


import com.pliesveld.flashnote.domain.Student;
import com.pliesveld.flashnote.domain.StudentDetails;
import com.pliesveld.flashnote.exception.StudentNotFoundException;
import com.pliesveld.flashnote.model.json.response.ExistingStudentDetails;
import com.pliesveld.flashnote.service.StudentService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
        ExistingStudentDetails existingStudent = ExistingStudentDetails.convert(student);
        return new ResponseEntity<>(existingStudent,HttpStatus.OK);
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
    @RequestMapping(value="",method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> createStudent(@Valid @RequestBody NewStudentDetails studentdto)
    {
        Student studentDetails = studentService.createStudent(studentdto.getName(),studentdto.getEmail(),studentdto.getPassword());

        LOG.info("Created studentDetails: " + studentDetails);

        HttpHeaders responseHeaders = new HttpHeaders();
        URI newStudentUri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(studentDetails.getId())
                .toUri();

        responseHeaders.setLocation(newStudentUri);
        return new ResponseEntity<>(null,responseHeaders,HttpStatus.CREATED);
    }*/



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
