package com.pliesveld.flashnote.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pliesveld.flashnote.domain.Student;
import com.pliesveld.flashnote.model.json.request.NewStudentRequestJson;
import com.pliesveld.flashnote.service.AccountRegistrationService;
import com.pliesveld.flashnote.service.AdminService;
import com.pliesveld.flashnote.service.StudentService;
import com.pliesveld.flashnote.util.generator.StudentGenerator;
import com.pliesveld.flashnote.web.controller.AdministrationController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/*
    Traditional unit testing of controller components.  The dependencies of the controller component
     are created within this unit test, and injected into the into an object instance of the controller
     under test.

     The behavior of dependencies' methods can be defined using Mockitos when() function.

     The dependencies' method behavior can be expected with Mockitos verify() function.

     Finally the response object is inspected for correctness.

     Note -- AdministrationController is treated as a POJO.  Not tested is the controllers request-mapping,
     validations, data bindings, and any associated exception handlers.
 */
public class StudentControllerTestMock {
    private static final Logger LOG = LogManager.getLogger();

    /*
        Dependency of AdministrationController whose behavior will be overridden
     */
    @Mock
    private StudentService studentService;

    @Mock
    private AdminService adminService;

    @Mock
    private AccountRegistrationService accountRegistrationService;


    @Before
    public void setUp() throws Exception {
        /* mock servlet request to handle usage of UriComponentsBuilder .. .fromCurrentRequest()
         from inside AdministrationController.createStudent(student_name,...);
         */
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAllStudents() {
        /*
            Instantiates a AdministrationController object, and injects the mocked dependency
         */

        AdministrationController administrationController = new AdministrationController();
        ReflectionTestUtils.setField(administrationController, "adminService", adminService);


        ReflectionTestUtils.setField(administrationController, "studentService", studentService);

        /*
            When the mocked method findAllStudent() is invoked, the an empty ArrayList<Student> is returned.
         */
        when(adminService.findAllStudent()).thenReturn(new ArrayList<Student>());

        /*
            Invoke the Controller methods that depend on the mocked objects.
         */
        ResponseEntity<Iterable<Student>> allStudentsEntry = administrationController.getAllStudents();

        /*
            Checks that the underlying mocked dependency invoked the method findAllStudent once.
         */
        verify(adminService, times(1)).findAllStudent();

        /*
            Asserts that the HTTP status coded returned by the Controller was OK.
            Asserts that the response returned Iteration of student elements was zero.
         */
        assertEquals(HttpStatus.OK, allStudentsEntry.getStatusCode());
        List<Student> target = new ArrayList<>();
        allStudentsEntry.getBody().forEach(target::add);
        assertEquals(0, target.size());
    }

    @Test
    public void testCreateStudent() throws Exception {

        AdministrationController administrationController = new AdministrationController();
        ReflectionTestUtils.setField(administrationController, "studentService", studentService);
        ReflectionTestUtils.setField(administrationController, "registrationService", accountRegistrationService);

        ObjectMapper mapper = new ObjectMapper();
        Student student = StudentGenerator.randomizedStudent();
        NewStudentRequestJson newStudent = new NewStudentRequestJson();
        newStudent.setName(student.getName());
        newStudent.setEmail(student.getEmail());
        newStudent.setPassword(student.getPassword());

        final String JSON_DATA = mapper.writeValueAsString(newStudent);
        LOG.info(JSON_DATA);
        when(accountRegistrationService.createStudent(any(String.class), any(String.class), any(String.class))).thenReturn(student);

        ResponseEntity<?> creationResponse = administrationController.createStudent(newStudent);
        assertEquals(HttpStatus.CREATED, creationResponse.getStatusCode());
    }

}
