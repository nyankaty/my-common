package com.company.common.spring.exception;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Map.Entry;

import com.company.common.spring.constant.ResponseStatusCode;
import com.company.common.spring.constant.ResponseStatusCodeEnum;
import com.company.common.spring.factory.response.Response;
import com.company.common.spring.factory.response.ResponseFactory;
import com.company.common.spring.factory.response.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@SuppressWarnings("java:S1452")
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    private final Map<String, ResponseStatusCode> handleHttpMessageNotReadableListError = new HashMap<>();
    
    @Autowired
    ResponseFactory responseFactory;

    public GlobalExceptionHandler() {
        this.handleHttpMessageNotReadableListError.put("JSON parse error", ResponseStatusCodeEnum.ERROR_BODY_CLIENT);
        this.handleHttpMessageNotReadableListError.put("Required request body is missing", ResponseStatusCodeEnum.ERROR_BODY_REQUIRED);
    }

    @ExceptionHandler({BusinessException.class})
    public final ResponseEntity<Object> handleValidationExceptions(RuntimeException ex) {
        log.error("Exception handleValidationExceptions: {}", ex.getMessage(), ex);
        return this.createResponse(ResponseStatusCodeEnum.BUSINESS_ERROR);
    }

    @ExceptionHandler({BaseResponseException.class})
    public ResponseEntity<?> handleValidationExceptions(BaseResponseException ex) {
        try {
            if (Objects.isNull(ex.getParams())) {
                return this.responseFactory.fail(ex.getDataResponse(), ex.getResponseStatusCode());
            } else {
                return Objects.isNull(ex.getDataResponse()) ? this.responseFactory.fail(new Response<>(), ex.getResponseStatusCode(), ex.getParams()) : this.responseFactory.fail(ex.getDataResponse(), ex.getResponseStatusCode(), ex.getParams());
            }
        } catch (Exception var3) {
            return this.responseFactory.fail(ResponseStatusCodeEnum.INTERNAL_GENERAL_SERVER_ERROR);
        }
    }

    @ExceptionHandler({Exception.class, RuntimeException.class})
    public final ResponseEntity<Object> handleAllExceptions(Exception ex) {
        log.error("Exception: ", ex);
        return this.createResponse(ResponseStatusCodeEnum.INTERNAL_GENERAL_SERVER_ERROR, ex.getStackTrace());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error("Exception handleMethodArgumentNotValid: {}", ex.getMessage(), ex);
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> errors.put(error.getObjectName(), error.getDefaultMessage()));
        return this.createResponse(ResponseStatusCodeEnum.VALIDATION_ERROR, errors);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        if (Objects.nonNull(ex.getMessage())) {
            log.error("Exception handleHttpMessageNotReadable: {}", ex.getMessage(), ex);
            Optional<ResponseStatusCode> code = this.handleHttpMessageNotReadableListError.entrySet()
                    .stream()
                    .filter(entry -> ex.getMessage().contains(entry.getKey()))
                    .map(Entry::getValue)
                    .findFirst();
            if (code.isPresent()) {
                return this.createResponse(code.get());
            }
        }

        return this.handleExceptionInternal(ex, null, headers, status, request);
    }

    private ResponseEntity<Object> createResponse(ResponseStatusCode code) {
        Status status = new Status(code.getCode(), true);
        status.setResponseTime(new Date());
        Response<Object> response = new Response<>();
        response.setStatus(status);
        return new ResponseEntity<>(response, HttpStatus.valueOf(code.getHttpCode()));
    }

    private ResponseEntity<Object> createResponse(ResponseStatusCode code, Object errors) {
        Status status = new Status(code.getCode(), true);
        status.setResponseTime(new Date());
        Response<Object> response = new Response<>();
        response.setStatus(status);
        response.setData(errors);
        return new ResponseEntity<>(response, HttpStatus.valueOf(code.getHttpCode()));
    }
}

