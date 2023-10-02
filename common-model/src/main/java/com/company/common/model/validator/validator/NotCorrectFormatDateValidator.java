package com.company.common.model.validator.validator;

import java.util.Date;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.company.common.model.validator.annotation.NotCorrectFormatDate;
import com.company.common.util.utils.DateUtil;
import org.apache.commons.lang3.StringUtils;

public class NotCorrectFormatDateValidator implements ConstraintValidator<NotCorrectFormatDate, String> {
    NotCorrectFormatDate notCorrectFormatDate;

    public void initialize(NotCorrectFormatDate constraintAnnotation) {
        this.notCorrectFormatDate = constraintAnnotation;
    }

    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        } else {
            if (!StringUtils.isEmpty(this.notCorrectFormatDate.value())) {
                Date date = DateUtil.formatDate(value, this.notCorrectFormatDate.value());
                if (date != null) {
                    return true;
                }
            }

            for (int i = 0; i < this.notCorrectFormatDate.values().length; ++i) {
                Date date = DateUtil.formatDate(value, this.notCorrectFormatDate.values()[i]);
                if (date != null) {
                    return true;
                }
            }

            return false;
        }
    }
}

