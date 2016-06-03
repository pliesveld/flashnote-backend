package com.pliesveld.flashnote.web.controller;


import com.pliesveld.flashnote.domain.*;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.security.Principal;

@RestController
@RequestMapping("/attachment")
/*
@MultipartConfig(location = "/tmp/upload",
        fileSizeThreshold = 1024*1024*5*5, // The file size in bytes after which the file will be temporarily stored on disk
        maxFileSize = 1024*1024*5*5,     // Maximum file size of each file
        maxRequestSize = 1024*1024*5*5 // Total files must be below this request size

)*/
public class AttachmentUploadController {
    private static final Logger LOG = LogManager.getLogger();

    @Autowired
    private StudentService studentService;

    @Autowired
    private CardService cardService;

    @Autowired
    private AttachmentService attachmentService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value="/upload", method = RequestMethod.POST)
    public ResponseEntity<?> handleFileupload(
            @RequestParam("file") MultipartFile file,
            @RequestParam(name = "question_id", required = false) Integer question_id,
            HttpServletRequest request) throws AttachmentUploadException
    {
        Principal principal = request.getUserPrincipal();

        Question question = null;
        if(question_id != null) {
            question = cardService.findQuestionById(question_id);
        }

        LOG.info("Uploading attachment ip: {} size: {} user: {} file: {} orig: {}", request.getRemoteAddr(),file.getSize() ,principal,file.getName(),file.getOriginalFilename());

        String fileContentType = file.getContentType();
        AttachmentType attachmentType = AttachmentType.valueOfMime(fileContentType);

        if(attachmentType == null)
            throw new AttachmentUploadException("Unknown content-type: " + fileContentType);


        String fileName = file.getOriginalFilename();

        if(!StringUtils.hasText(fileName) || !attachmentType.supportsFilenameBySuffix(fileName))
        {
            throw new AttachmentUploadException("Invalid file extension. " + attachmentType + " does not support " + fileName);
        }


        byte[] contents = null;

        try {
            contents = file.getBytes();
        } catch (IOException e) {
            throw new AttachmentUploadException("Could not get upload contents.",e);
        }

        int id = 0;

        AbstractAttachment abstractAttachment = null;
        if(attachmentType.isBinary()) {
            AttachmentBinary attachment = new AttachmentBinary();
            attachment.setContents(contents);
            attachment.setAttachmentType(attachmentType);
            attachment.setFileName(fileName);
            abstractAttachment = attachment;
            id = attachmentService.storeAttachment(attachment).getId();
        } else {
            AttachmentText attachment = new AttachmentText();
            try {
                attachment.setContents(new String(contents,"UTF-8"));
            } catch (UnsupportedEncodingException e) {
                throw new AttachmentUploadException("Could not convert uploaded contents to String",e);
            }
            attachment.setAttachmentType(attachmentType);
            attachment.setFileName(fileName);
            abstractAttachment = attachment;
            id = attachmentService.storeAttachment(attachment).getId();
        }


        if(question != null )
        {
            question.setAttachment(abstractAttachment);
            cardService.update(question);
        }

        HttpHeaders responseHeaders = new HttpHeaders();

        URI newStudentUri = MvcUriComponentsBuilder
                .fromController(AttachmentController.class)
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();

        responseHeaders.setLocation(newStudentUri);
        return new ResponseEntity<>(null,responseHeaders, HttpStatus.OK);
    }

}
