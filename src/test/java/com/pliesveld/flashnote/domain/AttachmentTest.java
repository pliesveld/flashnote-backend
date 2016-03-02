package com.pliesveld.flashnote.domain;

import com.pliesveld.spring.DefaultTestAnnotations;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.io.*;
import java.nio.file.Paths;
import java.time.Instant;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@DefaultTestAnnotations
@Transactional
public class AttachmentTest
{
    private static final Logger LOG = LogManager.getLogger();

    @PersistenceContext
    protected EntityManager entityManager;

    @Before
    public void setupSession()
    {
        assertNotNull(entityManager);
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
        AttachmentType expected_type = AttachmentType.IMAGE;
        String resource_dir = ClassLoader.getSystemResource(".").getPath();
        String expected_file = "puppy.jpg";

        Serializable id = null;

        {
    //        System.out.println("dir: " + resource_dir);

            assertTrue("Could not load test resource: " + expected_file, new ClassPathResource(expected_file).exists());

            byte[] photoBytes = readBytesFromFile(new ClassPathResource(expected_file).getFile().getAbsolutePath());

            Attachment attachment = new Attachment();
            attachment.setContentType(expected_type);
            attachment.setFileName("puppy.jpg");
            attachment.setFileData(photoBytes);
            entityManager.persist(attachment);
            entityManager.flush();      // flush transaction to database
            entityManager.clear();      // detach previous entity so retrieval SQL statement is executed
            id = attachment.getId();
        }

        Instant creationTime = null;
        AttachmentType attachmentType = null;
        int length;

        {
            assertNotNull(id);
            Attachment attachment2 = entityManager.find(Attachment.class,id);
            assertNotNull(attachment2.getAttachmentType());
            assertNotNull(attachment2.getFileName());
            assertEquals(expected_type,attachment2.getAttachmentType());
            assertEquals(expected_file,attachment2.getFileName());
            assertNotNull(attachment2.getFileData());

            String fileout = Paths.get(resource_dir,"puppy_out.jpg").toString();
            saveBytesToFile(fileout,attachment2.getFileData());

            // TODO: verify files match

            creationTime = attachment2.getModifiedOn();
            attachmentType = attachment2.getAttachmentType();
            length = attachment2.getFileLength();

            entityManager.clear();
        }

        /* Unit testing named query to fetch header information only */
        {
            TypedQuery<AttachmentHeader> query = entityManager.createNamedQuery("Attachment.findHeaderByAttachmentId",AttachmentHeader.class);
            assertNotNull(id);
            query.setParameter("id",id);

            AttachmentHeader header = query.getSingleResult();

            assertNotNull(header);
            assertNotNull(header.getContentType());
            assertNotNull(header.getLength());
            assertNotNull(header.getModified());

            assertEquals(attachmentType,header.getContentType());
            assertEquals(length,header.getLength());
            assertEquals(creationTime,header.getModified());
        }

    }




}

