package com.company.common.spring.filter;

import com.company.common.spring.filter.cachehttp.CachedBodyHttpServletRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.catalina.connector.CoyoteOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

@Configuration
@Order(1)
@ConditionalOnProperty(
        value = {"app.log-request-http"},
        havingValue = "true"
)
@SuppressWarnings("all")
public class RequestHttpFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(RequestHttpFilter.class);

    @Autowired
    ObjectMapper objectMapper;

    Map<String, String> replaceCharsError = new HashMap<>();

    public RequestHttpFilter() {
        this.replaceCharsError.put("\u0000", "");
    }

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) {
        StringBuilder str = new StringBuilder();

        try {
            ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
            CachedBodyHttpServletRequest cachedBodyHttpServletRequest = new CachedBodyHttpServletRequest(new ContentCachingRequestWrapper(request));
            str.append("\n Request = ").append(" \n").append("Request to : ").append(this.getFullURL(cachedBodyHttpServletRequest)).append(" \n").append("Method     : ").append(cachedBodyHttpServletRequest.getMethod()).append(" \n").append("Header     : ").append(cachedBodyHttpServletRequest.getHeaders()).append(" \n").append("Body       : ").append(this.replaceChars(new String(cachedBodyHttpServletRequest.getCachedBody(), StandardCharsets.UTF_8))).append(" \n").append(" \n");
            chain.doFilter(cachedBodyHttpServletRequest, responseWrapper);
            responseWrapper.copyBodyToResponse();
            str.append("\n Response = ").append(" \n").append("Status code  : {}").append(responseWrapper.getStatus()).append(" \n").append("Header     : ").append(this.getHeaders(responseWrapper)).append(" \n");
            str.append("Body       : ").append(this.getBodyResponse(responseWrapper)).append(" \n");
            str.append(" \n");
        } catch (Exception var10) {
            this.logger.error(var10.getMessage(), var10);
        } finally {
            log.info(str.toString());
        }

    }

    public String getBodyResponse(ContentCachingResponseWrapper responseWrapper) throws NoSuchFieldException, IOException, IllegalAccessException {
        Field field = CoyoteOutputStream.class.getDeclaredField("ob");
        field.setAccessible(true);
        Field field2 = field.get(responseWrapper.getResponse().getOutputStream()).getClass().getDeclaredField("bb");
        field2.setAccessible(true);
        ByteBuffer byteBuffer = (ByteBuffer)field2.get(field.get(responseWrapper.getResponse().getOutputStream()));
        Field fieldBytesWritten = field.get(responseWrapper.getResponse().getOutputStream()).getClass().getDeclaredField("bytesWritten");
        fieldBytesWritten.setAccessible(true);
        Long bytesWritten = (Long)fieldBytesWritten.get(field.get(responseWrapper.getResponse().getOutputStream()));
        if (byteBuffer.array().length < bytesWritten) {
            bytesWritten = (long)byteBuffer.array().length;
        }

        return this.replaceChars(new String(byteBuffer.array(), 0, Math.toIntExact(bytesWritten), StandardCharsets.UTF_8));
    }

    public Map<String, String> getHeaders(HttpServletResponse response) {
        Map<String, String> map = new HashMap<>();
        Collection<String> headersName = response.getHeaderNames();
        Iterator<String> var4 = headersName.iterator();

        while(var4.hasNext()) {
            String s = var4.next();
            map.put(s, response.getHeader(s));
        }

        return map;
    }

    public String getFullURL(HttpServletRequest request) {
        StringBuilder requestURL = new StringBuilder(request.getRequestURL().toString());
        String queryString = request.getQueryString();
        return queryString == null ? requestURL.toString() : requestURL.append('?').append(queryString).toString();
    }

    public String replaceChars(String str) {
        Entry entry;
        for(Iterator var2 = this.replaceCharsError.entrySet().iterator(); var2.hasNext(); str = str.replace((CharSequence)entry.getKey(), (CharSequence)entry.getValue())) {
            entry = (Entry)var2.next();
        }

        return str;
    }
}
