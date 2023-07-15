package com.company.common.spring.controller.restful;

import java.util.Date;
import java.util.List;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import com.company.common.spring.config.properties.AppConfigurationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BaseController {
    @Autowired
    AppConfigurationProperties appConfigurationProperties;

    public BaseController() {
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public <T> GeneralResponse<T> handleValidationExceptions(MethodArgumentNotValidException ex) {
        GeneralResponse<T> errors = new GeneralResponse();
        vn.com.viettel.vds.starter.factory.response.ResponseStatus status = new vn.com.viettel.vds.starter.factory.response.ResponseStatus();
        status.setCode(ResponseStatusCodeEnum.VALIDATION_ERROR.getCode());
        List<ObjectError> objectError = ex.getBindingResult().getAllErrors();
        if (!objectError.isEmpty()) {
            status.setMessage(((ObjectError)objectError.get(0)).getDefaultMessage());
        }

        status.setResponseTime(new Date());
        errors.setStatus(status);
        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ServletRequestBindingException.class})
    public <T> GeneralResponse<T> handleValidationExceptions(ServletRequestBindingException ex) {
        GeneralResponse<T> errors = new GeneralResponse();
        vn.com.viettel.vds.starter.factory.response.ResponseStatus status = new vn.com.viettel.vds.starter.factory.response.ResponseStatus();
        status.setCode(ResponseStatusCodeEnum.VALIDATION_ERROR.getCode());
        status.setMessage(ex.getMessage());
        errors.setStatus(status);
        status.setResponseTime(new Date());
        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ConstraintViolationException.class})
    public <T> GeneralResponse<T> handleValidationException1s(ConstraintViolationException ex) {
        GeneralResponse<T> errors = new GeneralResponse();
        vn.com.viettel.vds.starter.factory.response.ResponseStatus status = new vn.com.viettel.vds.starter.factory.response.ResponseStatus();
        status.setResponseTime(new Date());
        status.setCode(ResponseStatusCodeEnum.VALIDATION_ERROR.getCode());
        ConstraintViolation<?> constraintViolation = (ConstraintViolation)ex.getConstraintViolations().stream().findFirst().orElse((Object)null);
        if (constraintViolation == null) {
            status.setMessage(Translator.toLocale(ResponseStatusCodeEnum.VALIDATION_ERROR.getCode()));
        } else {
            status.setMessage(constraintViolation.getMessage());
        }

        errors.setStatus(status);
        return errors;
    }
}

