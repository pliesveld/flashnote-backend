package com.pliesveld.flashnote.web.controller;


import com.pliesveld.flashnote.domain.Attachment;
import com.pliesveld.flashnote.domain.AttachmentHeader;
import com.pliesveld.flashnote.exception.AttachmentUploadException;
import com.pliesveld.flashnote.service.AttachmentService;
import com.pliesveld.flashnote.service.CardService;
import com.pliesveld.flashnote.service.StudentService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.security.Principal;

@RestController
@RequestMapping("/attachments")
@MultipartConfig(location = "/tmp/upload",
        fileSizeThreshold = 1024*1024, // The file size in bytes after which the file will be temporarily stored on disk
        maxFileSize = 1024*1024*5,     // Maximum file size of each file
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

        Attachment attachment = attachmentService.storeAttachment(fileName, file);

        HttpHeaders responseHeaders = new HttpHeaders();

        URI newStudentUri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(attachment.getId())
                .toUri();

        responseHeaders.setLocation(newStudentUri);
        return new ResponseEntity<>(null,responseHeaders, HttpStatus.CREATED);
    }

    @RequestMapping(value="/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> downloadAttachment(@PathVariable("id") int id)
    {
        Attachment attachment = attachmentService.findAttachmentById(id);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(attachment.getContentType().getMediatype());
        responseHeaders.setDate(attachment.getModifiedOn().toEpochMilli());
        return new ResponseEntity<>(attachment.getFileData(),responseHeaders,HttpStatus.OK);
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

    public AttachmentService getAttachmentService() {
        return attachmentService;
    }
    public void setAttachmentService(AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }
}
