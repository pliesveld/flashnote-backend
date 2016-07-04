package com.pliesveld.flashnote.web.controller;


import com.pliesveld.flashnote.domain.AbstractAttachment;
import com.pliesveld.flashnote.domain.AttachmentBinary;
import com.pliesveld.flashnote.domain.AttachmentText;
import com.pliesveld.flashnote.model.json.response.AttachmentHeader;
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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/attachments")
public class AttachmentController {
    private static final Logger LOG = LogManager.getLogger();

    @Autowired
    private StudentService studentService;

    @Autowired
    private CardService cardService;

    @Autowired
    private AttachmentService attachmentService;

    @RequestMapping(value = "/{id}", method = RequestMethod.HEAD)
    public ResponseEntity<?> modifiedSinceAttachment(@PathVariable("id") int id) {
        AttachmentHeader header = attachmentService.findAttachmentHeaderById(id);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(header.getContentType().getMediatype());
        responseHeaders.setContentLength(header.getLength());
        responseHeaders.setDate(header.getModified().toEpochMilli());
        return new ResponseEntity<>(null, responseHeaders, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> downloadAttachment(@PathVariable("id") int id) {
        MediaType content_type = null;
        Object response_data = null;
        long last_modified = 0;

        AbstractAttachment attachment = attachmentService.findAttachmentById(id);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(attachment.getAttachmentType().getMediatype());
        responseHeaders.setDate(attachment.getModifiedOn().toEpochMilli());

        if (attachment.getAttachmentType().isBinary()) {
            AttachmentBinary binary = (AttachmentBinary) attachment;
            return new ResponseEntity<>(binary.getContents(), responseHeaders, HttpStatus.OK);
        } else {
            AttachmentText text = (AttachmentText) attachment;
            return new ResponseEntity<>(text.getContents(), responseHeaders, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteAttachment(@PathVariable("id") int id) {
        attachmentService.removeAttachmentById(id);
    }
}
