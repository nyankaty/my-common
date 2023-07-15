package com.company.common.model.validator.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import com.company.common.model.validator.annotation.SingleValueNotNull;
import org.springframework.beans.BeanWrapperImpl;

public class SingleValueNotNullValidator implements ConstraintValidator<SingleValueNotNull, Object> {
    String[] fields;

    public SingleValueNotNullValidator() {
        // no arg constructor
    }

    public void initialize(final SingleValueNotNull combinedNotNull) {
        this.fields = combinedNotNull.value();
    }

    public boolean isValid(Object value, ConstraintValidatorContext context) {
        BeanWrapperImpl beanWrapper = new BeanWrapperImpl(value);
        String[] var4 = this.fields;
        int var5 = var4.length;

        for (int var6 = 0; var6 < var5; ++var6) {
            String f = var4[var6];
            Object fieldValue = beanWrapper.getPropertyValue(f);
            if (fieldValue != null) {
                return true;
            }
        }

        return false;
    }
}
