package com.pliesveld.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pliesveld.flashnote.domain.Student;
import com.pliesveld.flashnote.domain.StudentDetails;
import com.pliesveld.flashnote.service.StudentService;
import com.pliesveld.flashnote.spring.FlashnoteContainerApplication;
import com.pliesveld.flashnote.web.controller.StudentController;
import com.pliesveld.flashnote.web.dto.StudentDTO;
import com.pliesveld.spring.SpringTestConfig;
import com.pliesveld.test.generator.StudentGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

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
@ActiveProfiles("h2")
@SpringApplicationConfiguration(classes = FlashnoteContainerApplication.class)
@ContextConfiguration(classes = { MockServletContext.class,SpringTestConfig.class }, loader = AnnotationConfigContextLoader.class)
@WebAppConfiguration
public class StudentDetailsControllerTest {
    private static final Logger LOG = LogManager.getLogger();

    @Mock
    private StudentService studentService;

    @InjectMocks
    StudentController studentController;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
 //       MockHttpServletRequest request = new MockHttpServletRequest();
//        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        MockitoAnnotations.initMocks(this);
        mockMvc = standaloneSetup(studentController).build();
    }

    @Test
    public void getAllStudentsEmpty() throws Exception {
        when(studentService.findAll()).thenReturn(new ArrayList<StudentDetails>());
        mockMvc.perform(get("/students/list"))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }

    @Test
    public void createStudent() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        Student student = StudentGenerator.randomizedStudent();
        StudentDTO studentDTO = StudentDTO.convert(student);

        final String JSON_DATA = mapper.writeValueAsString(studentDTO);
        LOG.info(JSON_DATA);
        when(studentService.create(any(String.class),any(String.class),any(String.class))).thenReturn(student);
        MvcResult result = mockMvc.perform(post("/students")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSON_DATA))

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
