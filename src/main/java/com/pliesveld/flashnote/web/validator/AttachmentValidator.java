package com.pliesveld.flashnote.web.validator;

import com.pliesveld.flashnote.domain.AttachmentBinary;
import com.pliesveld.flashnote.domain.AttachmentType;
import com.pliesveld.flashnote.exception.AudioFormatNotSupportedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
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

    final ValidationImageSettings validationImageSettings;

    @Autowired
    public AttachmentValidator(ValidationImageSettings validationImageSettings) {
        this.validationImageSettings = validationImageSettings;
    }

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

        if (attachmentType == null) {
            is_valid = false;
        } else {

            if (attachment.getContents() == null) {
                is_valid = true;
            } else {

                switch (attachmentType) {
                    case AUDIO:
                        is_valid = is_valid && validateAudioAttachment(attachment, context);
                        break;
                    case TEXT:
                        is_valid = is_valid && validateDocumentAttachment(attachment, context);
                        break;
                    case IMAGE:
                        is_valid = is_valid && validateImageAttachment(attachment, context);
                        break;
                }


            }

        }

        if (!is_valid) {
            context.buildConstraintViolationWithTemplate("{com.pliesveld.flashnote.web.validator.ValidAttachment.message}").addConstraintViolation();
        }

        return is_valid;
    }


    @Component
    @ConfigurationProperties(prefix = "validation.image")
    public static class ValidationImageSettings {
        private int maxHeight;
        private int maxWidth;
        private int minAspectRatio;
        private int maxAspectRatio;

        public int getMaxHeight() {
            return maxHeight;
        }

        public void setMaxHeight(int maxHeight) {
            this.maxHeight = maxHeight;
        }

        public int getMaxWidth() {
            return maxWidth;
        }

        public void setMaxWidth(int maxWidth) {
            this.maxWidth = maxWidth;
        }

        public int getMinAspectRatio() {
            return minAspectRatio;
        }

        public void setMinAspectRatio(int minAspectRatio) {
            this.minAspectRatio = minAspectRatio;
        }

        public int getMaxAspectRatio() {
            return maxAspectRatio;
        }

        public void setMaxAspectRatio(int maxAspectRatio) {
            this.maxAspectRatio = maxAspectRatio;
        }
    }

    private boolean validateImageAttachment(AttachmentBinary attachment, ConstraintValidatorContext errors) {
        byte[] content = attachment.getContents();
        String fileName = attachment.getFileName();
        String mime = attachment.getMimeContentType();

        try {
            ImageMetadata metadata = ImageMetadataReader.readImageMetadata(fileName, content, mime);

            if (metadata == null) {
                errors.buildConstraintViolationWithTemplate("{com.pliesveld.flashnote.web.validator.ValidAttachment.image.message}").addConstraintViolation();
                return false;
            } else {
                if (metadata.getAspectRatio() > validationImageSettings.getMaxAspectRatio() || metadata.getAspectRatio() < validationImageSettings.getMinAspectRatio()) {
                    errors.buildConstraintViolationWithTemplate("{com.pliesveld.flashnote.web.validator.ValidAttachment.image.aspect.message}").addConstraintViolation();
                    return false;
                }
                if (metadata.getHeight() > validationImageSettings.getMaxHeight()) {
                    errors.buildConstraintViolationWithTemplate("{com.pliesveld.flashnote.web.validator.ValidAttachment.image.dimensions.message}").addConstraintViolation();
                    return false;
                }
                if (metadata.getWidth() > validationImageSettings.getMaxWidth()) {
                    errors.buildConstraintViolationWithTemplate("{com.pliesveld.flashnote.web.validator.ValidAttachment.image.dimensions.message}").addConstraintViolation();
                    return false;
                }
            }

        } catch (IOException e) {
            errors.buildConstraintViolationWithTemplate("{com.pliesveld.flashnote.web.validator.ValidAttachment.image.message}").addConstraintViolation();
            return false;
        }
        return true;
    }

    private boolean validateDocumentAttachment(AttachmentBinary attachment, ConstraintValidatorContext errors) {
        if (!isValidUTF8(attachment.getContents())) {
            errors.buildConstraintViolationWithTemplate("{com.pliesveld.flashnote.web.validator.ValidAttachment.document.message}").addConstraintViolation();
            return false;
        }
        return true;
    }

    private boolean validateAudioAttachment(AttachmentBinary attachment, ConstraintValidatorContext errors) {
        byte[] content = attachment.getContents();
        String fileName = attachment.getFileName();
        String mime = attachment.getMimeContentType();

        AudioMetadata metadata = null;

        try {
            metadata = AudioMetadataReader.readAudioMetadata(fileName, content, mime);
        } catch (AudioFormatNotSupportedException e) {
            errors.buildConstraintViolationWithTemplate("{com.pliesveld.flashnote.web.validator.ValidAttachment.audio.message}").addConstraintViolation();

            return false;
        }

        // TODO: verify mime content against audioformat
        metadata.getMimeContentType();

        metadata.getAudioFileFormat(); // one of these is null
        metadata.getAudioFormat();
        return true;
    }

    /**
     * Used to verify attachment of text content types do not
     * contain binary data.
     */
    public static boolean isValidUTF8(byte[] input) {

        CharsetDecoder cs = Charset.forName("UTF-8").newDecoder();

        try {
            cs.decode(ByteBuffer.wrap(input));
            return true;
        } catch (CharacterCodingException e) {
            return false;
        }
    }
}
