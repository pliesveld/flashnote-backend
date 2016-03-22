package com.pliesveld.flashnote.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pliesveld.flashnote.domain.AccountRegistrationToken;
import com.pliesveld.flashnote.domain.Student;
import com.pliesveld.flashnote.model.json.request.RegistrationRequestJson;
import com.pliesveld.flashnote.service.AccountRegistrationService;
import com.pliesveld.flashnote.service.StudentService;
import com.pliesveld.flashnote.spring.Profiles;
import com.pliesveld.flashnote.unit.spring.SpringEntityTestConfig;
import com.pliesveld.flashnote.util.generator.StudentGenerator;
import com.pliesveld.flashnote.web.controller.RegistrationController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;


@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles(Profiles.INTEGRATION_TEST)
//@SpringApplicationConfiguration(classes = FlashnoteContainerApplication.class)
@ContextConfiguration(classes = { MockServletContext.class, SpringEntityTestConfig.class }, loader = AnnotationConfigContextLoader.class)
@WebAppConfiguration
public class RegistrationControllerTest {
    private static final Logger LOG = LogManager.getLogger();

    @Mock
    private AccountRegistrationService accountRegistrationService;

    @Mock
    private StudentService studentService;

    @InjectMocks
    RegistrationController registrationController;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
 //       MockHttpServletRequest request = new MockHttpServletRequest();
//        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        MockitoAnnotations.initMocks(this);
        mockMvc = standaloneSetup(registrationController).build();
    }

    @Test
    public void registerNewAccount() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        String confirm_token = "B6EFF3D";
        Student student = StudentGenerator.randomizedStudent();
        AccountRegistrationToken registration = new AccountRegistrationToken();
        registration.setToken(confirm_token);

        RegistrationRequestJson jsonRequest = new RegistrationRequestJson();

        jsonRequest.setName(student.getStudentDetails().getName());
        jsonRequest.setEmail(student.getEmail());
        jsonRequest.setPassword(student.getPassword());

        final String JSON_DATA = mapper.writeValueAsString(jsonRequest);
        LOG.info(JSON_DATA);

        when(accountRegistrationService.createStudent(any(String.class),any(String.class),any(String.class))).thenReturn(student);
        when(accountRegistrationService.createAccountRegistration(any(Student.class))).thenReturn(registration);
        when(studentService.findByName(any(String.class))).thenReturn(null);
        when(studentService.findByEmail(any(String.class))).thenReturn(null);

        MvcResult result = mockMvc.perform(post("/registration/sign-up")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON_DATA))

            .andExpect(status().is2xxSuccessful())
         //   .andExpect(content().contentType(MediaType.APPLICATION_JSON))
         //  .andExpect(content().string("{JSON_DATA}"));
            .andReturn();

    }

    @Test
    public void confirmNewAccount() throws Exception {
        String confirm_token = "B6EFF3D";
        Student student = StudentGenerator.randomizedStudent();

        when(accountRegistrationService.processRegistrationConfirmation(any(String.class))).thenReturn(student);

        mockMvc.perform(get("/registration/confirm")
                .param("token", confirm_token))
                .andExpect(status().is2xxSuccessful());
    }


}
