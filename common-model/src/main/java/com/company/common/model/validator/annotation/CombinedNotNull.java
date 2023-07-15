package com.company.common.model.validator.annotation;

import com.company.common.model.validator.validator.CombinedNotNullValidator;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Constraint(
        validatedBy = {CombinedNotNullValidator.class}
)
public @interface CombinedNotNull {
    String message() default "file1 and file2... is required";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String[] value() default {};
}
