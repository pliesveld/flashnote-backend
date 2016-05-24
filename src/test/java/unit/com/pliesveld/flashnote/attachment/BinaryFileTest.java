package com.pliesveld.flashnote.attachment;

import com.pliesveld.flashnote.domain.AttachmentBinary;
import com.pliesveld.flashnote.domain.AttachmentType;
import com.pliesveld.flashnote.model.json.response.AttachmentHeader;
import com.pliesveld.flashnote.repository.AttachmentBinaryRepository;
import com.pliesveld.flashnote.repository.AttachmentRepository;
import com.pliesveld.flashnote.spring.DefaultEntityTestAnnotations;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.*;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@DefaultEntityTestAnnotations
@Transactional
public class BinaryFileTest
{
    private static final Logger LOG = LogManager.getLogger();

    @PersistenceContext
    protected EntityManager entityManager;

    @Autowired
    AttachmentBinaryRepository attachmentBinaryRepository;

    @Autowired
    AttachmentRepository attachmentRepository;

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

        String expected_file = "puppy.jpg";
        String output_file = "puppy.jpg";

        Resource resource = new ClassPathResource(expected_file, this.getClass());
        Resource resource_out = new ClassPathResource(output_file, this.getClass());
        int id;

        LOG.info("Storing binary file attachment");
        {
            assertTrue("Could not load test resource: " + resource.getFile(), resource.exists());

            byte[] photoBytes = readBytesFromFile(resource.getFile().getAbsolutePath());

            AttachmentBinary attachment = new AttachmentBinary();
            attachment.setAttachmentType(expected_type);
            attachment.setFileName("puppy.jpg");
            attachment.setContents(photoBytes);

            attachment = attachmentBinaryRepository.save(attachment);
            assertEquals(photoBytes.length,attachment.getFileLength());
            id = attachment.getId();
            entityManager.flush();
            entityManager.clear();
        }

        Instant creationTime = null;
        AttachmentType attachmentType = null;
        int length;
        assertNotNull(id);

        LOG.info("Fetching binary file attachment");
        {

            AttachmentBinary attachment2 = attachmentBinaryRepository.findOne(id);
            assertNotNull(attachment2.getAttachmentType());
            assertNotNull(attachment2.getFileName());
            assertEquals(expected_type,attachment2.getAttachmentType());
            assertEquals(expected_file,attachment2.getFileName());
            assertNotNull(attachment2.getContents());

            saveBytesToFile(resource_out.getFilename(),attachment2.getContents());

            verifyFiles(resource.getFile(),resource_out.getFile());
            // TODO: verify files match

            creationTime = attachment2.getModifiedOn();
            attachmentType = attachment2.getAttachmentType();
            length = attachment2.getFileLength();

            entityManager.flush();
            entityManager.clear();
        }


        LOG.info("Fetching binary file, content only");
        {
            AttachmentBinary attachment3 = attachmentBinaryRepository.findOne(id);
            assertNotNull(attachment3.getContents());
            entityManager.flush();
            entityManager.clear();
        }


        LOG.info("Executing find header query.");
        /* Unit testing named query to fetch header information only */
        {
            AttachmentHeader header = attachmentRepository.findAttachmentHeaderById(id);
            assertNotNull(header);
            assertNotNull(header.getContentType());
            assertNotNull(header.getLength());
            assertNotNull(header.getModified());

            assertEquals(attachmentType,header.getContentType());
            assertEquals(length,header.getLength());
            assertEquals(creationTime,header.getModified());
            entityManager.flush();
            entityManager.clear();
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

