package com.company.common.spring.factory.response;

import com.company.common.util.enums.ResponseStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

public class ResponseFactory {

    public static <T> ResponseEntity<Response<T>> success(Response<T> response) {
        return ResponseEntity.ok().body(response);
    }

    public static <T> ResponseEntity<Response<T>> success(T data, ResponseStatus status) {
        return ResponseEntity.ok().body(Response.success(data, status));
    }

    public static <T> ResponseEntity<Response<T>> success(T data) {
        return ResponseEntity.ok().body(Response.of(data));
    }

    public static <T> ResponseEntity<Response<T>> success() {
        return ResponseEntity.ok().body(Response.ok());
    }

    public static <T> ResponseEntity<Response<T>> successWithHeader(MultiValueMap<String, String> header, T data) {
        HttpHeaders headers = new HttpHeaders();
        headers.addAll(header);

        return ResponseEntity.ok().headers(headers).body(Response.of(data));
    }

    public static ResponseEntity<Object> fail(Object errorDetail, ResponseStatus status) {
        return ResponseEntity.status(status.getHttpCode()).body(new ErrorTrace<>(errorDetail, status));
    }

    public static ResponseEntity<Object> fail(Throwable throwable, ResponseStatus status) {
        return ResponseEntity.status(status.getHttpCode()).body(new ErrorTrace<>(throwable, status));
    }

    public static ResponseEntity<Object> fail(ResponseStatus status) {
        return ResponseEntity.status(status.getHttpCode()).body(new ErrorTrace<>(status));
    }

    public static <T> void successHttpServletResponse(HttpServletResponse servletResponse, T data, ResponseStatus status) throws IOException {
        writeToHttpServletResponse(servletResponse, Response.success(data, status), status);
    }

    public static <T> void failHttpServletResponse(HttpServletResponse servletResponse, T data, ResponseStatus status) throws IOException {
        writeToHttpServletResponse(servletResponse, Response.success(data, status), status);
    }

    public static void writeToHttpServletResponse(HttpServletResponse servletResponse, Object response, ResponseStatus status) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String responseString = mapper.writeValueAsString(response);
        servletResponse.setCharacterEncoding(StandardCharsets.UTF_8.name());
        servletResponse.setStatus(status.getHttpCode());
        servletResponse.setHeader("Content-Type", "application/json");
        servletResponse.getWriter().write(responseString);
        servletResponse.getWriter().flush();
        servletResponse.getWriter().close();
    }

}

