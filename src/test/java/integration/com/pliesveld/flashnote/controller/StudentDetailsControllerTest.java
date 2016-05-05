package com.pliesveld.flashnote.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pliesveld.flashnote.domain.Student;
import com.pliesveld.flashnote.domain.StudentDetails;
import com.pliesveld.flashnote.domain.StudentRole;
import com.pliesveld.flashnote.model.json.request.NewStudentDetails;
import com.pliesveld.flashnote.service.AccountRegistrationService;
import com.pliesveld.flashnote.service.AdminService;
import com.pliesveld.flashnote.spring.Profiles;
import com.pliesveld.flashnote.spring.SpringEntityTestConfig;
import com.pliesveld.flashnote.util.generator.StudentGenerator;
import com.pliesveld.flashnote.web.controller.AdministrationController;
import com.pliesveld.flashnote.web.controller.StudentController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;


@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles(Profiles.INTEGRATION_TEST)
@ContextConfiguration(classes = { MockServletContext.class, SpringEntityTestConfig.class }, loader = AnnotationConfigContextLoader.class)
@WebAppConfiguration
public class StudentDetailsControllerTest {
    private static final Logger LOG = LogManager.getLogger();

    @Mock
    private AdminService adminService;

    @Mock
    private AccountRegistrationService registrationService;

    @InjectMocks
    StudentController studentController;

    @InjectMocks
    AdministrationController adminController;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        MockitoAnnotations.initMocks(this);
        mockMvc = standaloneSetup(studentController,adminController).build();
    }


    @Test
    public void getAllStudentsEmpty() throws Exception {
        when(adminService.findAllStudentDetails()).thenReturn(new ArrayList<StudentDetails>());
        mockMvc.perform(get("/admin/student/list"))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }

    @Test
    public void createStudent() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Student student = StudentGenerator.randomizedStudent();
        student.setRole(StudentRole.ROLE_USER);
        student.setId(4);
        NewStudentDetails newStudent = new NewStudentDetails();
        newStudent.setName(student.getStudentDetails().getName());
        newStudent.setEmail(student.getEmail());
        newStudent.setPassword(student.getPassword());
        newStudent.setRole(StudentRole.ROLE_USER);


        final String JSON_DATA = mapper.writeValueAsString(newStudent);
        LOG.info(JSON_DATA);
        when(registrationService.createStudent(any(String.class),any(String.class),any(String.class))).thenReturn(student);
        MvcResult result = mockMvc.perform(post("/admin/student/create")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON_DATA))

            //.andDo(print())
            .andExpect(status().isCreated())

         //   .andExpect(content().contentType(MediaType.APPLICATION_JSON))
         //  .andExpect(content().string("{JSON_DATA}"));
            .andReturn();


        MockHttpServletResponse mhsr = result.getResponse();
        LOG.info(mhsr.getHeaderNames());
        assertTrue(mhsr.getHeaderNames().contains("Location"));
        String location = mhsr.getHeader("Location");
        LOG.info(location);


    }


}
