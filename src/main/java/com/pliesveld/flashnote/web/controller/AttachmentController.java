package com.pliesveld.flashnote.web.controller;


import com.pliesveld.flashnote.domain.AbstractAttachment;
import com.pliesveld.flashnote.domain.AttachmentBinary;
import com.pliesveld.flashnote.domain.AttachmentText;
import com.pliesveld.flashnote.domain.AttachmentType;
import com.pliesveld.flashnote.domain.dto.AttachmentHeader;
import com.pliesveld.flashnote.exception.AttachmentUploadException;
import com.pliesveld.flashnote.service.AttachmentService;
import com.pliesveld.flashnote.service.CardService;
import com.pliesveld.flashnote.service.StudentService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.security.Principal;

@RestController
@RequestMapping("/attachments")
@MultipartConfig(location = "/tmp/upload",
        fileSizeThreshold = 1024*1024*5*5, // The file size in bytes after which the file will be temporarily stored on disk
        maxFileSize = 1024*1024*5*5,     // Maximum file size of each file
        maxRequestSize = 1024*1024*5*5 // Total files must be below this request size
)
public class AttachmentController  {
    private static final Logger LOG = LogManager.getLogger();

    @Autowired
    private StudentService studentService;

    @Autowired
    private CardService cardService;

    @Autowired
    private AttachmentService attachmentService;

    @RequestMapping(value="", method = RequestMethod.POST)
    public ResponseEntity<?> handleFileupload(
            @RequestParam("file") MultipartFile file,
            HttpServletRequest request) throws AttachmentUploadException
    {
        Principal principal = request.getUserPrincipal();

        final String DEFAULT_FILENAME = "unamed.file";
        String fileName = StringUtils.hasText(file.getName()) ? file.getName() :
                        StringUtils.hasText(file.getOriginalFilename()) ? file.getOriginalFilename() :
                                DEFAULT_FILENAME;

        LOG.info("Uploading attachment from: " + request.getRemoteAddr() + " filename: " + fileName + " size: " + file.getSize() );

        String fileContentType = file.getContentType();
        AttachmentType attachmentType = AttachmentType.valueOfMime(fileContentType);

        if(attachmentType == null)
        {
            throw new AttachmentUploadException("Unknown content-type");
        }

        if(!attachmentType.supportsFilenameBySuffix(fileName))
        {
            throw new AttachmentUploadException("Invalid file extension");
        }

        byte[] contents = null;

        try {
            contents = file.getBytes();
        } catch (IOException e) {
            throw new AttachmentUploadException("Could not get upload contents.",e);
        }

        int id = 0;

        if(attachmentType.isBinary()) {
            AttachmentBinary attachment = new AttachmentBinary();
            attachment.setContents(contents);
            attachment.setContentType(attachmentType);
            attachment.setFileName(fileName);
            id = attachmentService.storeAttachment(attachment).getId();
        } else {
            AttachmentText attachment = new AttachmentText();
            try {
                attachment.setContents(new String(contents,"UTF-8"));
            } catch (UnsupportedEncodingException e) {
                throw new AttachmentUploadException("Could not convert uploaded contents to String",e);
            }
            attachment.setContentType(attachmentType);
            attachment.setFileName(fileName);
            id = attachmentService.storeAttachment(attachment).getId();
        }

        HttpHeaders responseHeaders = new HttpHeaders();

        URI newStudentUri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();

        responseHeaders.setLocation(newStudentUri);
        return new ResponseEntity<>(null,responseHeaders, HttpStatus.OK);
    }

    @RequestMapping(value="/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> downloadAttachment(@PathVariable("id") int id)
    {
        MediaType content_type = null;
        Object response_data = null;
        long last_modified = 0;

        AbstractAttachment attachment = attachmentService.findAttachmentById(id);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(attachment.getAttachmentType().getMediatype());
        responseHeaders.setDate(attachment.getModifiedOn().toEpochMilli());

        if(attachment.getAttachmentType().isBinary())
        {
            AttachmentBinary binary = (AttachmentBinary) attachment;
            return new ResponseEntity<>(binary.getContents(),responseHeaders,HttpStatus.OK);
        } else {
            AttachmentText text = (AttachmentText) attachment;
            return new ResponseEntity<>(text.getContents(),responseHeaders,HttpStatus.OK);
        }
    }

    @RequestMapping(value="/{id}", method = RequestMethod.HEAD)
    public ResponseEntity<?> modifiedSinceAttachment(@PathVariable("id") int id)
    {
        AttachmentHeader header = attachmentService.findAttachmentHeaderById(id);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(header.getContentType().getMediatype());
        responseHeaders.setContentLength(header.getLength());
        responseHeaders.setDate(header.getModified().toEpochMilli());
        return new ResponseEntity<>(null,responseHeaders,HttpStatus.OK);
    }

    @RequestMapping(value="/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteAttachment(@PathVariable("id") int id)
    {
        attachmentService.delete(id);
    }

    @RequestMapping(value="/test", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public void testValidation()
    {
        Integer ret = attachmentService.testValidation(null);
        LOG.debug("returned " + ret);
    }

}
