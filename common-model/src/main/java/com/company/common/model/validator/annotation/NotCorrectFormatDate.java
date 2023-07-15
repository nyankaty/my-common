package com.company.common.model.validator.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

import com.company.common.model.validator.validator.NotCorrectFormatDateValidator;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = {NotCorrectFormatDateValidator.class}
)
public @interface NotCorrectFormatDate {
    String message() default "Không đúng định dạng format date";

    String value() default "";

    String[] values() default {};

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
