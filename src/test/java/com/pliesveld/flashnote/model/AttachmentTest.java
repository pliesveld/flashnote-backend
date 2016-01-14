package com.pliesveld.flashnote.model;

import com.pliesveld.config.SpringTestConfig;
import org.hibernate.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.PathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.*;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringTestConfig.class, loader = AnnotationConfigContextLoader.class)
@Transactional
public class AttachmentTest
{
    @Autowired
    protected SessionFactory sessionFactory;

    Session session;

    @Before
    public void setupSession()
    {
        assertNotNull(sessionFactory);
        session = sessionFactory.getCurrentSession();
        assertNotNull(session);
        session.beginTransaction();
    }

    @After
    public void teardownSession()
    {
        session.getTransaction().rollback();
        session.close();
    }

    private static byte[] readBytesFromFile(String filePath) throws IOException, IllegalAccessException {
        File inputFile = new File(filePath);
        if(!inputFile.exists())
            throw new IllegalAccessException("File not found: " + inputFile.getAbsolutePath());
        FileInputStream inputStream = new FileInputStream(inputFile);

        byte[] fileBytes = new byte[(int) inputFile.length()];
        inputStream.read(fileBytes);
        inputStream.close();

        return fileBytes;
    }

    private static void saveBytesToFile(String filePath, byte[] fileBytes) throws IOException {
        FileOutputStream outputStream = new FileOutputStream(filePath);
        outputStream.write(fileBytes);
        outputStream.close();
    }

    @Test
    public void testStorage() throws IOException, IllegalAccessException {
        AttachmentType expected_type = AttachmentType.AUDIO;

        String expected_file = "puppy.jpg";
        String resource_dir = ClassLoader.getSystemResource(".").getPath();
//        System.out.println("dir: " + resource_dir);

        assertTrue("Could not load test resource: " + expected_file, new ClassPathResource(expected_file).exists()); 
        
        byte[] photoBytes = readBytesFromFile(new ClassPathResource(expected_file).getFile().getAbsolutePath());

        Attachment attachment = new Attachment();
        attachment.setContentType(expected_type);
        attachment.setFileName("puppy.jpg");
        attachment.setFileData(photoBytes);
        Serializable id = session.save(attachment);

        Attachment attachment2 = session.load(Attachment.class,id);
        assertNotNull(attachment2.getContentType());
        assertNotNull(attachment2.getFileName());
        assertEquals(expected_type,attachment2.getContentType());
        assertEquals(expected_file,attachment2.getFileName());
        assertNotNull(attachment2.getFileData());

        String fileout = Paths.get(resource_dir,"puppy_out.jpg").toString();
        saveBytesToFile(fileout,attachment2.getFileData());
    }




}

