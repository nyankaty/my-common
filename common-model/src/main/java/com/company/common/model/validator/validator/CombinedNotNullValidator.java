package com.company.common.model.validator.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.company.common.model.validator.annotation.CombinedNotNull;
import org.springframework.beans.BeanWrapperImpl;

public class CombinedNotNullValidator implements ConstraintValidator<CombinedNotNull, Object> {
    String[] fields;

    public CombinedNotNullValidator() {
        // no arg constructor
    }

    public void initialize(final CombinedNotNull combinedNotNull) {
        this.fields = combinedNotNull.value();
    }

    public boolean isValid(Object value, ConstraintValidatorContext context) {
        BeanWrapperImpl beanWrapper = new BeanWrapperImpl(value);
        String[] var4 = this.fields;
        int var5 = var4.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            String f = var4[var6];
            Object fieldValue = beanWrapper.getPropertyValue(f);
            if (fieldValue == null) {
                return false;
            }
        }

        return true;
    }
}
