package com.company.common.spring.exception;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

import com.company.common.spring.factory.response.FieldErrorResponse;
import com.company.common.spring.factory.response.ResponseFactory;
import com.company.common.util.enums.ResponseStatusEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({Exception.class, RuntimeException.class})
    public final ResponseEntity<Object> handleAllExceptions(Exception ex) {
        log.error("Exception: ", ex);
        return ResponseFactory.fail(ex, ResponseStatusEnum.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({ResponseException.class})
    public ResponseEntity<Object> handleResponseException(ResponseException ex) {
        try {
            if (Objects.isNull(ex.getErrorDetail())) {
                return ResponseFactory.fail(ex.getStatus());
            } else {
                return ResponseFactory.fail(ex.getErrorDetail(), ex.getStatus());
            }
        } catch (Exception var3) {
            return ResponseFactory.fail(ResponseStatusEnum.INTERNAL_SERVER_ERROR);
        }
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex) {
        log.error("ConstraintViolation Exception: ", ex);
        List<String> errors = new ArrayList<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.add(violation.getRootBeanClass().getName() + " " + violation.getPropertyPath() + ": " + violation.getMessage());
        }

        return ResponseFactory.fail(errors, ResponseStatusEnum.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        log.error("MethodArgumentTypeMismatch Exception: ", ex);
        String error = ex.getName() + " should be of type in " + ex.getRequiredType();
        return ResponseFactory.fail(error, ResponseStatusEnum.BAD_REQUEST);
    }

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<Object> handleObjectOptimisticLockingFailureException(ObjectOptimisticLockingFailureException ex) {
        return ResponseFactory.fail(ex, ResponseStatusEnum.LOCKED);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        log.error("HttpMethodNotSupported Exception: ", ex);
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getMethod());
        builder.append(" method is not supported for this request. ");

        if (ex.getSupportedHttpMethods() != null) {
            builder.append("Supported methods are: ");
            ex.getSupportedHttpMethods().forEach(t -> builder.append(t).append(" "));
        }

        return ResponseFactory.fail(builder.toString(), ResponseStatusEnum.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex) {
        log.error("HttpMediaTypeNotSupported Exception: ", ex);
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getContentType());
        builder.append(" media type is not supported. Supported media types are ");
        ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(" "));

        return ResponseFactory.fail(builder.toString(), ResponseStatusEnum.UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        log.error("MethodArgumentNotValid Exception: ", ex);
        List<FieldErrorResponse> errors = getObjectNotValid(ex.getBindingResult());
        return ResponseFactory.fail(errors, ResponseStatusEnum.BAD_REQUEST);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValid(BindException ex) {
        log.error("BindException Exception: ", ex);
        List<FieldErrorResponse> errors = getObjectNotValid(ex.getBindingResult());
        return ResponseFactory.fail(errors, ResponseStatusEnum.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex) {
        log.error("MissingServletRequestParameter Exception: ", ex);
        String error = ex.getParameterName() + " parameter is missing";
        return ResponseFactory.fail(error, ResponseStatusEnum.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        log.error("HttpMessageNotReadableException Exception: ", ex);
        return ResponseFactory.fail(ex, ResponseStatusEnum.BAD_REQUEST);
    }

    private List<FieldErrorResponse> getObjectNotValid(BindingResult bindingResult) {
        return bindingResult.getAllErrors().stream().map(objectError -> {
                try {
                    FieldError fieldError = (FieldError) objectError;
                    return new FieldErrorResponse(fieldError.getObjectName(), fieldError.getField(), fieldError.getDefaultMessage());
                } catch (ClassCastException var1) {
                    return null;
                }})
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }
}

