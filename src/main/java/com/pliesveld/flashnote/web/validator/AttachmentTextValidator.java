package com.pliesveld.flashnote.web.validator;

import com.pliesveld.flashnote.domain.AttachmentText;
import com.pliesveld.flashnote.domain.AttachmentType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class AttachmentTextValidator implements ConstraintValidator<ValidAttachment, AttachmentText> {
    private static final Logger LOG = LogManager.getLogger();

    @Override
    public void initialize(ValidAttachment constraintAnnotation) {
        LOG.debug("Initializing Constraint annotation with message = {}", constraintAnnotation.message());

        /*
            Used to retrieve annotation messages and store here for further validation
         */
    }

    @Override public boolean isValid(AttachmentText attachment, ConstraintValidatorContext context) {
        AttachmentType attachmentType = attachment.getAttachmentType();
        boolean is_valid = true;

        if (attachmentType == null) {
            is_valid = false;
        } else {

            if (attachment.getContents() == null) {
                is_valid = true;
            } else {

                switch (attachmentType.getCategory()) {

                    case DOCUMENT:
                        is_valid = is_valid && validateDocumentAttachment(attachment, context);
                        break;
                    case UNSUPPORTED:
                    default:
                        is_valid = false;
                        break;
                }


            }

        }

        if (!is_valid) {
            context.buildConstraintViolationWithTemplate("{com.pliesveld.flashnote.web.validator.ValidAttachment.message}").addConstraintViolation();
        }

        return is_valid;
    }

    private boolean validateDocumentAttachment(AttachmentText attachment, ConstraintValidatorContext errors) {
        return true;
    }

}
