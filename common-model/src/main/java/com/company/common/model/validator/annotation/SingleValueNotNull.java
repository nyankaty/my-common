package com.company.common.model.validator.annotation;

import com.company.common.model.validator.validator.SingleValueNotNullValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Constraint(
        validatedBy = {SingleValueNotNullValidator.class}
)
public @interface SingleValueNotNull {
    String message() default "file1 or file2 is required";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String[] value() default {};
}
