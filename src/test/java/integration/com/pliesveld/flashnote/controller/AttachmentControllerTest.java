package com.pliesveld.flashnote.controller;

import com.pliesveld.flashnote.domain.AttachmentBinary;
import com.pliesveld.flashnote.service.AttachmentService;
import com.pliesveld.flashnote.spring.Profiles;
import com.pliesveld.flashnote.spring.SpringEntityTestConfig;
import com.pliesveld.flashnote.spring.data.SpringDataConfig;
import com.pliesveld.flashnote.spring.db.PersistenceContext;
import com.pliesveld.flashnote.web.controller.AttachmentController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.InputStream;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles(Profiles.INTEGRATION_TEST)
@ContextConfiguration(classes = {PersistenceContext.class,SpringDataConfig.class, SpringEntityTestConfig.class}, loader = AnnotationConfigWebContextLoader.class)
@WebAppConfiguration
public class AttachmentControllerTest {
    private static final Logger LOG = LogManager.getLogger();

    @Mock
    private AttachmentService attachmentService;

    @InjectMocks
    AttachmentController attachmentController;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        //       MockHttpServletRequest request = new MockHttpServletRequest();
//        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(attachmentController).build();
    }

    @Test
    public void uploadAttachment() throws Exception {

        MockMultipartFile firstFile = new MockMultipartFile("data", "filename.txt", "text/plain", "some xml".getBytes());
        MockMultipartFile secondFile = new MockMultipartFile("data", "other-file-name.data", "text/plain", "some other type".getBytes());
        MockMultipartFile jsonFile = new MockMultipartFile("json", "", "application/json", "{\"json\": \"someValue\"}".getBytes());

        InputStream is = new ClassPathResource("puppy.jpg").getInputStream();

        MockMultipartFile binaryFile = new MockMultipartFile("file","puppy.jpg","image/jpeg",is);

        AttachmentBinary attachmentBinary = new AttachmentBinary();
        attachmentBinary.setId(4);

        Mockito.when(attachmentService.storeAttachment(Mockito.any(AttachmentBinary.class))).thenReturn(attachmentBinary);

        mockMvc.perform(MockMvcRequestBuilders.fileUpload("/attachments")
                .file(binaryFile))
                // .file(jsonFile)
                //.andDo(print())
                .andExpect(status().is(HttpStatus.OK.value()))

                ;




    }

}
