package com.company.common.model.validator.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.company.common.model.validator.annotation.CombinedNotNull;
import org.springframework.beans.BeanWrapperImpl;

public class CombinedNotNullValidator implements ConstraintValidator<CombinedNotNull, Object> {

    String[] fields;

    public void initialize(final CombinedNotNull combinedNotNull) {
        this.fields = combinedNotNull.value();
    }

    public boolean isValid(Object value, ConstraintValidatorContext context) {
        BeanWrapperImpl beanWrapper = new BeanWrapperImpl(value);
        String[] fields = this.fields;

        for (String f : fields) {
            Object fieldValue = beanWrapper.getPropertyValue(f);
            if (fieldValue == null) {
                return false;
            }
        }

        return true;
    }
}
