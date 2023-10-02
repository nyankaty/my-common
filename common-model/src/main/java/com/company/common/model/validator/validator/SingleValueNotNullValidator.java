package com.company.common.model.validator.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import com.company.common.model.validator.annotation.SingleValueNotNull;
import org.springframework.beans.BeanWrapperImpl;

public class SingleValueNotNullValidator implements ConstraintValidator<SingleValueNotNull, Object> {

    String[] fields;

    public void initialize(final SingleValueNotNull combinedNotNull) {
        this.fields = combinedNotNull.value();
    }

    public boolean isValid(Object value, ConstraintValidatorContext context) {
        BeanWrapperImpl beanWrapper = new BeanWrapperImpl(value);
        String[] fields = this.fields;

        for (String f : fields) {
            Object fieldValue = beanWrapper.getPropertyValue(f);
            if (fieldValue != null) {
                return true;
            }
        }

        return false;
    }
}
