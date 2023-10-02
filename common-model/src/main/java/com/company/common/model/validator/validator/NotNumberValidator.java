package com.company.common.model.validator.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.company.common.model.validator.annotation.NotNumber;
import org.springframework.util.StringUtils;

public class NotNumberValidator implements ConstraintValidator<NotNumber, String> {

    NotNumber notCorrectFormatDate;

    public void initialize(NotNumber constraintAnnotation) {
        this.notCorrectFormatDate = constraintAnnotation;
    }

    public boolean isValid(String value, ConstraintValidatorContext context) {
        return !StringUtils.hasLength(value) || value.matches("[0-9]+");
    }
}
