package com.company.common.spring.factory.response;

import com.company.common.spring.config.MessageResponseConfig;
import com.company.common.spring.constant.ResponseStatusCode;
import com.company.common.spring.constant.ResponseStatusCodeEnum;
import com.company.common.spring.service.ErrorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;

@Component
@SuppressWarnings("java:S3740")
public class ResponseFactory {

    private static final Logger log = LoggerFactory.getLogger(ResponseFactory.class);

    @Autowired(required = false)
    private ErrorService errorService;

    @Autowired
    private MessageResponseConfig messageResponseConfig;

    public ResponseFactory() {
        // no arg constructor
    }

    public <T> ResponseEntity<Response<T>> success(T data) {
        Response<T> responseObject = new Response<>();
        responseObject.setData(data);
        return this.success(responseObject);
    }

    public <T> ResponseEntity<Response<T>> success(Response<T> responseObject) {
        Status status = this.parseResponseStatus(ResponseStatusCodeEnum.SUCCESS.getCode(), null);
        responseObject.setStatus(status);
        return ResponseEntity.ok().body(responseObject);
    }

    public <T> ResponseEntity<Response<T>> successWithHeader(MultiValueMap<String, String> header, T data) {
        Response<T> responseObject = new Response<>();
        responseObject.setData(data);
        Status status = this.parseResponseStatus(ResponseStatusCodeEnum.SUCCESS.getCode(), null);
        responseObject.setStatus(status);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.addAll(header);
        return (ResponseEntity.ok().headers(responseHeaders)).body(responseObject);
    }

    public <T> ResponseEntity<Response<T>> fail(T data, ResponseStatusCode code) {
        Response<T> responseObject = new Response<>();
        responseObject.setData(data);
        return this.fail(responseObject, code, null);
    }

    public <T> ResponseEntity<Response<T>> fail(ResponseStatusCode code) {
        Response<T> responseObject = new Response<>();
        return this.fail(responseObject, code, null);
    }

    public <T> ResponseEntity<Response<T>> fail(Response<T> responseObject, ResponseStatusCode code) {
        if (Objects.isNull(responseObject)) {
            responseObject = new Response<>();
        }

        return this.fail(responseObject, code, null);
    }

    public <T> ResponseEntity<Response<T>> fail(Response<T> responseObject, ResponseStatusCode code, Map<String, String> params) {
        Status status = this.parseResponseStatus(code.getCode(), params);
        if (Objects.isNull(responseObject)) {
            responseObject = new Response<>();
        }

        responseObject.setStatus(status);
        return ResponseEntity.status(code.getHttpCode()).body(responseObject);
    }

    public <T> void httpServletResponseToClient(HttpServletResponse httpServletResponse, T data, ResponseStatusCode statusCode) throws IOException {
        this.httpServletResponseToClient(httpServletResponse, data, statusCode, null);
    }

    public <T> void httpServletResponseToClient(HttpServletResponse httpServletResponse, T data, ResponseStatusCode statusCode, Map<String, String> params) throws IOException {
        Response<T> response = new Response<>();
        response.setData(data);
        Status status = this.parseResponseStatus(statusCode.getCode(), params);
        response.setStatus(status);
        this.writeToHttpServletResponse(httpServletResponse, response, statusCode);
    }

    public void writeToHttpServletResponse(HttpServletResponse httpServletResponse, Object response, ResponseStatusCode statusCode) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String responseString = mapper.writeValueAsString(response);
        httpServletResponse.setCharacterEncoding(StandardCharsets.UTF_8.name());
        httpServletResponse.setStatus(statusCode.getHttpCode());
        httpServletResponse.setHeader("Content-Type", "application/json");
        httpServletResponse.getWriter().write(responseString);
        httpServletResponse.getWriter().flush();
        httpServletResponse.getWriter().close();
    }

    private String replaceParams(String message, Map<String, String> params) {
        Iterator var3;
        Entry param;
        if (!CollectionUtils.isEmpty(params)) {
            for(var3 = params.entrySet().iterator(); var3.hasNext(); message = message.replaceAll("%%" + param.getKey() + "%%", (String) param.getValue())) {
                param = (Entry)var3.next();
            }
        }

        if (!CollectionUtils.isEmpty(this.messageResponseConfig.getParams())) {
            for(var3 = this.messageResponseConfig.getParams().entrySet().iterator(); var3.hasNext(); message = message.replaceAll("%%" + param.getKey() + "%%", (String) param.getValue())) {
                param = (Entry) var3.next();
            }
        }

        return message;
    }

    private Status parseResponseStatus(String code, Map<String, String> params) {
        Status status = new Status(code, true);
        status.setMessage(this.replaceParams(status.getMessage(), params));
        String errorDetail = null;
        if (Objects.nonNull(this.errorService)) {
            errorDetail = this.errorService.getErrorDetail(code, LocaleContextHolder.getLocale().getLanguage());
        }

        if (Objects.nonNull(errorDetail)) {
            status.setDisplayMessage(this.replaceParams(errorDetail, params));
        } else {
            status.setDisplayMessage(status.getMessage());
        }

        log.debug(status.toString());
        status.setResponseTime(new Date());
        return status;
    }
}

