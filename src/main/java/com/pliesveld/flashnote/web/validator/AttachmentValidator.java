package com.pliesveld.flashnote.web.validator;

import com.pliesveld.flashnote.domain.Attachment;
import com.pliesveld.flashnote.domain.AttachmentType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

@Component
public class AttachmentValidator implements ConstraintValidator<ValidAttachment, Attachment> {
    private static final Logger LOG = LogManager.getLogger();

    @Override
    public void initialize(ValidAttachment constraintAnnotation) {
        LOG.debug("Initializing Constraint annotation with message = {}", constraintAnnotation.message());

        /*
            Used to retrieve annotation messages and store here for further validation
         */
    }

    @Override
    public boolean isValid(Attachment attachment, ConstraintValidatorContext context) {
        AttachmentType attachmentType = attachment.getAttachmentType();
        boolean is_valid = true;

        if(attachmentType == null)
        {
            is_valid = false;
        } else {

            if(attachment.getFileData() == null)
            {
                is_valid = true;
            } else {

                switch(attachmentType) {
                    case AUDIO:
                        is_valid = is_valid && validateAudioAttachment(attachment,context);
                        break;
                    case DOC:
                        is_valid = is_valid && validateDocumentAttachment(attachment,context);
                        break;
                    case IMAGE:
                        is_valid = is_valid && validateImageAttachment(attachment,context);
                        break;
                }


            }

        }
 
        if(!is_valid)
        {
            context.buildConstraintViolationWithTemplate("{com.pliesveld.flashnote.web.validator.ValidAttachment.message}").addConstraintViolation();
        }

        return is_valid;
    }



    private boolean validateImageAttachment(Attachment attachment, ConstraintValidatorContext errors) {
        byte[] content = attachment.getFileData();
        String fileName = attachment.getFileName();
        String mime = attachment.getMimeType();

        try {
            ImageMetadataReader.readImageMetaData(fileName,content,mime);

        } catch (IOException e) {

            return false;
        }
        return true;
    }

    private boolean validateDocumentAttachment(Attachment attachment, ConstraintValidatorContext errors) {
        if(!isValidUTF8(attachment.getFileData()))
        {
            //errors.buildConstraintViolationWithTemplate("{com.pliesveld.flashnote.web.validator.ValidAttachment.message}").addConstraintViolation();
            return false;
        }
        return true;
    }

    private boolean validateAudioAttachment(Attachment attachment, ConstraintValidatorContext errors) {
        //errors.buildConstraintViolationWithTemplate("{com.pliesveld.flashnote.web.validator.ValidAttachment.message}").addConstraintViolation();
        return false;
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
