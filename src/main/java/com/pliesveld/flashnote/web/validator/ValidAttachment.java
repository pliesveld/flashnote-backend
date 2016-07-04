package com.pliesveld.flashnote.web.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.NotNull;
import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = AttachmentValidator.class)
@NotNull
@ReportAsSingleViolation
public @interface ValidAttachment {
    String message() default "{com.pliesveld.flashnote.web.validator.ValidAttachment.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
