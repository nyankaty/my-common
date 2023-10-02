package com.company.common.model.validator.validator;

import com.company.common.model.validator.annotation.ValueOfEnum;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValueOfEnumValidator implements ConstraintValidator<ValueOfEnum, CharSequence> {
    private List<String> acceptedValues;

    public void initialize(ValueOfEnum annotation) {
        this.acceptedValues = Stream.of(annotation.enumClass().getEnumConstants()).map(Enum::name).collect(Collectors.toList());
    }

    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        return value == null || this.acceptedValues.contains(value.toString());
    }
}
