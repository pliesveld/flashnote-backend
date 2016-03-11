package com.pliesveld.flashnote.web.validator;

import com.pliesveld.flashnote.domain.AttachmentBinary;
import com.pliesveld.flashnote.domain.AttachmentType;
import com.pliesveld.flashnote.exception.AudioFormatNotSupportedException;
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
public class AttachmentValidator implements ConstraintValidator<ValidAttachment, AttachmentBinary> {
    private static final Logger LOG = LogManager.getLogger();

    @Override
    public void initialize(ValidAttachment constraintAnnotation) {
        LOG.debug("Initializing Constraint annotation with message = {}", constraintAnnotation.message());

        /*
            Used to retrieve annotation messages and store here for further validation
         */
    }

    @Override
    public boolean isValid(AttachmentBinary attachment, ConstraintValidatorContext context) {
        AttachmentType attachmentType = attachment.getAttachmentType();
        boolean is_valid = true;

        if(attachmentType == null)
        {
            is_valid = false;
        } else {

            if(attachment.getContents() == null)
            {
                is_valid = true;
            } else {

                switch(attachmentType) {
                    case AUDIO:
                        is_valid = is_valid && validateAudioAttachment(attachment,context);
                        break;
                    case TEXT:
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



    private boolean validateImageAttachment(AttachmentBinary attachment, ConstraintValidatorContext errors) {
        byte[] content = attachment.getContents();
        String fileName = attachment.getFileName();
        String mime = attachment.getMimeContentType();

        try {
            ImageMetadataReader.readImageMetadata(fileName, content, mime);

        } catch (IOException e) {

            return false;
        }
        return true;
    }

    private boolean validateDocumentAttachment(AttachmentBinary attachment, ConstraintValidatorContext errors) {
        if(!isValidUTF8(attachment.getContents()))
        {
            //errors.buildConstraintViolationWithTemplate("{com.pliesveld.flashnote.web.validator.ValidAttachment.message}").addConstraintViolation();
            return false;
        }
        return true;
    }

    private boolean validateAudioAttachment(AttachmentBinary attachment, ConstraintValidatorContext errors) {
        byte[] content = attachment.getContents();
        String fileName = attachment.getFileName();
        String mime = attachment.getMimeContentType();

        AudioMetadata metadata = null;
        //errors.buildConstraintViolationWithTemplate("{com.pliesveld.flashnote.web.validator.ValidAttachment.message}").addConstraintViolation();
        try {
            metadata = AudioMetadataReader.readAudioMetadata(fileName,content,mime);
        } catch (AudioFormatNotSupportedException e) {
            errors.buildConstraintViolationWithTemplate("{validator.ValidAttachment.audio.message}").addConstraintViolation();
            return false;
        }

        // TODO: verify mime content against audioformat
        metadata.getMimeContentType();

        metadata.getAudioFileFormat(); // one of these is null
        metadata.getAudioFormat();
        return true;
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
