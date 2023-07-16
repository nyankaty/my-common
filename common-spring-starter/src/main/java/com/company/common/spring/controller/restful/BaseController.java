package com.company.common.spring.controller.restful;

import java.util.Date;
import java.util.List;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import com.company.common.spring.config.properties.AppConfigurationProperties;
import com.company.common.spring.constant.ResponseStatusCodeEnum;
import com.company.common.spring.factory.response.Response;
import com.company.common.spring.factory.response.Status;
import com.company.common.spring.locale.Translator;
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
        // no arg constructor
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public <T> Response<T> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Response<T> errors = new Response<>();
        Status status = new Status();

        List<ObjectError> objectError = ex.getBindingResult().getAllErrors();
        if (!objectError.isEmpty()) {
            status.setMessage(objectError.get(0).getDefaultMessage());
        }

        status.setCode(ResponseStatusCodeEnum.VALIDATION_ERROR.getCode());
        status.setResponseTime(new Date());

        errors.setStatus(status);
        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ServletRequestBindingException.class})
    public <T> Response<T> handleValidationExceptions(ServletRequestBindingException ex) {
        Response<T> errors = new Response<>();
        Status status = new Status();

        status.setCode(ResponseStatusCodeEnum.VALIDATION_ERROR.getCode());
        status.setMessage(ex.getMessage());
        status.setResponseTime(new Date());

        errors.setStatus(status);
        return errors;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ConstraintViolationException.class})
    public <T> Response<T> handleValidationException1s(ConstraintViolationException ex) {
        Response<T> errors = new Response<>();
        Status status = new Status();

        ConstraintViolation<?> constraintViolation = ex.getConstraintViolations().stream().findFirst().orElse(null);
        if (constraintViolation == null) {
            status.setMessage(Translator.toLocale(ResponseStatusCodeEnum.VALIDATION_ERROR.getCode()));
        } else {
            status.setMessage(constraintViolation.getMessage());
        }

        status.setResponseTime(new Date());
        status.setCode(ResponseStatusCodeEnum.VALIDATION_ERROR.getCode());

        errors.setStatus(status);
        return errors;
    }
}
