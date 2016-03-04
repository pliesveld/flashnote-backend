package com.pliesveld.flashnote.unit.domain.entity;

import com.pliesveld.flashnote.domain.Attachment;
import com.pliesveld.flashnote.domain.AttachmentType;
import com.pliesveld.flashnote.domain.dto.AttachmentHeader;
import com.pliesveld.flashnote.unit.spring.DefaultTestAnnotations;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.io.*;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

        Resource resource = new ClassPathResource(expected_file);
        Serializable id = null;

        {
    //        System.out.println("dir: " + resource_dir);


            assertTrue("Could not load test resource: " + resource.getFile(), resource.exists());

            byte[] photoBytes = readBytesFromFile(resource.getFile().getAbsolutePath());

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

            verifyFiles(resource.getFile(),new File(fileout));
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
    private String computeHash(InputStream inputStream) throws IOException {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            fail("Could not load message digest sha-256");
        }

        byte[] dataBytes = new byte[1024];

        int nread = 0;
        while((nread = inputStream.read(dataBytes)) != -1)
        {
            md.update(dataBytes,0,nread);
        }

        byte[] mdbytes = md.digest();

        StringBuffer hexString = new StringBuffer();
    	for (int i=0;i<mdbytes.length;i++) {
    	  hexString.append(Integer.toHexString(0xFF & mdbytes[i]));
    	}

        return hexString.toString();
    }

    private void verifyFiles(File file_in, File file_out) {
        assertNotNull(file_in);
        assertNotNull(file_out);



        try {
            FileInputStream fis = new FileInputStream(file_in);
            FileInputStream fis2 = new FileInputStream(file_out);

            try {
                String hash1 = computeHash(fis);
                String hash2 = computeHash(fis2);

                assertEquals(hash1,hash2);
            } catch(IOException ioe) {
                fail(ioe.getMessage());
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

    }


}

