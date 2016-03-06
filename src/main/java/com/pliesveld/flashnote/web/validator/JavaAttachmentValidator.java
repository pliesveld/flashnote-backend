package com.pliesveld.flashnote.web.validator;

import com.pliesveld.flashnote.domain.AttachmentBinary;
import com.pliesveld.flashnote.domain.AttachmentType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

public class JavaAttachmentValidator implements Validator {
    private static final Logger LOG = LogManager.getLogger();

//    @Autowired
//    private Validator basicValidator;



    @Override
    public void validate(Object target, Errors errors) {
        LOG.debug("Validating " + target);
  //      basicValidator.validate(target,errors);

        if(errors.hasErrors())
        {
            LOG.debug("Validation of " + target + " had errors.");
            return;
        }

        AttachmentBinary attachment = (AttachmentBinary) target;
        AttachmentType attachmentType = attachment.getAttachmentType();

        if(!StringUtils.hasLength(attachment.getFileName()))
            errors.rejectValue("fileName","required","required");


        boolean check_content = true;

        if(attachment.getAttachmentType() == null)
        {
            errors.rejectValue("contentType","required","required");
            check_content = false;
        }

        if(attachment.getFileData() == null)
        {
            errors.rejectValue("fileData","required","required");
            check_content = false;
        }

        if(!check_content)
            return;


        switch(attachmentType) {
            case AUDIO:
                validateAudioAttachment(attachment,errors);
                break;
            case TEXT:
                validateDocumentAttachment(attachment,errors);
                break;
            case IMAGE:
                validateImageAttachment(attachment,errors);
                break;
        }

    }



    private void validateImageAttachment(AttachmentBinary attachment, Errors errors) {
        byte[] content = attachment.getFileData();
        String fileName = attachment.getFileName();
        String mime = attachment.getMimeType();

        try {
            ImageMetadataReader.readImageMetadata(fileName, content, mime);

        } catch (IOException e) {
            errors.rejectValue("fileData","invalid","invalid");
        }
    }

    private void validateDocumentAttachment(AttachmentBinary attachment, Errors errors) {
        if(!isValidUTF8(attachment.getFileData()))
        {
            errors.rejectValue("fileData","invalid","invalid");
        }
    }

    private void validateAudioAttachment(AttachmentBinary attachment, Errors errors) {

    }

    @Override
    public boolean supports(Class<?> clazz) {
        return AttachmentBinary.class.isAssignableFrom(clazz);
    }

    /**
        Used to verify attachment of text content types do not
        contain binary data.
     */
    public static boolean isValidUTF8( byte[] input ) {

        CharsetDecoder cs = Charset.forName("UTF-8").newDecoder();

        try {
            cs.decode(ByteBuffer.wrap(input));
            return true;
        }
        catch(CharacterCodingException e){
            return false;
        }
    }
}
